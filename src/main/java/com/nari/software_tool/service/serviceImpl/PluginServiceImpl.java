package com.nari.software_tool.service.serviceImpl;

import com.nari.software_tool.dao.SoftPluginMapper;
import com.nari.software_tool.entity.DataTableModel;
import com.nari.software_tool.entity.SoftPluginInfo;
import com.nari.software_tool.service.PluginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yinyx
 * @version 1.0 2020/4/10
 */
@Service
public class PluginServiceImpl implements PluginService {

    @Autowired
    SoftPluginMapper softPluginMapper;


    @Override
    public DataTableModel queryPluginList(Map<String, String> dataTableMap) {
        DataTableModel<Map<String, Object>> dataTableModel = new DataTableModel<>();
        Map<String,Object> paramMap = new HashMap<>();
        String sEcho = dataTableMap.get("sEcho");
        int start = Integer.parseInt(dataTableMap.get("iDisplayStart"));
        int length = Integer.parseInt(dataTableMap.get("iDisplayLength"));
        String type = dataTableMap.get("Kind");
        String softId = dataTableMap.get("softId");
        String branch = dataTableMap.get("branchId");
        String version = dataTableMap.get("versionId");
        String pluginName = dataTableMap.get("pluginName");

        paramMap.put("start", start);
        paramMap.put("length", length);
        paramMap.put("type",type);
        paramMap.put("softId",softId);
        paramMap.put("branch",branch);
        paramMap.put("version",version);
        paramMap.put("pluginName",pluginName);

        List<Map<String, Object>> resList = softPluginMapper.queryPluginList(paramMap);
        for (Map<String,Object> map: resList) {
            String iconStr = (String) map.get("icon");
            String icon = iconStr.replace("/home/nari/softwareTool/softIcon/","");
            map.put("icon",icon);
        }
        int count = softPluginMapper.queryPluginCount(paramMap);
        dataTableModel.setiTotalDisplayRecords(count);
        dataTableModel.setiTotalRecords(count);
        dataTableModel.setsEcho(Integer.valueOf(sEcho));
        dataTableModel.setAaData(resList);
        return dataTableModel;
    }

    @Override
    public List<SoftPluginInfo> getPluginList() {
        return softPluginMapper.getPluginList();
    }

    @Override
    public int addPlugin(Map<String, Object> paramMap) {
        return softPluginMapper.addPlugin(paramMap);
    }

    @Override
    public Map<String, Object> getPluginById(String pluginId) {
        return softPluginMapper.queryPluginById(pluginId);
    }

    @Override
    public int updatePlugin(Map<String, Object> paramMap) {
        return softPluginMapper.updatePlugin(paramMap);
    }

    @Override
    public Map<String, Object> getPluginPkgCfgById(String pluginId) {
        Map<String, Object> obj = softPluginMapper.getPluginPkgCfgById(pluginId);
        return obj;
    }

    @Override
    public void setPluginInstllconfigById(Map<String, Object> paramMap){
        softPluginMapper.setPluginInstllconfig(paramMap);
    }

    @Override
    public int updatePluginIcon(String icon, String pluginId) {
        return softPluginMapper.updatePluginIcon(icon,pluginId);
    }
}
