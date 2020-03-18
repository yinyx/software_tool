package com.nari.software_tool.dao;

import com.nari.software_tool.entity.SysRoleInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yinyx
 * @version 1.0 2020/3/16
 */
@Mapper
@Repository
public interface SysRoleInfoMapper {

    /**
     * 获取列表
     * @return
     */
    List<SysRoleInfo> querySysRoleInfoList();

    /**
     * 更新
     * @param sysRoleInfo
     * @return
     */
    int updateSysRoleInfo(SysRoleInfo sysRoleInfo);

    /**
     * 录入
     * @param sysRoleInfo
     * @return
     */
    int insertSysRoleInfo(SysRoleInfo sysRoleInfo);

    /**
     * 删除
     * @param roleId
     * @return
     */
    int deleteSysRole(@Param("roleId") int roleId);


}
