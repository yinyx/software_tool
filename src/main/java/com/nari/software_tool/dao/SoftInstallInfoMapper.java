package com.nari.software_tool.dao;

import com.nari.software_tool.entity.SoftInstallInfo;
import com.nari.software_tool.entity.SoftwareInstall;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author yinyx
 * @version 1.0 2020/3/25
 */
@Mapper
@Repository
public interface SoftInstallInfoMapper {

    SoftInstallInfo queryInstallPktById(@Param("Id") String id);
}
