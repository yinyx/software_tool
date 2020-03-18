package com.nari.software_tool.service.serviceImpl;

import java.util.Map;
import java.util.UUID;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.nari.software_tool.service.UserService;
import com.nari.software_tool.dao.UserMapper;
import com.nari.software_tool.entity.DataTableModel;
import com.nari.software_tool.entity.Page;

import util.aes.PaginationUtil;

import util.aes.StringUtils;

@Service
@Transactional
public class UserServiceImple implements UserService{
    // 注入用户Mapper
    @Autowired
    private UserMapper userMapper;
    
    public Map<String, Object> findUserByName(String username, String password)
    {
        Map<String, Object> rstMap = new HashMap<String, Object>();
        Map<String, Object> userMap = new HashMap<String, Object>();
        userMap = userMapper.findUserByName(username);
        if ((userMap != null)&&(userMap.get("has_del").equals(0))) {
            if (userMap.get("password").equals(password)) {
                rstMap.put("states", true);
                rstMap.put("userMap",userMap);
                rstMap.put("msg", "登录成功");
            } else {
                rstMap.put("states", false);
                rstMap.put("msg", "密码不正确");
            }
        } else {
            rstMap.put("states", false);
            rstMap.put("msg", "用户不存在");
        }
        return rstMap;
    }
    
    public void saveNewPassWord(Map<String, Object> paramMap) {
        userMapper.saveNewPassWord(paramMap);
    }

    public String findUserPwdById(String id) {
        
        return userMapper.findUserPwdById(id);
    }

    public DataTableModel queryUsersList(Map<String, String> dataTableMap)
    {
        DataTableModel dataTableModel = new DataTableModel();
        Map<String,Object> paramMap = new HashMap<String,Object>();
        String sEcho = dataTableMap.get("sEcho");

        int start = Integer.parseInt(dataTableMap.get("iDisplayStart"));
        int length = Integer.parseInt(dataTableMap.get("iDisplayLength"));

        paramMap.put("start", start);
        paramMap.put("length", length);

        List<Map<String, Object>> resList = userMapper.queryUserList(paramMap);
        Integer count = userMapper.queryUsersCount();

        dataTableModel.setiTotalDisplayRecords(count);
        dataTableModel.setiTotalRecords(count);
        dataTableModel.setsEcho(Integer.valueOf(sEcho));
        dataTableModel.setAaData(resList);

        return dataTableModel;
    }

    public void resetPassword(Map<String, Object> paramMap) {
        userMapper.resetPassword(paramMap);
    }

    public Map<String, Object> getUserById(String userId) {
        return userMapper.getUserById(userId);
    }

    public void saveUser(Map<String, Object> paramMap) {
        String userId = (String) paramMap.get("userId");
        if (StringUtils.isEmpty(userId)) {
            paramMap.put("userId", StringUtils.getUUId());
            userMapper.addUser(paramMap);
        } else {
            userMapper.updateUser(paramMap);
        }
    }

    public boolean deleteUser(String userId) {
        userMapper.deleteUser(userId);
        return true;
    }

    public boolean queryUserNameIsRepeat(String userName) {
        Integer count = userMapper.queryUserNameIsRepeat(userName);
        if (count == 0) {
            return true;
        } else {
            return false;
        }

    }
}

