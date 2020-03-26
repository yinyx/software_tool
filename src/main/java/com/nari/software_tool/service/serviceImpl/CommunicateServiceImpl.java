package com.nari.software_tool.service.serviceImpl;

import com.nari.software_tool.dao.SoftwareInfoMapper;
import com.nari.software_tool.dao.SoftwareKindMapper;
import com.nari.software_tool.entity.SoftwareInfo;
import com.nari.software_tool.service.CommunicateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    SoftwareKindMapper softwareKindMapper;


}
