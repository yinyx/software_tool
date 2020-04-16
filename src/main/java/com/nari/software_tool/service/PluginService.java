package com.nari.software_tool.service;

import com.nari.software_tool.entity.DataTableModel;
import com.nari.software_tool.entity.SoftPluginInfo;

import java.util.List;
import java.util.Map;

/**
 * @author yinyx
 * @version 1.0 2020/4/10
 */
public interface PluginService {

    DataTableModel queryPluginList(Map<String, String> dataTableMap);

    List<SoftPluginInfo> getPluginList();

    int addPlugin(Map<String,Object> paramMap);

    Map<String,Object> getPluginById(String pluginId);

    int updatePlugin(Map<String,Object> paramMap);
}
