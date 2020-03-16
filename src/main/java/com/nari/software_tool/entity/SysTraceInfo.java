package com.nari.software_tool.entity;

import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author yinyx
 * @version 1.0 2020/3/16
 */
@Component
public class SysTraceInfo {
    //记录Id
    private int traceId;
    //操作者Id
    private int userId;
    //操作对象
    private int softId;
    //操作内容
    private String operation;
    //操作时间
    private Date dataTime;
    //系统日志
    private String logDetail;

    public int getTraceId() {
        return traceId;
    }

    public void setTraceId(int traceId) {
        this.traceId = traceId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSoftId() {
        return softId;
    }

    public void setSoftId(int softId) {
        this.softId = softId;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Date getDataTime() {
        return dataTime;
    }

    public void setDataTime(Date dataTime) {
        this.dataTime = dataTime;
    }

    public String getLogDetail() {
        return logDetail;
    }

    public void setLogDetail(String logDetail) {
        this.logDetail = logDetail;
    }

    @Override
    public String toString() {
        return "SysTraceInfo{" +
                "traceId=" + traceId +
                ", userId=" + userId +
                ", softId=" + softId +
                ", operation='" + operation + '\'' +
                ", dataTime=" + dataTime +
                ", logDetail='" + logDetail + '\'' +
                '}';
    }
}
