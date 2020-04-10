package com.nari.software_tool.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import com.nari.software_tool.service.SoftwareKindService;
import com.nari.software_tool.entity.DataTableModel;
import com.nari.software_tool.entity.DataTableParam;

import net.sf.json.JSONObject;
import util.aes.AesUtil;
import util.aes.DatatableUtil;

@RestController
@RequestMapping(value = "/softwareKind")
public class SoftwareKindController {
    @Value("${iconPath}")
    private String iconPath;

    @Value("${rootPath}")
    private String rootPath;

    @Resource
    private SoftwareKindService softwareKindService;

    @RequestMapping(value="/queryAllKinds")
    public Object  queryAllKinds(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> resultMap=new HashMap<String,Object>();
        try {
            List<Map<String,Object>> dataList = softwareKindService.queryAllKinds();
            resultMap.put("status", "success");
            resultMap.put("dataList",dataList);
        } catch (Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "获取全部软件种类信息异常!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }

    @RequestMapping(value="/queryKindsList",method=RequestMethod.POST)
    public Object  queryKindsList(@RequestBody DataTableParam[] dataTableParams){
        DataTableModel dataTableModel = new DataTableModel();
        Map<String, String> dataTableMap = DatatableUtil.convertToMap(dataTableParams);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            dataTableModel = softwareKindService.queryKindsList(dataTableMap);
            resultMap.put("status", "success");
            resultMap.put("kindsData", dataTableModel);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询软件种类信息异常!");
        }
        return resultMap;
    }

    @RequestMapping(value="/getKindById",method=RequestMethod.POST)
    public Object getKindById(@RequestParam Map<String, Object> map){
        JSONObject paramObj=AesUtil.GetParam(map);
        String kindId = (String) paramObj.get("kindId");
        Map<String, Object> kindData = new HashMap<String, Object>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            kindData = softwareKindService.getKindById(kindId);
            resultMap.put("status", "success");
            resultMap.put("kindData", kindData);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询某条软件种类信息异常!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }

    @RequestMapping(value="/saveKind",method=RequestMethod.POST)
    public Map<String, Object> saveKind(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        //平台类型
        paramMap.put("kindId", request.getParameter("kindId"));
        paramMap.put("kindName", request.getParameter("kindName"));
        paramMap.put("name_en", request.getParameter("name_en"));
        try {
            if (!softwareKindService.queryKindNameIsRepeat(request.getParameter("kindName")))
            {
                resultMap.put("status", "warn");
                resultMap.put("msg", "软件中文重名!");
            }
            else  if (!softwareKindService.queryKindNameEnIsRepeat(request.getParameter("name_en")))
            {
                resultMap.put("status", "warn");
                resultMap.put("msg", "软件英文重名!");
            }
            else
            {
                softwareKindService.saveKind(paramMap);
                resultMap.put("status", "success");
                resultMap.put("msg", "软件种类保存成功!");
            }
        } catch(Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "软件种类保存失败!");
        }
        return resultMap;
    }

    @RequestMapping(value="/deleteKind",method=RequestMethod.POST)
    public Object deleteKind(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> resultMap = new HashMap<String, Object>();

        String kindId = request.getParameter("kindId");

        try {
            //删除文件和图标
            softwareKindService.deleteIcon(iconPath, kindId);
            softwareKindService.deleteDir(rootPath, kindId);
            boolean flag = softwareKindService.deleteKind(kindId);
            if(flag){
                resultMap.put("status", "success");
                resultMap.put("msg", "删除软件类别成功!");
            }else{
                resultMap.put("status", "error");
                resultMap.put("msg", "删除软件类别失败!");
            }
        } catch(Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "删除软件类别失败!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }
	
	@RequestMapping(value="/queryKindNameIsRepeat",method=RequestMethod.POST)
    public Object  queryKindNameIsRepeat(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> resultMap=new HashMap<String,Object>();
        String kindName = request.getParameter("kindName");
        boolean flag = false;
        try {
            flag = softwareKindService.queryKindNameIsRepeat(kindName);
            resultMap.put("status", "success");
            resultMap.put("flag",flag);
        } catch (Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "获取软件种类名是否重复失败!");
        }
        return resultMap;
    }

    @RequestMapping(value="/queryKindNameEnIsRepeat",method=RequestMethod.POST)
    public Object  queryKindNameEnIsRepeat(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> resultMap=new HashMap<String,Object>();
        String kindNameEn = request.getParameter("kindNameEn");
        boolean flag = false;
        try {
            flag = softwareKindService.queryKindNameEnIsRepeat(kindNameEn);
            resultMap.put("status", "success");
            resultMap.put("flag",flag);
        } catch (Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "获取软件种类英文名是否重复失败!");
        }
        return resultMap;
    }
}
