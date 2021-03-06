package com.nari.software_tool.entity.Communicate;

/**
 * 各类返回类型值定义
 * @author yinyx
 * @version 1.0 2020/3/25
 */
public enum Result{
    //通讯接口数据响应类型
    SOFT_RESP(0,"程序列表"),

    HISTORY_RESP(1,"历史版本列表"),

    PACKET_RESP(2,"程序包信息"),

    USER_RESP(3,"用户信息"),

    SUCCESS(0,"成功"),

    NOT_FIND(1,"未找到"),

    USER_IS_NOT_EXIST(1,"用户不存在"),

    PASSWORD_WRONG(2,"密码错误"),

    ALL_USER(0,"所有用户"),

    EXPERT_VISITOR(1,"除访客外用户"),

    EXPERT_VISITOR_COMMON(2,"除访客和普通用户"),

    EXPERT_ALL_USER(3,"除访客、普通、高级用户");

    private int code;
    private String description;

    Result(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
