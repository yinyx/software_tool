package com.nari.software_tool.service.serviceImpl;

import java.io.File;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.nari.software_tool.service.SoftwareKindService;
import com.nari.software_tool.dao.SoftwareKindMapper;
import com.nari.software_tool.dao.ScreenShotsMapper;
import com.nari.software_tool.dao.SoftwareInstallMapper;
import com.nari.software_tool.dao.SoftHistoryInfoMapper;
import com.nari.software_tool.dao.SoftwareBranchMapper;
import com.nari.software_tool.dao.SoftwareInfoMapper;
import com.nari.software_tool.dao.SoftPluginMapper;
import com.nari.software_tool.entity.DataTableModel;

import util.aes.StringUtils;
import util.aes.TestProperties;

@Service
@Transactional(rollbackFor = { Exception.class })
public class SoftwareKindServiceImple implements SoftwareKindService{
    @Autowired
    private SoftPluginMapper softPluginMapper;

    @Autowired
    private SoftwareKindMapper softwareKindMapper;

    @Autowired
    private ScreenShotsMapper screenShotsMapper;

    @Autowired
    private SoftwareInstallMapper softwareInstallMapper;

    @Autowired
    private SoftHistoryInfoMapper softHistoryInfoMapper;

    @Autowired
    private SoftwareBranchMapper softwareBranchMapper;

    @Autowired
    private SoftwareInfoMapper softwareInfoMapper;

    @Override
    public List<Map<String,Object>> queryAllKinds()
    {
        List<Map<String, Object>> allKinds = softwareKindMapper.queryAllKinds();
        return allKinds;
    }

    @Override
    public DataTableModel queryKindsList(Map<String, String> dataTableMap)
    {
        DataTableModel dataTableModel = new DataTableModel();
        Map<String,Object> paramMap = new HashMap<String,Object>();
        String sEcho = dataTableMap.get("sEcho");
        String KindName = dataTableMap.get("KindName");

        int start = Integer.parseInt(dataTableMap.get("iDisplayStart"));
        int length = Integer.parseInt(dataTableMap.get("iDisplayLength"));

        paramMap.put("start", start);
        paramMap.put("length", length);
        paramMap.put("KindName", KindName);

        List<Map<String, Object>> resList = softwareKindMapper.queryKindList(paramMap);
        Integer count = softwareKindMapper.queryKindsCount(paramMap);

        dataTableModel.setiTotalDisplayRecords(count);
        dataTableModel.setiTotalRecords(count);
        dataTableModel.setsEcho(Integer.valueOf(sEcho));
        dataTableModel.setAaData(resList);

        return dataTableModel;
    }

    @Override
    public Map<String, Object> getKindById(String kindId) {
        return softwareKindMapper.getKindById(kindId);
    }

    @Override
    public void saveKind(Map<String, Object> paramMap) {
        String kindId = (String) paramMap.get("kindId");
        if (StringUtils.isEmpty(kindId)) {
            paramMap.put("kindId", StringUtils.getUUId());
            softwareKindMapper.addKind(paramMap);
        } else {
            softwareKindMapper.updateKind(paramMap);
        }
    }

    @Override
    public void deleteIcon(String iconPath, String KindId){
        Map<String, Object> kindObj =  softwareKindMapper.getKindById(KindId);
        String kindName = (String) kindObj.get("name_en");

        String oldIconPath = iconPath+"/"+kindName;
        File oldIconPathDir = new File(oldIconPath);
        String oldIconDirAbsolutePath = oldIconPathDir.getAbsolutePath();
        TestProperties.deleteFile(oldIconDirAbsolutePath);
    }

    @Override
    public void deleteDir(String rootPath, String KindId)
    {
        Map<String,Object> map = softwareKindMapper.getKindById(KindId);
        if (map == null)
        {
            return;
        }
        String softPath = rootPath+"/"+map.get("name_en");
        File softDir = new File(softPath);
        String softDirAbsolutePath = softDir.getAbsolutePath();

        TestProperties.deleteFile(softDirAbsolutePath);
    }

    @Override
    public boolean deleteKind(String kindId) {
        softwareKindMapper.deleteKind(kindId);
        //删除软件截图
        screenShotsMapper.deleteScreenShotsByKind(kindId);
        //删除软件安装配置
        softwareInstallMapper.deleteInstallsByKind(kindId);
        //删除软件插件
        softPluginMapper.deletePluginsByKind(kindId);
        //删除软件版本
        softHistoryInfoMapper.deleteVersionsByKind(kindId);
        //删除软件分支
        softwareBranchMapper.deleteBranchsByKind(kindId);
        //删除软件
        softwareInfoMapper.deleteSoftwaresByKind(kindId);
        //删除文件，包括安装文件，图标文件，截图文件
        return true;
    }
	
	@Override
    public boolean queryKindNameIsRepeat(String KindName) {
        Integer count = softwareKindMapper.queryKindNameIsRepeat(KindName);
        if (count == 0) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public boolean queryKindNameEnIsRepeat(String KindName) {
        Integer count = softwareKindMapper.queryKindNameEnIsRepeat(KindName);
        if (count == 0) {
            return true;
        } else {
            return false;
        }

    }
}

