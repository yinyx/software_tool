package com.nari.software_tool.service.serviceImpl;

import java.util.Map;
import java.util.UUID;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.io.File;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.nari.software_tool.service.SoftwareService;
import com.nari.software_tool.dao.SoftwareInfoMapper;
import com.nari.software_tool.dao.SoftwareKindMapper;
import com.nari.software_tool.entity.DataTableModel;
import com.nari.software_tool.entity.Page;
import com.nari.software_tool.entity.SoftwareInfo;

import util.aes.PaginationUtil;
import util.aes.StringUtils;

@Service
@Transactional
public class SoftwareServiceImple implements SoftwareService{
    // 注入软件类别Mapper
    @Autowired
    private SoftwareInfoMapper softwareInfoMapper;
	
	@Autowired
	private SoftwareKindMapper softwareKindMapper;

    public DataTableModel querySoftwaresList(Map<String, String> dataTableMap)
    {
        DataTableModel dataTableModel = new DataTableModel();
        Map<String,Object> paramMap = new HashMap<String,Object>();
        String sEcho = dataTableMap.get("sEcho");
		String Kind = dataTableMap.get("Kind");
		
		if ((Kind != null)&&(Kind.equals("0")))
		{
			Kind = null;
		}

        int start = Integer.parseInt(dataTableMap.get("iDisplayStart"));
        int length = Integer.parseInt(dataTableMap.get("iDisplayLength"));

        paramMap.put("start", start);
        paramMap.put("length", length);
		paramMap.put("Kind", Kind);

        List<Map<String, Object>> resList = softwareInfoMapper.querySoftwaresList(paramMap);
        Integer count = softwareInfoMapper.querySoftwaresCount(paramMap);

        dataTableModel.setiTotalDisplayRecords(count);
        dataTableModel.setiTotalRecords(count);
        dataTableModel.setsEcho(Integer.valueOf(sEcho));
        dataTableModel.setAaData(resList);

        return dataTableModel;
    }
	
	public void saveSoftware(Map<String, Object> paramMap)
	{
        String recordId = (String) paramMap.get("id");
        if (StringUtils.isEmpty(recordId)) {
			paramMap.put("id", StringUtils.getUUId());
            paramMap.put("soft_id", StringUtils.getUUId());
            softwareInfoMapper.addSoftware(paramMap);
        } else {
            softwareInfoMapper.updateSoftware(paramMap);
        }
	}
	
	public Map<String, Object> getSoftwareById(String softwareId)
	{
        Map<String, Object> obj = softwareInfoMapper.querySoftwareById(softwareId);
        return obj;
    
	}
	
    public boolean deleteSoftware(String softwareId) {
        softwareInfoMapper.deleteSoftware(softwareId);
		//删除软件相关的其他表的记录
		
        return true;
    }

    public void updateSoftware(Map<String, Object> paramMap){
        softwareInfoMapper.updateSoftware(paramMap);
    }
	
	public void updateSoftwareIcon(Map<String, Object> paramMap){
		softwareInfoMapper.updateSoftwareIcon(paramMap);
	}
	
	public void deleteIcon(String softwareId){
		Map<String, Object> obj = softwareInfoMapper.querySoftwareById(softwareId);
		System.out.println("obj");
		System.out.println(obj);
		String oldIconpath = (String) obj.get("icon");	
		System.out.println("oldIconpath");
		System.out.println(oldIconpath);		
		oldIconpath = "src/main/resources/static/"+oldIconpath;
		File oldIconpathDir = new File(oldIconpath);
		String oldIconDirAbsolutePath = oldIconpathDir.getAbsolutePath();
		System.out.println("oldIconDirAbsolutePath");
		System.out.println(oldIconDirAbsolutePath);		
		
		if (!StringUtils.isEmpty(oldIconDirAbsolutePath))
		{
			//删除原来的图标
		    File file = new File(oldIconDirAbsolutePath);
            if (file.exists()) {
                file.delete();
                System.out.println("文件已删除");
            }
		}
	}
	
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
        deleteFile(softDirAbsolutePath);
    }
}

