package com.nari.software_tool.controller;

import com.nari.software_tool.entity.DataTableModel;
import com.nari.software_tool.entity.DataTableParam;
import com.nari.software_tool.service.BranchService;
import com.nari.software_tool.service.VersionService;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import util.aes.AesUtil;
import util.aes.DatatableUtil;

import javax.annotation.Resource;
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
}
