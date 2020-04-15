package com.nari.software_tool.dao;

import org.apache.ibatis.annotations.Mapper;
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

    List<Map<String, Object>> queryPluginList(Map<String,Object> paramMap);

    int queryPluginCount(Map<String, Object> paramMap);

    int addPlugin(Map<String,Object> paramMap);

    Map<String,Object> queryPluginById(String pluginId);

    int updatePlugin(Map<String,Object> paramMap);

    void deletePluginsByKind(String kindId);
}
