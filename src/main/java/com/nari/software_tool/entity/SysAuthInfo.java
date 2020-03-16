package com.nari.software_tool.entity;

import org.springframework.stereotype.Component;

/**
 * @author yinyx
 * @version 1.0 2020/3/16
 */
@Component
public class SysAuthInfo {
    //用户Id
    private int userId;
    //角色Id
    private int roleId;
    //用户名
    private String userName;
    //软件权限
    private String adminRights;
    //已上传产品
    private String subSoftware;
    //企业（部门）
    private String enterprise;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAdminRights() {
        return adminRights;
    }

    public void setAdminRights(String adminRights) {
        this.adminRights = adminRights;
    }

    public String getSubSoftware() {
        return subSoftware;
    }

    public void setSubSoftware(String subSoftware) {
        this.subSoftware = subSoftware;
    }

    public String getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(String enterprise) {
        this.enterprise = enterprise;
    }

    @Override
    public String toString() {
        return "SysAuthInfo{" +
                "userId=" + userId +
                ", roleId=" + roleId +
                ", userName='" + userName + '\'' +
                ", adminRights='" + adminRights + '\'' +
                ", subSoftware='" + subSoftware + '\'' +
                ", enterprise='" + enterprise + '\'' +
                '}';
    }
}
