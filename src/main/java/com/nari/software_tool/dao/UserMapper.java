package com.nari.software_tool.dao;
import com.nari.software_tool.entity.UserDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.nari.software_tool.entity.User;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface UserMapper {
    //根据id查询用户
    Map<String, Object> findUserByName(String name);

    public void saveNewPassWord(Map<String, Object> paramMap);

    public String findUserPwdById(String id);

    List<Map<String, Object>> queryUserList(Map<String, Object> paramMap);

    int queryUsersCount();

    void resetPassword(Map<String, Object> paramMap);

    Map<String, Object> getUserById(String userId);

    void addUser(Map<String, Object> paramMap);

    void updateUser(Map<String, Object> paramMap);

    void deleteUser(String userId);

    int queryUserNameIsRepeat(String name);

    boolean checkUserIsExist(String userName, String password);

    UserDetail queryUserDetail(String userName,String password);
}
