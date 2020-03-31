package com.nari.software_tool.controller;

import com.nari.software_tool.entity.DataTableModel;
import com.nari.software_tool.entity.DataTableParam;
import com.nari.software_tool.service.BranchService;
import com.nari.software_tool.service.UserService;
import com.nari.software_tool.service.VersionService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import util.aes.AesUtil;
import util.aes.DatatableUtil;
import util.aes.MD5.MD5Util;
import util.aes.StringUtils;

import javax.annotation.Resource;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/history")
public class VersionController {

    // 注入软件类别Service
    @Resource
    private VersionService versionService;

    @Resource
    private BranchService branchService;

    @Autowired
    private UserService userService;

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
        String historyId = (String) paramObj.get("historyId");
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

    @RequestMapping(value="/queryBranchBySelect")
    public Object queryBranchBySelect(@RequestParam Map<String, Object> map){
        JSONObject paramObj=AesUtil.GetParam(map);
        String softId = (String) paramObj.get("softId");
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
        if (!StringUtils.isEmpty(oldVersionDirAbsolutePath))
        {
            File file = new File(oldVersionDirAbsolutePath+"\\");
            if (file.isFile()) {
                file.delete();
                System.out.println("文件已删除");
            }
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
            versionService.updateVersion(paramMap);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject;
    }

}
