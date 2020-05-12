package com.nari.software_tool.dao;

import com.nari.software_tool.entity.SoftPluginInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author yinyx
 * @version 1.0 2020/3/25
 */
@Mapper
@Repository
public interface SoftPluginMapper {

    List<SoftPluginInfo> getPluginList();

    List<Map<String, Object>> queryPluginList(Map<String,Object> paramMap);

    int queryPluginCount(Map<String, Object> paramMap);

    int addPlugin(Map<String,Object> paramMap);

    Map<String,Object> queryPluginById(String pluginId);

    int updatePlugin(Map<String,Object> paramMap);

    void deletePluginsByKind(String kindId);

    void deletePluginBySoftwareId(String softwareId);

    void deletePluginsByBranchId(String branchId);

    Map<String, Object> getPluginPkgCfgById(@Param("id") String pluginId);

    void setPluginInstllconfig(Map<String,Object> paramMap);

    int updatePluginIcon(String icon,String pluginId);
}
