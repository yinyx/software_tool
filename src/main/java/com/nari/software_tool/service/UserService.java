package com.nari.software_tool.service;

import java.util.Map;
import java.util.List;

import com.nari.software_tool.entity.DataTableModel;

public interface UserService {
    Map<String, Object> findUserByName(String username, String password);

    String findUserPwdById(String id);
    
    void saveNewPassWord(Map<String, Object> paramMap);

    DataTableModel queryUsersList(Map<String, String> dataTableMap);

    void resetPassword(Map<String, Object> paramMap);

    Map<String, Object> getUserById(String userId);

    void saveUser(Map<String, Object> paramMap);

    boolean deleteUser(String userId);

    boolean queryUserNameIsRepeat(String userName);
}