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

import com.nari.software_tool.service.UserService;
import com.nari.software_tool.entity.DataTableModel;
import com.nari.software_tool.entity.DataTableParam;

import net.sf.json.JSONObject;
import util.aes.AesUtil;
import util.aes.DatatableUtil;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    // 注入用户Service
    @Resource
    private UserService userService;

    @RequestMapping(value="/login",method=RequestMethod.POST)
    public String findUserByName(@RequestParam Map<String, Object> map,HttpSession session) {
        JSONObject paramObj=AesUtil.GetParam(map);
        String username = (String) paramObj.get("username");
        String password = (String) paramObj.get("password");
        Map<String, Object> userMap = userService.findUserByName(username,password);

        JSONObject jsonObject = JSONObject.fromObject(userMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }

    @RequestMapping(value="/saveNewPassWord",method=RequestMethod.POST)
    public String saveNewPassWord(@RequestParam Map<String, Object> map){
        JSONObject paramObj=AesUtil.GetParam(map);
        JSONObject obj = new JSONObject();
        //Map<String, Object> resultMap = new HashMap<String, Object>();
        String id = (String) paramObj.get("id");
        String oldPassword = (String) paramObj.get("oldPassword");
        String newPassword = (String) paramObj.get("newPassword");
        System.out.println("oldPassword");
        System.out.println(oldPassword);
        System.out.println("newPassword");
        System.out.println(newPassword);
        try{
            if(userService.findUserPwdById(id).equals(oldPassword)){
                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("id", id);              
                paramMap.put("newPassword", newPassword);
                userService.saveNewPassWord(paramMap);
                //resultMap.put("status", "success");
                //resultMap.put("msg", "密码保存成功!");
                obj.put("result", "success");
                obj.put("reason", "密码保存成功!");
                //obj.put("resultMap", resultMap);
            } else{
                obj.put("result", "failure");
                obj.put("reason", "原密码错误");
            }
        } catch (Exception e) {
            obj.put("result", "failure");
            obj.put("reason", "修改密码失败");
        }
        JSONObject jsonObject = JSONObject.fromObject(obj);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }

    @RequestMapping(value="/queryUsersList",method=RequestMethod.POST)
    public Object  queryUsersList(@RequestBody DataTableParam[] dataTableParams){
        DataTableModel dataTableModel = new DataTableModel();
        Map<String, String> dataTableMap = DatatableUtil.convertToMap(dataTableParams);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            dataTableModel = userService.queryUsersList(dataTableMap);
            resultMap.put("status", "success");
            resultMap.put("usersData", dataTableModel);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询用户信息异常!");
        }
        return resultMap;
    }

    @RequestMapping(value="/resetPassword",method=RequestMethod.POST)
    public String resetPassword(@RequestParam Map<String, Object> map){
        JSONObject paramObj=AesUtil.GetParam(map);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("id", paramObj.get("id"));
        try {
            userService.resetPassword(paramMap);
            resultMap.put("status", "success");
            resultMap.put("msg", "重置密码成功!");
        } catch(Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "重置密码失败!");
        }

        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }

    @RequestMapping(value="/getUserById",method=RequestMethod.POST)
    public Object getUserById(@RequestParam Map<String, Object> map){
        JSONObject paramObj=AesUtil.GetParam(map);
        String userId = (String) paramObj.get("userId");
        Map<String, Object> usersData = new HashMap<String, Object>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            usersData = userService.getUserById(userId);
            resultMap.put("status", "success");
            resultMap.put("usersData", usersData);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询某条信息异常!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }

    @RequestMapping(value="/saveUser",method=RequestMethod.POST)
    public Map<String, Object> saveUser(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        //平台类型
        paramMap.put("userId", request.getParameter("userId"));
        paramMap.put("userName", request.getParameter("realName"));
        paramMap.put("phone", request.getParameter("userName"));
        paramMap.put("role", request.getParameter("role"));
        paramMap.put("status", request.getParameter("states"));
        paramMap.put("authority", request.getParameter("authority"));
        try {
            userService.saveUser(paramMap);
            resultMap.put("status", "success");
            resultMap.put("msg", "用户保存成功!");
        } catch(Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "用户保存失败!");
        }
        return resultMap;
    }

    @RequestMapping(value="/deleteUser",method=RequestMethod.POST)
    public Object deleteUser(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> resultMap = new HashMap<String, Object>();

        String userId = request.getParameter("userId");

        try {
            boolean flag = userService.deleteUser(userId);
            if(flag){
                resultMap.put("status", "success");
                resultMap.put("msg", "删除成功!");
            }else{
                resultMap.put("status", "error");
                resultMap.put("msg", "删除失败,该用户已被使用!");
            }
        } catch(Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "删除失败!");
        }
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }

    @RequestMapping(value="/queryUserNameIsRepeat",method=RequestMethod.POST)
    public Object  queryUserNameIsRepeat(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> resultMap=new HashMap<String,Object>();
        String userName = request.getParameter("userName");
        boolean flag = false;
        try {
            flag = userService.queryUserNameIsRepeat(userName);
            resultMap.put("status", "success");
            resultMap.put("flag",flag);
        } catch (Exception e) {
            resultMap.put("status", "error");
            resultMap.put("msg", "获取用户名是否重复失败!");
        }
        return resultMap;
    }
}
