package com.nari.software_tool.dao;

import com.nari.software_tool.entity.SysRoleInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author yinyx
 * @version 1.0 2020/3/16
 */
@Mapper
@Repository
public interface SysRoleInfoMapper {

    SysRoleInfo querySysRoleInfo();

    int updateSysRoleInfo(SysRoleInfo sysRoleInfo);

    int insertSysRoleInfo(SysRoleInfo sysRoleInfo);

    int deleteSysRole(@Param("roleId") int roleId);


}
