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

import com.nari.software_tool.service.BranchService;
import com.nari.software_tool.entity.DataTableModel;
import com.nari.software_tool.entity.DataTableParam;

import net.sf.json.JSONObject;
import util.aes.AesUtil;
import util.aes.DatatableUtil;
import util.aes.StringUtils;

@RestController
@RequestMapping(value = "/branch")
public class BranchController {
    @Value("${rootPath}")
    private String rootPath;
	
	@Value("${pluginPath}")
    private String pluginPath;

    @Resource
    private BranchService branchService;

    @RequestMapping(value="/queryBranchsList",method=RequestMethod.POST)
    public Object  queryBranchsList(@RequestBody DataTableParam[] dataTableParams){
        DataTableModel dataTableModel = new DataTableModel();
        Map<String, String> dataTableMap = DatatableUtil.convertToMap(dataTableParams);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            dataTableModel = branchService.queryBranchsList(dataTableMap);
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
	
	@RequestMapping(value="/saveBranch",method=RequestMethod.POST)
    public Map<String, Object> saveBranch(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        //平台类型
		paramMap.put("branchId", request.getParameter("branchId"));
        paramMap.put("softwareId", request.getParameter("softwareId"));
        paramMap.put("branchName", request.getParameter("branchName"));
        paramMap.put("branchDescription", request.getParameter("branchDescription"));
		paramMap.put("userId", request.getParameter("userId"));

        Map<String, Object> cheackNameMap = new HashMap<String, Object>();

        cheackNameMap.put("branchName", request.getParameter("branchName"));
        cheackNameMap.put("softwareId", request.getParameter("softwareId"));

        String branchId = (String) paramMap.get("branchId");

        try {

            if ((StringUtils.isEmpty(branchId))&&(!branchService.queryBranchNameIsRepeat(cheackNameMap)))
            {
                resultMap.put("status", "warn");
                resultMap.put("msg", "软件分支重名!");
            }
            else
            {
                branchService.saveBranch(paramMap);
                resultMap.put("status", "success");
                resultMap.put("msg", "软件分支保存成功!");
            }
        } catch(Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "软件分支保存失败!");
        }
        return resultMap;
    }
	
	@RequestMapping(value="/getBranchById",method=RequestMethod.POST)
    public Object getBranchById(@RequestParam Map<String, Object> map){
        JSONObject paramObj=AesUtil.GetParam(map);
        String branchId = (String) paramObj.get("branchId");
        Map<String, Object> branchData = new HashMap<String, Object>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            branchData = branchService.getBranchById(branchId);
            resultMap.put("status", "success");
            resultMap.put("branchData", branchData);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询某条软件分支信息异常!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }

    @RequestMapping(value="/checkBranchName",method=RequestMethod.POST)
    public Object checkBranchName(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> resultMap = new HashMap<String, Object>();

        String branchId = request.getParameter("branchId");
        Map<String, Object> branchMap = new HashMap<String, Object>();
        branchMap = branchService.getBranchById(branchId);
        String branchName = (String)branchMap.get("branch");
        String masterStr = "master";
        if (masterStr.equals(branchName))
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "不可删除软件的默认分支");
        }
        else
        {
            resultMap.put("status", "success");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }

	@RequestMapping(value="/deleteBranch",method=RequestMethod.POST)
    public Object deleteBranch(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> resultMap = new HashMap<String, Object>();

        String branchId = request.getParameter("branchId");
        Map<String, Object> branchMap = new HashMap<String, Object>();
        branchMap = branchService.getBranchById(branchId);
        String branchName = (String)branchMap.get("branch");
        String masterStr = "master";
        if (masterStr.equals(branchName))
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "不可删除软件的默认分支");
        }
        else
        {
            try {
                branchService.deleteDir(rootPath, branchId);
                branchService.deleteDir(pluginPath, branchId);
                boolean flag = branchService.deleteBranch(branchId);
                if(flag){
                    resultMap.put("status", "success");
                    resultMap.put("msg", "删除分支成功!");
                }else{
                    resultMap.put("status", "error");
                    resultMap.put("msg", "删除分支失败!");
                }
            } catch(Exception e) {
                resultMap.put("status", "error");
                resultMap.put("msg", "删除分支失败!");
            }
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }
	
	@RequestMapping(value="/queryBranchNameIsRepeat",method=RequestMethod.POST)
    public Object  queryBranchNameIsRepeat(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> resultMap=new HashMap<String,Object>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
				
		paramMap.put("branchName", request.getParameter("branchName"));
        paramMap.put("softwareId", request.getParameter("softwareId"));
        boolean flag = false;
        try {
            flag = branchService.queryBranchNameIsRepeat(paramMap);
            resultMap.put("status", "success");
            resultMap.put("flag",flag);
        } catch (Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "获取软件分支名是否重复失败!");
        }
        return resultMap;
    }
}
