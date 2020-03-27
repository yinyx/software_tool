package com.nari.software_tool.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

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

@RestController
@RequestMapping(value = "/branch")
public class BranchController {

    // 注入软件类别Service
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
        paramMap.put("softwareId", request.getParameter("softwareId"));
        paramMap.put("branchName", request.getParameter("branchName"));
        paramMap.put("branchDescription", request.getParameter("branchDescription"));
		paramMap.put("userId", request.getParameter("userId"));
		
        try {
            branchService.saveBranch(paramMap);
            resultMap.put("status", "success");
            resultMap.put("msg", "软件分支保存成功!");
        } catch(Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "软件分支保存失败!");
        }
        return resultMap;
    }
}
