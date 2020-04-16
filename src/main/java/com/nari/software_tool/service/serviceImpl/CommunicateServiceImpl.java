package com.nari.software_tool.service.serviceImpl;

import com.nari.software_tool.dao.*;
import com.nari.software_tool.entity.*;
import com.nari.software_tool.entity.Communicate.*;
import com.nari.software_tool.service.CommunicateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    @Autowired
    UserMapper userMapper;



    @Override
    public CommunicatePojo softReqCollect(List<SoftwareInfo> softwareInfoList, List<SoftPluginInfo> softPluginInfoList) {
        CommunicatePojo communicatePojo = new CommunicatePojo();
        communicatePojo.setRESP(Result.SOFT_RESP.getCode());
        List<Object> softPojoList = new ArrayList<>();
        for (SoftwareInfo ech:softwareInfoList) {
            CommunicateSoftPojo communicateSoftPojo = new CommunicateSoftPojo();
            //软件对象基础信息
            communicateSoftPojo.setUUID(ech.getSoftId());
            communicateSoftPojo.setName(ech.getName());
            communicateSoftPojo.setVersion(ech.getLatestVersion());
            communicateSoftPojo.setIcon(ech.getIcon());
            communicateSoftPojo.setType(ech.getKind());
            communicateSoftPojo.setDesc(ech.getBriefIntroduction());

            communicateSoftPojo.setHistory(softHistoryInfoMapper.queryHistoryVersionCount(ech.getId()));
            communicateSoftPojo.setHistoryUrl("software_tool/communicate/getHistoryList?"+"UUID="+ech.getSoftId());

            //安装配置对象
            SoftInstallInfo softInstallInfo = softInstallInfoMapper.queryInstallPktById(ech.getId());
            communicateSoftPojo.setInstall(softInstallInfo);

            //程序包对象
            SoftHistoryInfo softHistoryInfo = softHistoryInfoMapper.queryHistoryVersion(ech.getId());
            if(softHistoryInfo!= null) {
                AppPktPojo appPktPojo = new AppPktPojo();
                appPktPojo.setVer(softHistoryInfo.getHistoryVersion());
                appPktPojo.setNew(softHistoryInfo.getAppPktNew());
                appPktPojo.setPath("software_tool/communicate/downloadPkt?"+"fileName="+ech.getName()+"&softwareUrl="+softHistoryInfo.getAppPktPath());
                appPktPojo.setMD5(softHistoryInfo.getAppPktMd5());
                appPktPojo.setSize(softHistoryInfo.getAppPktSize());
                appPktPojo.setDate(softHistoryInfo.getAppPktDate());
                communicateSoftPojo.setAppPkt(appPktPojo);
            }else{
                AppPktPojo appPktPojo = new AppPktPojo();
                communicateSoftPojo.setAppPkt(appPktPojo);
            }
            communicateSoftPojo.setScreenShots(softwareInfoMapper.queryScreenShotListById(ech.getId()));
            softPojoList.add(communicateSoftPojo);
        }
        for (SoftPluginInfo pch:softPluginInfoList) {
            CommunicateSoftPojo communicateSoftPojo = new CommunicateSoftPojo();
            //安装配置对象
            SoftInstallInfo softInstallInfo = new SoftInstallInfo();
            softInstallInfo.setType(3);
            softInstallInfo.setPluginDir(pch.getAbsolutePath());
            softInstallInfo.setExecPath(pch.getRelativePath());
            softInstallInfo.setHostAppId(pch.getSoftId());
            communicateSoftPojo.setInstall(softInstallInfo);
            softPojoList.add(communicateSoftPojo);
        }
        communicatePojo.setTOTAL(softPojoList.size());
        communicatePojo.setApplications(softPojoList);
        return communicatePojo;
    }


    @Override
    public CommunicatePojo hisReqCollect(List<SoftHistoryInfo> softHistoryInfoList) {
        CommunicatePojo communicatePojo = new CommunicatePojo();
        communicatePojo.setRESP(Result.HISTORY_RESP.getCode());
        List<Object> hisPojoList = new ArrayList<>();
        for (SoftHistoryInfo ech:softHistoryInfoList){
            CommunicateHistoryPojo communicateHistoryPojo = new CommunicateHistoryPojo();
            if(ech!= null) {
                AppPktPojo appPktPojo = new AppPktPojo();
                appPktPojo.setVer(ech.getHistoryVersion());
                appPktPojo.setNew(ech.getAppPktNew());
                appPktPojo.setPath(ech.getHistoryPath());
                appPktPojo.setMD5(ech.getAppPktMd5());
                appPktPojo.setSize(ech.getAppPktSize());
                appPktPojo.setDate(ech.getAppPktDate());
                communicateHistoryPojo.setAppPkt(appPktPojo);
            }else{
                AppPktPojo appPktPojo = new AppPktPojo();
                communicateHistoryPojo.setAppPkt(appPktPojo);
            }
            hisPojoList.add(communicateHistoryPojo);
        }
        communicatePojo.setTOTAL(hisPojoList.size());
        communicatePojo.setApplications(hisPojoList);
        return communicatePojo;
    }

    @Override
    public CommunicatePacketPojo pktReqCollect(SoftHistoryInfo softHistoryInfo) {
        CommunicatePacketPojo communicatePacketPojo = new CommunicatePacketPojo();
        communicatePacketPojo.setRESP(Result.PACKET_RESP.getCode());
        if(softHistoryInfo != null){
            communicatePacketPojo.setRslt(Result.SUCCESS.getCode());
            AppPktPojo appPktPojo = new AppPktPojo();
            appPktPojo.setVer(softHistoryInfo.getHistoryVersion());
            appPktPojo.setDate(softHistoryInfo.getAppPktDate());
            appPktPojo.setNew(softHistoryInfo.getAppPktNew());
            appPktPojo.setSize(softHistoryInfo.getAppPktSize());
            appPktPojo.setMD5(softHistoryInfo.getAppPktMd5());
            appPktPojo.setPath(softHistoryInfo.getHistoryPath());
            communicatePacketPojo.setAppPkt(appPktPojo);
            communicatePacketPojo.setUUID(softHistoryInfo.getHistoryId());
        }else{
            communicatePacketPojo.setRslt(Result.NOT_FIND.getCode());
        }
        return communicatePacketPojo;
    }

    @Override
    public UserPojo userReqCollect(String userName, String password) {
        UserPojo userPojo = new UserPojo();
        userPojo.setResp(Result.USER_RESP.getCode());
        if(userMapper.checkUserIsExist(userName,password)){
            UserDetail userDetail = userMapper.queryUserDetail(userName,password);
            userPojo.setType(userDetail.getRole());
            userPojo.setRslt(Result.SUCCESS.getCode());
        }
        if(!userMapper.checkUserIsExist(userName, password)){
            try{
            Map<String,Object> userMap = userMapper.findUserByName(userName);
                if(userMap.get("password") != password){
                userPojo.setRslt(Result.PASSWORD_WRONG.getCode());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if(!userMapper.checkUserIsExist(userName,password)) {
            Map<String,Object> userMap = userMapper.findUserByName(userName);
            if(userMap == null){
                userPojo.setRslt(Result.USER_IS_NOT_EXIST.getCode());
            }
        }
        return userPojo;
    }
}
