package com.nari.software_tool.controller;

import com.nari.software_tool.entity.DataTableModel;
import com.nari.software_tool.entity.DataTableParam;
import com.nari.software_tool.service.*;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import util.aes.AesUtil;
import util.aes.DatatableUtil;
import util.aes.MD5.MD5Util;
import util.aes.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/plugin")
public class PluginController {

    Logger logger =  LoggerFactory.getLogger(getClass());

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

    @Autowired
    private PluginService pluginService;

    @RequestMapping(value="/queryPluginList",method=RequestMethod.POST)
    public Object  queryHistoryList(@RequestBody DataTableParam[] dataTableParams){
        DataTableModel dataTableModel = new DataTableModel();
        Map<String, String> dataTableMap = DatatableUtil.convertToMap(dataTableParams);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            dataTableModel = pluginService.queryPluginList(dataTableMap);
            resultMap.put("status", "success");
            resultMap.put("PluginData", dataTableModel);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询插件异常!");
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


    @RequestMapping(value="/queryVersionBySelect")
    public Object queryVesionBySelect(@RequestParam Map<String, Object> map){
        JSONObject paramObj=AesUtil.GetParam(map);
        String branchId = paramObj.get("branchId").toString();
        Map<String, Object> resultMap=new HashMap<>();
        try {
            List<Map<String,Object>> dataList = versionService.queryVersionByHistoryId(branchId);
            resultMap.put("status", "success");
            resultMap.put("dataList",dataList);
        } catch (Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "获取版本列表信息异常!");
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

    @RequestMapping(value="/addPlugin",method=RequestMethod.POST)
    @ResponseBody
    public JSONObject addVersionInfo(@RequestParam("pluginPkt") MultipartFile pluginPkt, @RequestParam("pluginObj") Map<String,Object> pluginObj){
        JSONObject jsonObject = new JSONObject();
        logger.info("当前上传插件==========================================================="+pluginObj+"===========================================================");

//        if((pluginObj.get("type") == "0")&&(pluginObj.get("softId")=="0")&&
//        (pluginObj.get("branchId") == "0")&&(pluginObj.get("versionId")=="0")&&(pluginObj.get("userId")=="0")){
//            jsonObject.put("status","failure");
//        }
//
//        Map<String,Object> typeMap = softwareKindService.getKindById((String) pluginObj.get("kindId"));
//        Map<String,Object> softMap = softwareService.getSoftwareById((String) pluginObj.get("softwareId"));
//        Map<String,Object> branchMap = branchService.getBranchById((String) pluginObj.get("branchId"));
//        Map<String,Object> userMap = userService.getUserById((String) pluginObj.get("userId"));
//
//        String root = rootPath;
//        File rootDir = new File(root);
//
//        String typePath = rootPath+"/"+typeMap.get("name_en");
//        File typeDir = new File(typePath);
//
//        String namePath = rootPath+"/"+typeMap.get("name_en")+"/"+ softMap.get("name_en");
//        File nameDir = new File(namePath);
//
//        String branchPath = rootPath+"/"+typeMap.get("name_en")+"/"+ softMap.get("name_en")+"/"+branchMap.get("branch");
//        File branchDir = new File(branchPath);
//
//        String versionPath = rootPath+"/"+typeMap.get("name_en")+"/"+ softMap.get("name_en")+"/"+branchMap.get("branch")+"/"+pluginObj.get("historyVersion");
//        File versionDir = new File(versionPath);
//
//        if(!rootDir.exists()) {
//            rootDir.mkdir();
//        }
//
//        if(!typeDir.exists()) {
//            typeDir.mkdir();
//        }
//
//        if(!nameDir.exists()) {
//            nameDir.mkdir();
//        }
//
//        if(!branchDir.exists()) {
//            branchDir.mkdir();
//        }
//
//        if(!versionDir.exists()) {
//            versionDir.mkdir();
//        }
//
//        //存储文件；
//        File softDir = new File(versionPath);
//        String softDirAbsolutePath = softDir.getAbsolutePath();
//        if(!softDir.exists()){
//            softDir.mkdir();
//        }
//        String softName = pluginPkt.getOriginalFilename();
//        try {
//            pluginPkt.transferTo(new File(softDirAbsolutePath,softName));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        try{
//            Map<String,Object> versionMap = new HashMap<>();
//            versionMap.put("historyId",StringUtils.getUUId());
//            versionMap.put("softId",pluginObj.get("softwareId"));
//            versionMap.put("branchId",pluginObj.get("branchId"));
//            versionMap.put("historyVersion",pluginObj.get("historyVersion"));
//            versionMap.put("historyPath",softDirAbsolutePath);
//            versionMap.put("uploadDate"," ");
//            versionMap.put("operator",userMap.get("user_name"));
//            versionMap.put("appPktNew",pluginObj.get("appPktNew"));
//            versionMap.put("appPktMd5",MD5Util.getFileMD5String(new File(softDirAbsolutePath,softName)));
//            versionMap.put("appPktSize",pluginPkt.getSize());
//            versionMap.put("appPktPath",versionPath);
//            SimpleDateFormat sdf =new SimpleDateFormat("yyyy/MM/dd HH:mm:ss" );
//            versionMap.put("appPktDate",sdf.format(new Date()));
//
//            if(versionService.addVersion(versionMap) ==1){
                jsonObject.put("status","success");
//            }else{
//                jsonObject.put("status","failure");
//            }
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }

        return jsonObject;
    }

}
