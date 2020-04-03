package com.nari.software_tool.service.serviceImpl;

import com.nari.software_tool.dao.*;
import com.nari.software_tool.entity.Communicate.AppPktPojo;
import com.nari.software_tool.entity.Communicate.CommunicatePojo;
import com.nari.software_tool.entity.Communicate.CommunicateSoftPojo;
import com.nari.software_tool.entity.Communicate.Result;
import com.nari.software_tool.entity.SoftHistoryInfo;
import com.nari.software_tool.entity.SoftInstallInfo;
import com.nari.software_tool.entity.SoftwareInfo;
import com.nari.software_tool.service.CommunicateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用于封装面向客户端接口数据
 * @author yinyx
 * @version 1.0 2020/3/25
 */
@Service
public class CommunicateServiceImpl implements CommunicateService {

    @Autowired
    SoftwareInfoMapper softwareInfoMapper;
    @Autowired
    SoftInstallInfoMapper softInstallInfoMapper;
    @Autowired
    SoftHistoryInfoMapper softHistoryInfoMapper;
    @Autowired
    SoftwareKindMapper softwareKindMapper;


    @Override
    public CommunicatePojo softReqCollect(List<SoftwareInfo> softwareInfoList) {
        CommunicatePojo communicatePojo = new CommunicatePojo();
        communicatePojo.setResp(Result.SOFT_RESP.getCode());
        CommunicateSoftPojo communicateSoftPojo = new CommunicateSoftPojo();
        List<Object> softPojoList = new ArrayList<>();
        for (SoftwareInfo ech:softwareInfoList) {
            //软件对象基础信息
            communicateSoftPojo.setUUID(ech.getSoftId());
            communicateSoftPojo.setName(ech.getName());
            communicateSoftPojo.setVersion(ech.getLatestVersion());
            communicateSoftPojo.setIcon(ech.getIcon());
            communicateSoftPojo.setType(Integer.parseInt(ech.getKind()));
            communicateSoftPojo.setDesc(ech.getBriefIntroduction());

            communicateSoftPojo.setHistory(softHistoryInfoMapper.queryHistoryVersionCount(ech.getSoftId()));
            communicateSoftPojo.setHistoryUrl(softHistoryInfoMapper.queryHistoryVersionUrl(ech.getSoftId()));

            //安装配置对象
            SoftInstallInfo softInstallInfo = softInstallInfoMapper.queryInstallPktById(ech.getId());
            communicateSoftPojo.setSoftwareInstallInfo(softInstallInfo);

            //程序包对象
            SoftHistoryInfo softHistoryInfo = softHistoryInfoMapper.queryHistoryVersion(ech.getSoftId());
            AppPktPojo appPktPojo = new AppPktPojo();
            appPktPojo.setVer(softHistoryInfo.getHistoryVersion());
            appPktPojo.setNew(softHistoryInfo.getAppPktNew());
            appPktPojo.setPath(softHistoryInfo.getHistoryPath());
            appPktPojo.setMD5(softHistoryInfo.getAppPktMd5());
            appPktPojo.setSize(softHistoryInfo.getAppPktSize());
            appPktPojo.setDate(softHistoryInfo.getAppPktDate());
            communicateSoftPojo.setSoftPktInfo(appPktPojo);

            communicateSoftPojo.setScreenShotInfoList(softwareInfoMapper.queryScreenShotListById(ech.getId()));

            softPojoList.add(communicateSoftPojo);
        }
        communicatePojo.setTotal(softPojoList.size());
        return communicatePojo;
    }
}
