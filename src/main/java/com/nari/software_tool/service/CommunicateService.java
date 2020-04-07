package com.nari.software_tool.service;

import com.nari.software_tool.entity.Communicate.CommunicatePojo;
import com.nari.software_tool.entity.Communicate.UserPojo;
import com.nari.software_tool.entity.SoftHistoryInfo;
import com.nari.software_tool.entity.SoftInstallInfo;
import com.nari.software_tool.entity.SoftwareInfo;
import com.nari.software_tool.entity.User;

import java.util.List;

/**
 * 用于封装面向客户端接口数据
 * @author yinyx
 * @version 1.0 2020/3/25
 */
public interface CommunicateService {
    /**
     * 封装客户端请求软件列表
     * @param softwareInfoList
     * @return
     */
    CommunicatePojo softReqCollect(List<SoftwareInfo> softwareInfoList);

    /**
     * 封装客户端请求历史版本列表
     * @param softHistoryInfoList
     * @return
     */
    CommunicatePojo hisReqCollect(List<SoftHistoryInfo> softHistoryInfoList);

    /**
     * 封装客户端请求用户校验
     * @param userName
     * @param password
     * @return
     */
    UserPojo userReqCollect(String userName, String password);

}
