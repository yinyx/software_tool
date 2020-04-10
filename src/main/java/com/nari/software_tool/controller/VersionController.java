package com.nari.software_tool.controller;

import com.nari.software_tool.entity.DataTableModel;
import com.nari.software_tool.entity.DataTableParam;
import com.nari.software_tool.service.*;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import util.aes.AesUtil;
import util.aes.DatatableUtil;
import util.aes.MD5.MD5Util;
import util.aes.StringUtils;

import javax.annotation.Resource;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/history")
public class VersionController {

    @Value("${rootPath}")
    private String rootPath;

    // 注入软件类别Service
    @Resource
    private VersionService versionService;

    @Resource
    private BranchService branchService;

    @Autowired
    private UserService userService;

    @Autowired
    private SoftwareKindService softwareKindService;

    @Autowired
    private SoftwareService softwareService;

    @RequestMapping(value="/queryHistoryList",method=RequestMethod.POST)
    public Object  queryHistoryList(@RequestBody DataTableParam[] dataTableParams){
        DataTableModel dataTableModel = new DataTableModel();
        Map<String, String> dataTableMap = DatatableUtil.convertToMap(dataTableParams);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            dataTableModel = versionService.queryHistoryList(dataTableMap);
            resultMap.put("status", "success");
            resultMap.put("VersionData", dataTableModel);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询软件历史版本异常!");
        }
        return resultMap;
    }
    @RequestMapping(value="/getVersionById",method=RequestMethod.POST)
    public Object getVersionById(@RequestParam Map<String, Object> map){
        JSONObject paramObj=AesUtil.GetParam(map);
        String historyId = paramObj.get("historyId").toString();
        Map<String, Object> versionData = new HashMap<String, Object>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            versionData = versionService.getHistoryById(historyId);
            resultMap.put("status", "success");
            resultMap.put("versionData", versionData);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询某条版本信息异常!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }
	
	@RequestMapping(value="/getVersionInstllconfigById",method=RequestMethod.POST)
    public Object getVersionInstllconfigById(@RequestParam Map<String, Object> map){
        JSONObject paramObj=AesUtil.GetParam(map);
        String historyId = paramObj.get("history_id").toString();
        Map<String, Object> versionPkgCfgData = new HashMap<String, Object>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            versionPkgCfgData = versionService.getVersionPkgCfgById(historyId);
            resultMap.put("status", "success");
            resultMap.put("versionPkgCfgData", versionPkgCfgData);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询某条版本的安装包配置信息异常!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }

    @RequestMapping(value="/queryBranchBySelect")
    public Object queryBranchBySelect(@RequestParam Map<String, Object> map){
        JSONObject paramObj=AesUtil.GetParam(map);
        String softId = paramObj.get("softId").toString();
        Map<String, Object> resultMap=new HashMap<>();
        try {
            List<Map<String,Object>> dataList = branchService.queryBranchList(softId);
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

    @PostMapping("/updateVersion")
    @ResponseBody
    public JSONObject updateVersion(@RequestParam("upFile") MultipartFile upFile, @RequestParam("versionObj") String versionObj){
        JSONObject jsonObject = new JSONObject();
        JSONObject versionJson = JSONObject.fromObject(versionObj);
        Map<String, Object> paramMap = versionJson;
        Map<String,Object> versionMap = versionService.getHistoryById((String) paramMap.get("historyId"));
        System.out.println(versionMap.toString()+"-------------");
        String oldVersionpath = (String) versionMap.get("appPkt_path");
        File oldVersionpathDir = new File(oldVersionpath);
        String oldVersionDirAbsolutePath = oldVersionpathDir.getAbsolutePath();
        try {
            if (!StringUtils.isEmpty(oldVersionDirAbsolutePath)) {
                File file = new File(oldVersionDirAbsolutePath + "\\");
                if (file.isDirectory()) {
                    File[] files = file.listFiles();
                    for(File key:files){
                        if(key.isFile()){
                            key.delete();
                        }
                    }
                    file.delete();
                    System.out.println("文件已删除");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        String[] pathArray = oldVersionpath.split("\\\\");
        String upFileName = upFile.getOriginalFilename();
        pathArray[pathArray.length-1] = upFileName;

        String newPath = pathArray[0];
        for(int i = 1; i < pathArray.length; i++) {
            newPath = newPath + "\\" + pathArray[i];
        }
        //存储版本；
        File versionDir = new File(newPath);
        String versionDirAbsolutePath = versionDir.getAbsolutePath();
        if(!versionDir.exists()){
            versionDir.mkdir();
        }
        try {
            upFile.transferTo(new File(versionDirAbsolutePath,upFileName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            paramMap.put("historyPath",newPath);
            paramMap.put("appPktPath",newPath);
            SimpleDateFormat sdf =new SimpleDateFormat("yyyy/MM/dd HH:mm:ss" );
            paramMap.put("appPktDate",sdf.format(new Date()));
            paramMap.put("uploadDate",sdf.format(new Date()));
            Map<String,Object> userMap = userService.getUserById((String) paramMap.get("userId"));
            paramMap.put("operator",userMap.get("user_name"));
            paramMap.put("appPktSize",0);
            paramMap.put("appPktMd5", MD5Util.getFileMD5String(new File(versionDirAbsolutePath,upFileName)));
            //数据库更新记录
            if(versionService.updateVersion(paramMap) == 1){
                jsonObject.put("status","success");
            }else{
                jsonObject.put("status","failure");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject;
    }
	
	@RequestMapping(value="/setVersionInstllconfigById",method=RequestMethod.POST)
    public Map<String, Object> setVersionInstllconfigById(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        //平台类型
        paramMap.put("history_id", request.getParameter("AppPktId"));
        paramMap.put("appPkt_date", request.getParameter("appPkt_date"));
        try {
            versionService.setVersionInstllconfigById(paramMap);
            resultMap.put("status", "success");
            resultMap.put("msg", "软件版本对应程序包信息保存成功!");
        } catch(Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "软件版本对应程序包信息保存失败!");
        }
        return resultMap;
    }


    @RequestMapping(value="/addVersionInfo",method=RequestMethod.POST)
    @ResponseBody
    public JSONObject addVersionInfo(@RequestParam("soft") MultipartFile soft, @RequestParam("versionObj") String versionObj){
        JSONObject jsonObject = new JSONObject();
        Map<String,Object> paramMap = JSONObject.fromObject(versionObj);
        System.out.println(paramMap+"----------");

        if((paramMap.get("kindId") == "0")&&(paramMap.get("softwareId")=="0")&&
        (paramMap.get("branchId") == "0")){
            jsonObject.put("status","failure");
        }

        Map<String,Object> typeMap = softwareKindService.getKindById((String) paramMap.get("kindId"));
        Map<String,Object> softMap = softwareService.getSoftwareById((String) paramMap.get("softwareId"));
        Map<String,Object> branchMap = branchService.getBranchById((String) paramMap.get("branchId"));
        Map<String,Object> userMap = userService.getUserById((String) paramMap.get("userId"));

        String root = rootPath;
        File rootDir = new File(root);

        String typePath = rootPath+"/"+typeMap.get("name_en");
        File typeDir = new File(typePath);

        String namePath = rootPath+"/"+typeMap.get("name_en")+"/"+ softMap.get("name_en");
        File nameDir = new File(namePath);

        String branchPath = rootPath+"/"+typeMap.get("name_en")+"/"+ softMap.get("name_en")+"/"+branchMap.get("branch");
        File branchDir = new File(branchPath);

        String versionPath = rootPath+"/"+typeMap.get("name_en")+"/"+ softMap.get("name_en")+"/"+branchMap.get("branch")+"/"+paramMap.get("historyVersion");
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
        File softDir = new File(versionPath);
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

        try{
            Map<String,Object> versionMap = new HashMap<>();
            versionMap.put("historyId",StringUtils.getUUId());
            versionMap.put("softId",paramMap.get("softwareId"));
            versionMap.put("branchId",paramMap.get("branchId"));
            versionMap.put("historyVersion",paramMap.get("historyVersion"));
            versionMap.put("historyPath",softDirAbsolutePath);
            versionMap.put("uploadDate"," ");
            versionMap.put("operator",userMap.get("user_name"));
            versionMap.put("appPktNew",paramMap.get("appPktNew"));
            versionMap.put("appPktMd5",MD5Util.getFileMD5String(new File(softDirAbsolutePath,softName)));
            versionMap.put("appPktSize",soft.getSize());
            versionMap.put("appPktPath",versionPath);
            SimpleDateFormat sdf =new SimpleDateFormat("yyyy/MM/dd HH:mm:ss" );
            versionMap.put("appPktDate",sdf.format(new Date()));

            if(versionService.addVersion(versionMap) ==1){
                jsonObject.put("status","success");
                jsonObject.put("msg","增加软件版本成功！");
            }else{
                jsonObject.put("status","failure");
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return jsonObject;
    }

}
