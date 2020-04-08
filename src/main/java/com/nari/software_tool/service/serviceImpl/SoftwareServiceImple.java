package com.nari.software_tool.service.serviceImpl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.io.File;

import com.nari.software_tool.dao.*;
import com.nari.software_tool.entity.ScreenShotInfo;
import com.nari.software_tool.entity.SoftwareInfo;
import com.nari.software_tool.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.nari.software_tool.service.SoftwareService;
import com.nari.software_tool.entity.DataTableModel;

import util.aes.StringUtils;
import util.aes.TestProperties;

@Service
@Transactional(rollbackFor = { Exception.class })
public class SoftwareServiceImple implements SoftwareService{
    // 注入软件类别Mapper
    @Autowired
    private SoftwareInfoMapper softwareInfoMapper;
	
	@Autowired
	private SoftwareKindMapper softwareKindMapper;
	
    @Autowired
	private SoftwareInstallMapper softwareInstallMapper;

    @Autowired
	private ScreenShotsMapper screenShotsMapper;

    @Autowired
    private SoftwareBranchMapper softwareBranchMapper;

    @Autowired
    private SoftHistoryInfoMapper softHistoryInfoMapper;

    @Autowired
	private UserService userService;

    @Override
	public DataTableModel querySoftwaresList(Map<String, String> dataTableMap)
    {
        DataTableModel dataTableModel = new DataTableModel();
        Map<String,Object> paramMap = new HashMap<String,Object>();
        String sEcho = dataTableMap.get("sEcho");
		String Kind = dataTableMap.get("Kind");
		String SoftName = dataTableMap.get("SoftName");
		
		if ((Kind != null)&&(Kind.equals("0")))
		{
			Kind = null;
		}

        int start = Integer.parseInt(dataTableMap.get("iDisplayStart"));
        int length = Integer.parseInt(dataTableMap.get("iDisplayLength"));

        paramMap.put("start", start);
        paramMap.put("length", length);
		paramMap.put("Kind", Kind);
		paramMap.put("SoftName", SoftName);

        List<Map<String, Object>> resList = softwareInfoMapper.querySoftwaresList(paramMap);
        Integer count = softwareInfoMapper.querySoftwaresCount(paramMap);

        dataTableModel.setiTotalDisplayRecords(count);
        dataTableModel.setiTotalRecords(count);
        dataTableModel.setsEcho(Integer.valueOf(sEcho));
        dataTableModel.setAaData(resList);

        return dataTableModel;
    }

    @Override
	public int querySoftwareCountByKind(String kindId){
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("Kind", kindId);
		Integer count = softwareInfoMapper.querySoftwaresCount(paramMap);
		return count;
	}

	@Override
	public void saveSoftware(Map<String, Object> paramMap)
	{
        String recordId = (String) paramMap.get("id");
        if (StringUtils.isEmpty(recordId)) {
		try{
			paramMap.put("id", StringUtils.getUUId());
            paramMap.put("soft_id", StringUtils.getUUId());
            softwareInfoMapper.addSoftware(paramMap);
			//增加安装配置属性记录
			softwareInstallMapper.addInstall(paramMap);
			//增加默认主分支记录
			paramMap.put("branchId", StringUtils.getUUId());
			paramMap.put("softwareId", (String) paramMap.get("id"));
			paramMap.put("branchName", "master");
			paramMap.put("branchDescription", (String) paramMap.get("briefIntroduction"));
			paramMap.put("userId", (String) paramMap.get("userId"));
			softwareBranchMapper.addBranch(paramMap);
			//增加版本记录
			paramMap.put("softId",paramMap.get("id"));
			paramMap.put("historyId",StringUtils.getUUId());
			Map<String,Object> userMap = userService.getUserById((String) paramMap.get("userId"));
			paramMap.put("operator",userMap.get("user_name"));
			SimpleDateFormat sdf =new SimpleDateFormat("yyyy/MM/dd HH:mm:ss" );
			paramMap.put("uploadDate",sdf.format(new Date()));
			paramMap.put("appPktDate",sdf.format(new Date()));
			paramMap.put("appPktNew","");
			paramMap.put("appPktPath",paramMap.get("file_path"));
			paramMap.put("appPktSize",paramMap.get("size"));
			softHistoryInfoMapper.addVersion(paramMap);
            }catch (Exception e){
                e.printStackTrace();
            }
        } else {
        	try {
				softwareInfoMapper.updateSoftware(paramMap);
			}catch(Exception e){
        		e.printStackTrace();
			}
        }
	}
	
	@Override
	public Map<String, Object> getSoftwareById(String softwareId)
	{
        Map<String, Object> obj = softwareInfoMapper.querySoftwareById(softwareId);
        return obj;
    
	}
	
