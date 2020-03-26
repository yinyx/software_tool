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

    PACKET_RESP(2,"程序包信息");

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
