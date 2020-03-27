package com.nari.software_tool.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nari.software_tool.entity.ScreenShotInfo;
import com.nari.software_tool.entity.SoftwareInfo;
import com.nari.software_tool.service.FileService;
import com.nari.software_tool.service.SoftwareService;
import com.nari.software_tool.service.SoftwareKindService;
import com.nari.software_tool.entity.DataTableModel;
import com.nari.software_tool.entity.DataTableParam;

import util.aes.DatatableUtil;
import util.aes.StringUtils;
import util.aes.AesUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yinyx
 * @version 1.0 2020/3/19
 */
@RestController
@RequestMapping(value = "/software")
public class SoftwareController {

    @Value("${iconPath}")
    private String iconPath;

    @Value("${rootPath}")
    private String rootPath;

    @Autowired
    private SoftwareKindService softwareKindService;

    @Autowired
    private FileService fileService;

    // 注入软件类别Service
    @Autowired
    private SoftwareService softwareService;

    @RequestMapping(value="/queryMarqueeInfo",method=RequestMethod.POST)
    @ResponseBody
    public Object  queryMarqueeInfo(){ 
        Map<String, Object> resultMap=new HashMap<String,Object>();
        Map<String, Object> paramMap = new HashMap<String,Object>();
        try {
            int allSoftwareNum = softwareService.querySoftwareCountByKind(null);
            List<Map<String, Object>> allKinds = softwareKindService.queryAllKinds();
			int kindnum = 0;

            for (int i = 0; i<allKinds.size(); i++)
            {
                Map<String, Object> kindMap = allKinds.get(i);
                if (kindMap != null)
                {
                    String kindId = (String) kindMap.get("id");
                    String kindName = (String) kindMap.get("kind_name");
                    kindnum = softwareService.querySoftwareCountByKind(kindId);
                    paramMap.put(kindName, kindnum);
                }
            }
			
            resultMap.put("status", "success");
            resultMap.put("allSoftwareNum",allSoftwareNum);
			resultMap.put("eachSoftwareNum",paramMap);

        } catch (Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "获取滚动栏信息异常!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }

    @RequestMapping(value="/querySoftwaresByKind")
    public Object querySoftwaresByKind(@RequestParam Map<String, Object> map){
        JSONObject paramObj=AesUtil.GetParam(map);
        String kindId = (String) paramObj.get("kindId");
		Map<String, Object> resultMap=new HashMap<String,Object>();
        try {
            List<Map<String,Object>> dataList = softwareService.querySoftwaresByKind(kindId);
            resultMap.put("status", "success");
            resultMap.put("dataList",dataList);
        } catch (Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "获取某一类软件列表信息异常!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }

    @RequestMapping(value="/querySoftwaresList",method=RequestMethod.POST)
    public Object  querySoftwaresList(@RequestBody DataTableParam[] dataTableParams){

        DataTableModel dataTableModel = new DataTableModel();
        Map<String, String> dataTableMap = DatatableUtil.convertToMap(dataTableParams);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            dataTableModel = softwareService.querySoftwaresList(dataTableMap);
            resultMap.put("status", "success");
            resultMap.put("SoftwaresData", dataTableModel);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询软件信息异常!");
        }
        return resultMap;
    }
	
	@RequestMapping(value="/getSoftwareById",method=RequestMethod.POST)
    public Object getSoftwareById(@RequestParam Map<String, Object> map){
        JSONObject paramObj=AesUtil.GetParam(map);
        String softwareId = (String) paramObj.get("softwareId");
        System.out.println(softwareId);
        Map<String, Object> softwareData = new HashMap<String, Object>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            softwareData = softwareService.getSoftwareById(softwareId);
            resultMap.put("status", "success");
            resultMap.put("softwareData", softwareData);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询某条软件信息异常!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }
	
	@RequestMapping(value="/getBranchConfigBySoftwareId",method=RequestMethod.POST)
    public Object getBranchConfigBySoftwareId(@RequestBody DataTableParam[] dataTableParams){
        DataTableModel dataTableModel = new DataTableModel();
        Map<String, String> dataTableMap = DatatableUtil.convertToMap(dataTableParams);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            dataTableModel = softwareService.queryBranchList(dataTableMap);
            resultMap.put("status", "success");
            resultMap.put("BranchsData", dataTableModel);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询软件分支信息异常!");
        }
        return resultMap;
    }
	
	@RequestMapping(value="/getInstallConfigBySoftwareId",method=RequestMethod.POST)
    public Object getInstallConfigBySoftwareId(@RequestParam Map<String, Object> map){
        JSONObject paramObj=AesUtil.GetParam(map);
        String softwareId = (String) paramObj.get("softwareId");
        System.out.println(softwareId);
        Map<String, Object> InstallConfig = new HashMap<String, Object>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            InstallConfig = softwareService.getInstallConfigBySoftwareId(softwareId);
			if (InstallConfig.size() != 0)
			{
				resultMap.put("status", "success");
                resultMap.put("InstallConfig", InstallConfig);
			}
			else
			{
				resultMap.put("status", "error");
                resultMap.put("msg", "没有查询到该条软件安装配置信息!");
			}
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询某条软件安装配置信息异常!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }
	
	
	@RequestMapping(value="/deleteWholeSoftware",method=RequestMethod.POST)
    public Object deleteWholeSoftware(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> resultMap = new HashMap<String, Object>();

        String softwareId = request.getParameter("softwareId");

        try {
			//删除文件和图标
			softwareService.deleteIcon(softwareId);
			softwareService.deleteDir(rootPath, softwareId);
            boolean flag = softwareService.deleteSoftware(softwareId);

			
            if(flag){
                resultMap.put("status", "success");
                resultMap.put("msg", "删除成功!");
            }else{
                resultMap.put("status", "error");
                resultMap.put("msg", "删除失败!");
            }
        } catch(Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "删除失败!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }
	
	@RequestMapping(value="/updateSoftware",method=RequestMethod.POST)
    public Map<String, Object> updateSoftware(HttpServletRequest request,HttpServletResponse response){
				System.out.println("paramMap");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        //平台类型
        paramMap.put("id", request.getParameter("recordId_attribute"));
        paramMap.put("name", request.getParameter("softwareName_attribute"));
        paramMap.put("name_en", request.getParameter("softwareName_enattribute"));
		paramMap.put("brief_introduction", request.getParameter("description_attribute"));
        paramMap.put("kind", request.getParameter("kind"));
        paramMap.put("install_type", request.getParameter("installType"));
		
		System.out.println(paramMap);
		
        try {
            softwareService.updateSoftware(paramMap);
            resultMap.put("status", "success");
            resultMap.put("msg", "软件更新成功!");
        } catch(Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "软件更新失败!");
        }
        return resultMap;
    }

	@RequestMapping(value="/updateInstallAttribute",method=RequestMethod.POST)
    public Map<String, Object> updateInstallAttribute(HttpServletRequest request,HttpServletResponse response){
				System.out.println("paramMap");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        //平台类型
        paramMap.put("installType", request.getParameter("installType"));
        paramMap.put("multiFlag", request.getParameter("multiFlag"));
        paramMap.put("Installer_installAttribute", request.getParameter("Installer_installAttribute"));
		paramMap.put("Uninstaller_installAttribute", request.getParameter("Uninstaller_installAttribute"));
        paramMap.put("KeyFile_installAttribute", request.getParameter("KeyFile_installAttribute"));
		paramMap.put("softwareId_attribute", request.getParameter("softwareId_attribute"));
		
		System.out.println(paramMap);
		
        try {
            softwareService.updateInstallAttribute(paramMap);
            resultMap.put("status", "success");
            resultMap.put("msg", "安装配置信息更新成功!");
        } catch(Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "安装配置信息更新失败!");
        }
        return resultMap;
    }

    @PostMapping("/uploadSoftware")
    @ResponseBody
    public JSONObject uploadSoftware(@RequestParam("icon") MultipartFile icon, @RequestParam("soft") MultipartFile soft, @RequestParam("softwareForm") String softwareStr) throws IOException {
        JSONObject jsonObject = new JSONObject();
        //存储图标；
        File iconDir = new File(iconPath);

        String iconDirAbsolutePath = iconDir.getAbsolutePath();
        if(!iconDir.exists()){
            iconDir.mkdir();
        }
        String iconName = icon.getOriginalFilename();
		System.out.println("iconName");
		System.out.println(iconName);
        try {
            icon.transferTo(new File(iconDirAbsolutePath,iconName));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //根据表单信息创建软件信息对象
        JSONObject software = JSONObject.fromObject(softwareStr);
		Map<String, Object> paramMap = software;
		System.out.println(paramMap);
        ObjectMapper objectMapper = new ObjectMapper();
        SoftwareInfo softwareInfo = objectMapper.readValue(software.toString(),SoftwareInfo.class);
		
		System.out.println("创建软件信息对象对象成功");
		
	    //设置图标保存路径
		String iconPath1 = "images/softIcon/"+iconName;
		softwareInfo.setIcon(iconPath1);
		paramMap.put("icon",iconPath1);

        String kindId = softwareInfo.getKind();
		int install_type = softwareInfo.getInstallType();
		if (0==install_type)
        {
            //解压安装包
        }

        Map<String,Object> map = softwareKindService.getKindById(kindId);
        String softPath = rootPath+"/"+map.get("name_en")+"/"+ softwareInfo.getNameEn()+"/"+"master"+"/"+softwareInfo.getLatestVersion();
        
        String root = rootPath;
        File rootDir = new File(root);
        
        String typePath = rootPath+"/"+map.get("name_en");
        File typeDir = new File(typePath);
        
        String namePath = rootPath+"/"+map.get("name_en")+"/"+ softwareInfo.getNameEn();
        File nameDir = new File(namePath);
        
        String branchPath = rootPath+"/"+map.get("name_en")+"/"+ softwareInfo.getNameEn()+"/"+"master";
        File branchDir = new File(branchPath);

        String versionPath = rootPath+"/"+map.get("name_en")+"/"+ softwareInfo.getNameEn()+"/"+"master"+"/"+softwareInfo.getLatestVersion();
        File versionDir = new File(versionPath);
        
		if(!rootDir.exists()) {
          rootDir.mkdir();
		}
		
		if(!rootDir.exists()) {
          rootDir.mkdir();
		}
		
		if(!typeDir.exists()) {
          typeDir.mkdir();
		}
		
		if(!nameDir.exists()) {
          nameDir.mkdir();
		}
		
		if(!branchDir.exists()) {
          branchDir.mkdir();
		}

        if(!versionDir.exists()) {
            versionDir.mkdir();
        }
		
        //存储文件；
        File softDir = new File(softPath);
        String softDirAbsolutePath = softDir.getAbsolutePath();
        if(!softDir.exists()){
            softDir.mkdir();
        }
        String softName = soft.getOriginalFilename();
        try {
            soft.transferTo(new File(softDirAbsolutePath,softName));
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		//设置安装文件保存路径
		String FilePath = softDirAbsolutePath+"\\"+softName;
		softwareInfo.setFilePath(FilePath);
		paramMap.put("file_path",FilePath);
		
		//设置软件大小，暂时设为0
		softwareInfo.setSize("0");
		paramMap.put("size","0");
		
		//数据库增加记录
		softwareService.saveSoftware(paramMap);

        return jsonObject;
    }
	
	@PostMapping("/uploadIcon")
    @ResponseBody
    public JSONObject uploadIcon(@RequestParam("icon") MultipartFile icon, @RequestParam("editIconForm") String softwareStr) throws IOException {
        JSONObject jsonObject = new JSONObject();
		JSONObject software = JSONObject.fromObject(softwareStr);
		Map<String, Object> paramMap = software;
		System.out.println(paramMap);
		System.out.println("paramMap");
		String oldIconpath = (String) paramMap.get("iconpath");		
		oldIconpath = "src/main/resources/static/"+oldIconpath;
		File oldIconpathDir = new File(oldIconpath);
		String oldIconDirAbsolutePath = oldIconpathDir.getAbsolutePath();
		System.out.println(oldIconDirAbsolutePath);
		
		if (!StringUtils.isEmpty(oldIconDirAbsolutePath))
		{
			//删除原来的图标
		    File file = new File(oldIconDirAbsolutePath);
            if (file.exists()) {
                file.delete();
                System.out.println("文件已删除");
            }
		}
		
        //存储新图标；
        File iconDir = new File(iconPath);

        String iconDirAbsolutePath = iconDir.getAbsolutePath();
        if(!iconDir.exists()){
            iconDir.mkdir();
        }
        String iconName = icon.getOriginalFilename();
		System.out.println("iconName");
		System.out.println(iconName);
        try {
            icon.transferTo(new File(iconDirAbsolutePath,iconName));
        } catch (Exception e) {
            e.printStackTrace();
        }
		
	    //设置图标保存路径
		String iconPath1 = "images/softIcon/"+iconName;
		System.out.println("iconPath1");
		System.out.println(iconPath1);
		paramMap.put("iconpath", iconPath1);
		
		//数据库更新记录
		softwareService.updateSoftwareIcon(paramMap);

        return jsonObject;
    }

    @PostMapping("/downloadSoftware")
    @ResponseBody
    public String downloadSoftware(HttpServletRequest request,HttpServletResponse response) throws IOException {
        JSONObject jsonObject = new JSONObject();
        String fileName = (request.getParameter("fileName"));
        if(fileName != null){
            File file = new File(request.getParameter("softwareUrl"));
            if(file.exists()){
                response.setContentType("application/force-download");
                response.addHeader("Content-Disposition","attachment;fileName="+fileName);
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try{
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while(i != -1){
                        os.write(buffer,0,i);
                        i = bis.read(buffer);
                    }
                    jsonObject.put("result","download success");
                    return jsonObject.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    bis.close();
                    fis.close();
                }
            }
        }
        jsonObject.put("result","failure");
        return jsonObject.toString();
    }

    @PostMapping("/uploadBatchScreenShots")
    @ResponseBody
    public JSONObject uploadBatchScreenShots(@RequestParam("screenShots") MultipartFile[] files,@RequestParam("softwareId") String softwareId) throws IOException {
        JSONObject jsonObject = new JSONObject();
        List<String> nameLst = new ArrayList<>();
        //批量存储文件至本地file文件夹
        File fileDir = new File(iconPath);
        String path = fileDir.getAbsolutePath();
        if(!fileDir.exists()){
            fileDir.mkdir();
        }
        MultipartFile multipartFile;
        for(int i=0;i<files.length;i++){
            multipartFile = files[i];
            String fileName = multipartFile.getOriginalFilename();
            nameLst.add(fileName);
            multipartFile.transferTo(new File(path,fileName));
        }
        List<ScreenShotInfo> lst = new ArrayList<>();
        SimpleDateFormat df = new SimpleDateFormat("yyMMdd");//设置日期格式
        Map<String,Object> sfMap = softwareService.getSoftwareById(softwareId);
        for (int i=0;i<files.length;i++) {
            String shotName = nameLst.get(i);
            ScreenShotInfo ss = new ScreenShotInfo();
                    ss.setSoftName((String) sfMap.get("name"));
                    ss.setCreateTime(df.format(new Date()));
                    ss.setId(softwareId);
                    ss.setShotsName(shotName);
                    ss.setUrl(path+"\\"+shotName);
                    ss.setShotId(i);
                lst.add(ss);
        }
        System.out.println(lst.toString());
        try{
                if(softwareService.batchInsertScreenShots(lst)){
                    jsonObject.put("status","success");
                }else{
                    jsonObject.put("status","failure");
                }
        }catch (Exception e){
            e.printStackTrace();
        }

        return jsonObject;
    }

}
