package com.nari.software_tool.service.serviceImpl;

import com.nari.software_tool.dao.SoftwareInfoMapper;
import com.nari.software_tool.entity.SoftwareInfo;
import com.nari.software_tool.service.FileService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.logging.Logger;

/**
 * @author yinyx
 * @version 1.0 2020/3/14
 */
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private SoftwareInfoMapper softwareInfoMapper;


    @Override
    public boolean insertSoft(String iconName ,SoftwareInfo softwareInfo) {
        String uuid = UUID.randomUUID().toString();
        softwareInfo.setSoftId(uuid);
        softwareInfo.setIcon(iconName);
        if(softwareInfoMapper.insertSoftwareInfo(softwareInfo) == 1){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean updateSoft(SoftwareInfo softwareInfo) {
        if(softwareInfoMapper.updateSoftwareInfo(softwareInfo) ==1){
            return true;
        }else{
            return false;
        }
    }
}
