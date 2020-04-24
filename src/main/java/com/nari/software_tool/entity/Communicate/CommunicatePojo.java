package com.nari.software_tool.entity.Communicate;


import java.util.List;

/**
 * 客户端通信基础类
 * @author yinyx
 * @version 1.0 2020/3/25
 */
public class CommunicatePojo {

    private String UUID;
    //响应数据类型
    private int  RESP;
    //列表元素数量
    private int TOTAL;
    //对象数组
    private List<Object> Applications;

    public int getRESP() {
        return RESP;
    }

    public void setRESP(int RESP) {
        this.RESP = RESP;
    }

    public int getTOTAL() {
        return TOTAL;
    }

    public void setTOTAL(int TOTAL) {
        this.TOTAL = TOTAL;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public List<Object> getApplications() {
        return Applications;
    }

    public void setApplications(List<Object> applications) {
        Applications = applications;
    }

    @Override
    public String toString() {
        return "CommunicatePojo{" +
                "UUID='" + UUID + '\'' +
                ", RESP=" + RESP +
                ", TOTAL=" + TOTAL +
                ", Applications=" + Applications +
                '}';
    }
}
