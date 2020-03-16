package com.nari.software_tool.entity;

import org.springframework.stereotype.Component;

/**
 * @author yinyx
 * @version 1.0 2020/3/16
 */
@Component
public class SysRoleInfo {
    //角色ID
    private int roleId;
    //角色描述
    private String roleDescription;
    //下属权限
    private String auth;
    //权限描述
    private String authDescription;

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getAuthDescription() {
        return authDescription;
    }

    public void setAuthDescription(String authDescription) {
        this.authDescription = authDescription;
    }

    @Override
    public String toString() {
        return "SysRoleInfo{" +
                "roleId=" + roleId +
                ", roleDescription='" + roleDescription + '\'' +
                ", auth='" + auth + '\'' +
                ", authDescription='" + authDescription + '\'' +
                '}';
    }
}
