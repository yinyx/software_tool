package com.nari.software_tool.entity;

import java.util.Date;

/**
 * @author yinyx
 * @version 1.0 2020/4/7
 */
public class UserDetail {

    private String Id;

    private String phone;

    private String userName;

    private String password;

    private Date createTime;

    private String createBy;

    private Date updateTime;

    private String updateBy;

    private int hasDel;

    private int role;

    private int operateAuth;

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public int getHasDel() {
        return hasDel;
    }

    public void setHasDel(int hasDel) {
        this.hasDel = hasDel;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getOperateAuth() {
        return operateAuth;
    }

    public void setOperateAuth(int operateAuth) {
        this.operateAuth = operateAuth;
    }

    @Override
    public String toString() {
        return "UserDetail{" +
                "Id='" + Id + '\'' +
                ", phone='" + phone + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", createTime=" + createTime +
                ", createBy='" + createBy + '\'' +
                ", updateTime=" + updateTime +
                ", updateBy='" + updateBy + '\'' +
                ", hasDel=" + hasDel +
                ", role=" + role +
                ", operateAuth=" + operateAuth +
                '}';
    }
}
