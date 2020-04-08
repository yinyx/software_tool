package com.nari.software_tool.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nari.software_tool.entity.*;
import com.nari.software_tool.service.FileService;
import com.nari.software_tool.service.SoftwareService;
import com.nari.software_tool.service.SoftwareKindService;

import util.aes.DatatableUtil;
import util.aes.MD5.MD5Util;
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
import java.text.ParseException;
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
        String kindId = paramObj.get("kindId").toString();
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

        //根据表单信息创建软件信息对象
        JSONObject software = JSONObject.fromObject(softwareStr);
		Map<String, Object> paramMap = software;
		System.out.println(paramMap);
        ObjectMapper objectMapper = new ObjectMapper();
        SoftwareInfo softwareInfo = objectMapper.readValue(software.toString(),SoftwareInfo.class);
		String kindId = softwareInfo.getKind();
		int install_type = softwareInfo.getInstallType();
		if (0==install_type)
        {
            //解压安装包
        }
		System.out.println("创建软件信息对象对象成功");
		Map<String,Object> map = softwareKindService.getKindById(kindId);
		
		//存储图标；
        File iconDir = new File(iconPath);

        String iconDirAbsolutePath = iconDir.getAbsolutePath();
        if(!iconDir.exists()){
            iconDir.mkdir();
        }
		
		String iconKindPath = iconDirAbsolutePath+"/"+map.get("name_en");
		File iconKindDir = new File(iconKindPath);
		if(!iconKindDir.exists()){
            iconKindDir.mkdir();
        }
		
		String iconSoftPath = iconKindPath+"/"+ softwareInfo.getNameEn();
		File iconSoftDir = new File(iconSoftPath);
		if(!iconSoftDir.exists()){
            iconSoftDir.mkdir();
        }
		
        String iconName = icon.getOriginalFilename();
		System.out.println("iconName");
		System.out.println(iconName);
        try {
            icon.transferTo(new File(iconSoftPath,iconName));
        } catch (Exception e) {
            e.printStackTrace();
        }
		
	    //设置图标保存路径
		String iconPath1 = "images/softIcon/"+map.get("name_en")+"/"+ softwareInfo.getNameEn()+"/"+iconName;
		softwareInfo.setIcon(iconPath1);
		paramMap.put("icon",iconPath1);
        
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

		//配置历史版本记录
        paramMap.put("historyVersion",softwareInfo.getLatestVersion());
		paramMap.put("historyPath",softwareInfo.getFilePath());
        paramMap.put("appPktMd5",MD5Util.getFileMD5String(new File(softDirAbsolutePath,softName)));
		//数据库增加记录

        try{
            softwareService.saveSoftware(paramMap);
            jsonObject.put("status","success");
            jsonObject.put("msg","创建软件信息对象成功！");

        }catch (Exception e){
            jsonObject.put("status","failure");
            e.printStackTrace();
        }

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
		String softwareId = (String) paramMap.get("id");	
		Map<String, Object> softwareData = new HashMap<String, Object>();
		softwareData = softwareService.getSoftwareById(softwareId);
		String kindId = (String) softwareData.get("kind");
		if (StringUtils.isEmpty(kindId))
		{
			jsonObject.put("status","failure");
			jsonObject.put("msg","获取软件类别id失败");
            return jsonObject;
		}
		Map<String,Object> kindMap = softwareKindService.getKindById(kindId);
		if (null == kindMap)
		{
			jsonObject.put("status","failure");
			jsonObject.put("msg","获取软件类别map失败");
            return jsonObject;
		}
		String kindName = (String)kindMap.get("name_en");
		String softwareName = (String)softwareData.get("name_en");
		
		
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
		else
        {
            jsonObject.put("status","failure");
            return jsonObject;
        }
		
        //存储新图标；
        File iconDir = new File(iconPath);

        String iconDirAbsolutePath = iconDir.getAbsolutePath();
        if(!iconDir.exists()){
            iconDir.mkdir();
        }
		String iconKindPath = iconDirAbsolutePath+"/"+kindName;
		File iconKindDir = new File(iconKindPath);
		if(!iconKindDir.exists()){
            iconKindDir.mkdir();
        }
		
		String iconSoftPath = iconKindPath+"/"+ softwareName;
		File iconSoftDir = new File(iconSoftPath);
		if(!iconSoftDir.exists()){
            iconSoftDir.mkdir();
        }
		
        String iconName = icon.getOriginalFilename();
		System.out.println("iconName");
		System.out.println(iconName);
        try {
            icon.transferTo(new File(iconSoftPath,iconName));
        } catch (Exception e) {
            e.printStackTrace();
        }
		
	    //设置图标保存路径
		String iconPath1 = "images/softIcon/"+kindName+"/"+ softwareName+"/"+iconName;
		paramMap.put("iconpath", iconPath1);
		
		//数据库更新记录

        try{
                softwareService.updateSoftwareIcon(paramMap);
                jsonObject.put("status","success");
                jsonObject.put("msg","更新图标成功！");

        }catch (Exception e){
            jsonObject.put("status","failure");
            e.printStackTrace();
        }

        return jsonObject;
    }


    @PostMapping("/uploadBatchScreenShots")
    @ResponseBody
    public JSONObject uploadBatchScreenShots(@RequestParam("screenShots") MultipartFile[] files,@RequestParam("softwareId") String softwareId) throws IOException {
        JSONObject jsonObject = new JSONObject();
        List<String> nameLst = new ArrayList<>();
        Map<String,Object> softMap = softwareService.getSoftwareById(softwareId);
        String type = (String) softMap.get("kind");
        Map<String,Object> kindMap = softwareKindService.getKindById(type);
        String typeName = (String) kindMap.get("name_en");
        String name_en = (String) softMap.get("name_en");
        //批量存储文件至本地file文件夹
        File fileDir = new File(iconPath);
        String path = fileDir.getAbsolutePath();
        if(!fileDir.exists()){
            fileDir.mkdir();
        }
        String typePath = path+"\\"+typeName;
        File typeDir = new File(typePath);
        if(!typeDir.exists()) {
            typeDir.mkdir();
        }
        String namePath = typePath+"\\"+name_en;
        File nameDir = new File(namePath);
        if(!nameDir.exists()) {
            nameDir.mkdir();
        }
        MultipartFile multipartFile;
        for(int i=0;i<files.length;i++){
            multipartFile = files[i];
            String fileName = multipartFile.getOriginalFilename();
            nameLst.add(fileName);
            multipartFile.transferTo(new File(namePath,fileName));
        }
        List<ScreenShotInfo> lst = new ArrayList<>();
        //设置日期格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss" );
        Map<String,Object> sfMap = softwareService.getSoftwareById(softwareId);

        for (int i=0;i<files.length;i++) {
            String shotName = nameLst.get(i);
            ScreenShotInfo ss = new ScreenShotInfo();
                    ss.setScreenId(StringUtils.getUUId());
                    ss.setSoftName((String) sfMap.get("name"));
                    ss.setCreateTime(df.format(new Date()));
                    ss.setId(softwareId);
                    ss.setShotsName(shotName);
                    ss.setUrl(namePath+"\\"+shotName);
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

    @RequestMapping(value="/querySoftwareNameIsRepeat",method=RequestMethod.POST)
    public Object  querySoftwareNameIsRepeat(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> resultMap=new HashMap<String,Object>();
        Map<String, Object> paramMap = new HashMap<String, Object>();

        paramMap.put("softwareName", request.getParameter("softwareName"));
        paramMap.put("kind", request.getParameter("kind"));

        boolean flag ;
        try {
            flag = softwareService.querySoftwareNameIsRepeat(paramMap);
            resultMap.put("status", "success");
            resultMap.put("flag",flag);
        } catch (Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "获取当前类别的软件中文名是否重名失败!");
        }
        return resultMap;
    }

    @RequestMapping(value="/querySoftwareEnNameIsRepeat",method=RequestMethod.POST)
    public Object  querySoftwareEnNameIsRepeat(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> resultMap=new HashMap<String,Object>();
        Map<String, Object> paramMap = new HashMap<String, Object>();

        paramMap.put("softwareName_en", request.getParameter("softwareName_en"));
        paramMap.put("kind", request.getParameter("kind"));

        boolean flag ;
        try {
            flag = softwareService.querySoftwareEnNameIsRepeat(paramMap);
            resultMap.put("status", "success");
            resultMap.put("flag",flag);
        } catch (Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "获取当前类别的软件英文名是否重名失败!");
        }
        return resultMap;
    }
}