    @Override
	public boolean deleteSoftware(String softwareId) {
		//删除软件相关的其他表的记录
		    //删除软件截图
		screenShotsMapper.deleteScreenShotsBySoftware(softwareId);
			//删除安装配置属性记录
		softwareInstallMapper.deleteInstall(softwareId);

			//删除版本记录
		softHistoryInfoMapper.deleteVersionsBySoftware(softwareId);
		    //删除分支记录
		softwareBranchMapper.deleteBranchBySoftwareId(softwareId);
		//删除软件记录
		softwareInfoMapper.deleteSoftware(softwareId);
        return true;
    }

    @Override
	public void updateSoftware(Map<String, Object> paramMap){
        softwareInfoMapper.updateSoftware(paramMap);
    }
	
	@Override
	public void updateSoftwareIcon(Map<String, Object> paramMap){
		softwareInfoMapper.updateSoftwareIcon(paramMap);
	}
	
	@Override
	public void deleteIcon(String softwareId){
		Map<String, Object> obj = softwareInfoMapper.querySoftwareById(softwareId);
		String kindId = (String) obj.get("kind");
		String softwareName = (String)obj.get("name_en");
		Map<String, Object> kindObj =  softwareKindMapper.getKindById(kindId);
		String kindName = (String) kindObj.get("name_en");
	
		String oldIconPath = "src/main/resources/static/images/softicon/"+kindName+"/"+softwareName;
		File oldIconPathDir = new File(oldIconPath);
		String oldIconDirAbsolutePath = oldIconPathDir.getAbsolutePath();
		TestProperties.deleteFile(oldIconDirAbsolutePath);
	}
	
	@Override
	public  void deleteFile(String path)
	{
      File file = new File(path);    
      if (file.isDirectory()) 
	  {    
        File[] ff = file.listFiles();    
        for (int i = 0; i < ff.length; i++) 
		{    
            deleteFile(ff[i].getPath());    
        }    
       }    
       file.delete();    
   }

    @Override
	public void deleteDir(String rootPath, String softwareId)
    {
		Map<String, Object> obj = softwareInfoMapper.querySoftwareById(softwareId);
		if (obj == null)
		{
			return;
		}
		String kindId = (String) obj.get("kind");	
		String en_name = (String) obj.get("name_en");	
		if ((StringUtils.isEmpty(kindId))||(StringUtils.isEmpty(en_name))){
			return;
		}
		Map<String,Object> map = softwareKindMapper.getKindById(kindId);		
		if (map == null)
		{
			return;
		}
		String softPath = rootPath+"/"+map.get("name_en")+"/"+ en_name;
		File softDir = new File(softPath);
        String softDirAbsolutePath = softDir.getAbsolutePath();

		TestProperties.deleteFile(softDirAbsolutePath);
    }
	
	@Override
	public Map<String, Object> getInstallConfigBySoftwareId(String softwareId)
	{
		Map<String, Object> obj = softwareInstallMapper.getInstallById(softwareId);
        return obj;
	}
	
	@Override
    public void updateInstallAttribute(Map<String, Object> paramMap)
	{
		softwareInstallMapper.updateInstall(paramMap);
	}

	@Override
	public boolean queryScreenShot(ScreenShotInfo screenShotInfo) {
		return screenShotsMapper.getScreenShot(screenShotInfo);
	}

	@Override
	public boolean batchInsertScreenShots(List<ScreenShotInfo> screenShotInfoLists) {
    	return screenShotsMapper.insertScreenShots(screenShotInfoLists);
	}

    @Override
    public List<Map<String,Object>> querySoftwaresByKind(String kindId){
        List<Map<String, Object>> allSoftwares = softwareInfoMapper.querySoftwaresByKind(kindId);
        return allSoftwares;
    }

	@Override
	public boolean querySoftwareNameIsRepeat(Map<String, Object> paramMap) {
		Integer count = softwareInfoMapper.querySoftwareNameIsRepeat(paramMap);
		if (count == 0) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public boolean querySoftwareEnNameIsRepeat(Map<String, Object> paramMap) {
		Integer count = softwareInfoMapper.querySoftwareEnNameIsRepeat(paramMap);
		if (count == 0) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public String getIdBySoftId(String softId) {
		return softwareInfoMapper.getIdBySoftId(softId);
	}

	@Override
	public List<ScreenShotInfo> queryScreenShotListById(String id) {
		return softwareInfoMapper.queryScreenShotListById(id);
	}

	@Override
	public List<SoftwareInfo> querySoftList() {
		return softwareInfoMapper.querySoftList();
	}
}

