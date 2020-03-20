package com.nari.software_tool.service;

import com.nari.software_tool.entity.SoftwareInfo;

import java.util.List;

/**
 * @author yinyx
 * @version 1.0 2020/3/14
 */
public interface FileService {



    boolean insertSoft(String iconName,SoftwareInfo softwareInfo);

    boolean updateSoft(SoftwareInfo softwareInfo);
}
