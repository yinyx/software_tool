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

    @Value("${pluginPath}")
    private String pluginPath;

    @Value("${iconPath}")
    private String iconPath;

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
    @RequestMapping(value="/getPluginById",method=RequestMethod.POST)
    public Object getPluginById(@RequestParam Map<String, Object> map){
        JSONObject paramObj=AesUtil.GetParam(map);
        String pluginId = paramObj.get("pluginId").toString();
        Map<String, Object> pluginData = new HashMap<String, Object>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            pluginData = pluginService.getPluginById(pluginId);
            resultMap.put("status", "success");
            resultMap.put("pluginData", pluginData);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询某条插件信息异常!");
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
        logger.info("---插件界面版本列表查询--- branchId:  "+branchId);
        try {
            List<Map<String,Object>> dataList = versionService.queryVersionByBranchId(branchId);
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

    @PostMapping("/updatePlugin")
    @ResponseBody
    public JSONObject updatePlugin(@RequestParam("upIcon") MultipartFile upIcon, @RequestParam("upFile") MultipartFile upFile, @RequestParam("pluginStr") String pluginStr){
        JSONObject jsonObject = new JSONObject();
        JSONObject paramMap = JSONObject.fromObject(pluginStr);
        Map<String,Object> pluginMap = pluginService.getPluginById((String) paramMap.get("pluginId"));
        //检索现有插件存储路径
        String oldPluginPath = (String) pluginMap.get("absolute_path");
        File oldPluginPathDir = new File(oldPluginPath);
        String oldPluginDirAbsolutePath = oldPluginPathDir.getAbsolutePath();
        String[] oldPathArray = oldPluginDirAbsolutePath.split("/");
        String oldPath = oldPathArray[0];
        for(int i = 1; i < oldPathArray.length-1; i++) {
            oldPath = oldPath + "/" + oldPathArray[i];
        }
        try {
            if (!StringUtils.isEmpty(oldPath)) {
                File file = new File(oldPath + "/");
                if (file.isDirectory()) {
                    File[] files = file.listFiles();
                    for(File key:files){
                        if(key.isFile()){
                            key.delete();
                        }
                    }
                    file.delete();
                    logger.info("---插件"+pluginMap.get("plugin_name")+"已更新---");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        String[] pathArray = oldPluginPath.split("/");
        String upFileName = upFile.getOriginalFilename();
        pathArray[pathArray.length-1] = null;

        String newPath = pathArray[0];
        for(int i = 1; i < pathArray.length-1; i++) {
            newPath = newPath + "/" + pathArray[i];
        }
        //存储版本；
        File newPluginDir = new File(newPath);
        String pluginDirAbsolutePath = newPluginDir.getAbsolutePath();
        if(!newPluginDir.exists()){
            newPluginDir.mkdir();
        }
        try {
            upFile.transferTo(new File(pluginDirAbsolutePath,upFileName));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //检索现有插件图标存储路径
        String oldIconPath = (String) pluginMap.get("icon");
        File oldIconPathDir = new File(oldIconPath);
        String oldIconPathAbsolutePath = oldIconPathDir.getAbsolutePath();
        String[] oldIconArray = oldIconPathAbsolutePath.split("/");
        String oldIcon = oldIconArray[0];
        for(int i = 1; i < oldIconArray.length-1; i++) {
            oldIcon = oldIcon + "/" + oldIconArray[i];
        }
        try {
            if (!StringUtils.isEmpty(oldIcon)) {
                File file = new File(oldIcon + "/");
                if (file.isDirectory()) {
                    File[] files = file.listFiles();
                    for(File key:files){
                        if(key.isFile()){
                            key.delete();
                        }
                    }
                    file.delete();
                    logger.info("---插件"+pluginMap.get("plugin_name")+"图标已更新---");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        String[] iconArray = oldIconPath.split("/");
        String upIconName = upIcon.getOriginalFilename();
        iconArray[iconArray.length-1] = null;

        String newIconPath = iconArray[0];
        for(int i = 1; i < iconArray.length-1; i++) {
            newIconPath = newIconPath + "/" + iconArray[i];
        }
        //存储版本；
        File newIconDir = new File(newIconPath);
        String iconDirAbsolutePath = newIconDir.getAbsolutePath();
        if(!newPluginDir.exists()){
            newPluginDir.mkdir();
        }
        try {
            upIcon.transferTo(new File(iconDirAbsolutePath,upIconName));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try{
        if(pluginMap.get("plugin_id").equals(paramMap.get("pluginId"))){
            pluginMap.put("pluginId",paramMap.get("pluginId"));
        }
        if(!pluginMap.get("plugin_name").equals(paramMap.get("pluginName"))){
            pluginMap.put("pluginName",paramMap.get("pluginName"));
        }
        if(!pluginMap.get("relative_path").equals(paramMap.get("relativePath"))){
            pluginMap.put("relativePath",paramMap.get("relativePath"));
        }
            pluginMap.put("icon",iconDirAbsolutePath+"/"+upIconName);
            pluginMap.put("absolutePath",pluginDirAbsolutePath+"/"+upFileName);
            pluginMap.put("description",paramMap.get("description"));
            SimpleDateFormat sdf =new SimpleDateFormat("yyyy/MM/dd HH:mm:ss" );
            pluginMap.put("uploadTime",sdf.format(new Date()));
            Map<String,Object> userMap = userService.getUserById((String) paramMap.get("userId"));
            pluginMap.put("operator",userMap.get("user_name"));
            pluginMap.put("size",upFile.getSize());
            if(!pluginMap.get("plugin_MD5").equals(MD5Util.getFileMD5String(new File(pluginDirAbsolutePath,upFileName)))){
                pluginMap.put("pluginMD5", MD5Util.getFileMD5String(new File(pluginDirAbsolutePath,upFileName)));
            }
            //数据库更新记录
            if(pluginService.updatePlugin(pluginMap) == 1){
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
    public JSONObject addVersionInfo(@RequestParam("pluginPkt") MultipartFile pluginPkt,@RequestParam("pluginIcon") MultipartFile pluginIcon, @RequestParam("pluginObj") String pluginStr){
        JSONObject jsonObject = new JSONObject();
        logger.info("当前上传插件 ==============================="+pluginStr+"===============================");
        JSONObject paramMap = JSONObject.fromObject(pluginStr);
        if((paramMap.get("type") == "0")&&(paramMap.get("softId")=="0")&&
        (paramMap.get("branchId") == "0")&&(paramMap.get("versionId")=="0")&&(paramMap.get("userId")=="0")){
            jsonObject.put("status","failure");
        }

        Map<String,Object> typeMap = softwareKindService.getKindById((String) paramMap.get("type"));
        Map<String,Object> softMap = softwareService.getSoftwareById((String) paramMap.get("softId"));
        Map<String,Object> branchMap = branchService.getBranchById((String) paramMap.get("branchId"));
        Map<String,Object> versionMap = versionService.getHistoryById((String) paramMap.get("versionId"));
        Map<String,Object> userMap = userService.getUserById((String) paramMap.get("userId"));

        String root = pluginPath;
        File rootDir = new File(root);

        String typePath = pluginPath+"/"+typeMap.get("name_en");
        File typeDir = new File(typePath);

        String namePath = pluginPath+"/"+typeMap.get("name_en")+"/"+ softMap.get("name_en");
        File nameDir = new File(namePath);

        String branchPath = pluginPath+"/"+typeMap.get("name_en")+"/"+ softMap.get("name_en")+"/"+branchMap.get("branch");
        File branchDir = new File(branchPath);

        String versionPath = pluginPath+"/"+typeMap.get("name_en")+"/"+ softMap.get("name_en")+"/"+branchMap.get("branch")+"/"+versionMap.get("history_version");
        File versionDir = new File(versionPath);

        String pluginNamePath = pluginPath+"/"+typeMap.get("name_en")+"/"+ softMap.get("name_en")+"/"+branchMap.get("branch")+"/"+versionMap.get("history_version")+"/"+paramMap.get("pluginName");
        File pluginNameDir = new File(pluginNamePath);

        String finalPath = pluginPath+"/"+typeMap.get("name_en")+"/"+ softMap.get("name_en")+"/"+branchMap.get("branch")+"/"+versionMap.get("history_version")+"/"+paramMap.get("pluginName")+"/"+paramMap.get("pluginVersion");
        File finalDir = new File(finalPath);

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

        if(!pluginNameDir.exists()) {
            pluginNameDir.mkdir();
        }

        if(!finalDir.exists()) {
            finalDir.mkdir();
        }

        //存储文件；
        File pluginDir = new File(finalPath);
        String pluginDirAbsolutePath = pluginDir.getAbsolutePath();
        if(!pluginDir.exists()){
            pluginDir.mkdir();
        }
        String pluginFileName = pluginPkt.getOriginalFilename();
        try {
            pluginPkt.transferTo(new File(pluginDirAbsolutePath,pluginFileName));
        } catch (Exception e) {
            e.printStackTrace();
        }

        String pluginIconPath = iconPath+"/"+typeMap.get("name_en")+"/"+ softMap.get("name_en");
        File pluginIconDir = new File(pluginIconPath);
        String pluginIconDirAbsolutePath = pluginIconDir.getAbsolutePath();
        if(!pluginDir.exists()){
            pluginDir.mkdir();
        }
        String pluginIconName = pluginIcon.getOriginalFilename();
        try{
            pluginIcon.transferTo(new File(pluginIconDirAbsolutePath,pluginIconName));
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            Map<String,Object> pluginMap = new HashMap<>();
            pluginMap.put("pluginId",StringUtils.getUUId());
            pluginMap.put("Type",paramMap.get("type"));
            pluginMap.put("softId",paramMap.get("softId"));
            pluginMap.put("Branch",paramMap.get("branchId"));
            pluginMap.put("Version",paramMap.get("versionId"));
            pluginMap.put("pluginName",paramMap.get("pluginName"));
            pluginMap.put("pluginMD5",MD5Util.getFileMD5String(new File(pluginDirAbsolutePath,pluginFileName)));
            SimpleDateFormat sdf =new SimpleDateFormat("yyyy/MM/dd HH:mm:ss" );
            pluginMap.put("uploadTime",sdf.format(new Date()));
            Map<String,Object> installMap = softwareService.getInstallConfigBySoftwareId((String) paramMap.get("softId"));
            pluginMap.put("relativePath",installMap.get("plugin_Dir"));
            pluginMap.put("absolutePath",pluginDirAbsolutePath+"/"+pluginFileName);
            pluginMap.put("operator",userMap.get("user_name"));
            pluginMap.put("description",paramMap.get("description"));
            pluginMap.put("size",pluginPkt.getSize());
            pluginMap.put("pluginIcon",pluginIconDirAbsolutePath+"/"+pluginIconName);
            pluginMap.put("pluginVersion",paramMap.get("pluginVersion"));

            if(pluginService.addPlugin(pluginMap) ==1){
                jsonObject.put("status","success");
            }else{
                jsonObject.put("status","failure");
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return jsonObject;
    }

    @RequestMapping(value="/getPluginInstllconfigById",method=RequestMethod.POST )
        public Object getPluginInstllconfigById(@RequestParam Map<String, Object> map){
        JSONObject paramObj=AesUtil.GetParam(map);
        String pluginId = paramObj.get("plugin_id").toString();
        Map<String, Object> pluginPkgCfgData = new HashMap<String, Object>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            pluginPkgCfgData = pluginService.getPluginPkgCfgById(pluginId);
            resultMap.put("status", "success");
            resultMap.put("pluginPkgCfgData", pluginPkgCfgData);
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

    @RequestMapping(value="/setPluginInstllconfigById",method=RequestMethod.POST)
    public Map<String, Object> setPluginInstllconfigById(HttpServletRequest request, HttpServletResponse response){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        //平台类型
        paramMap.put("pluginId", request.getParameter("AppPktId"));
        paramMap.put("appPktDate", request.getParameter("appPkt_date"));
        paramMap.put("keyFile", request.getParameter("key_file"));
        paramMap.put("keyFileMd5", request.getParameter("key_file_md5"));
        try {
            pluginService.setPluginInstllconfigById(paramMap);
            resultMap.put("status", "success");
            resultMap.put("msg", "软件版本对应程序包信息保存成功!");
        } catch(Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "软件版本对应程序包信息保存失败!");
        }
        return resultMap;
    }

}
