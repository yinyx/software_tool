package com.nari.software_tool.dao;

import com.nari.software_tool.entity.ScreenShotInfo;
import com.nari.software_tool.entity.SoftwareInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author yinyx
 * @version 1.0 2020/3/16
 */
@Mapper
@Repository
public interface SoftwareInfoMapper {

    /**
     * 获取列表
     * @return
     */
    List<SoftwareInfo> querySoftwareList();

    /**
     * 条件查询（软件Id)
     * @param softId
     * @return
     */
    Map<String, Object> querySoftwareById(@Param("softId") String softId);

    /**
     * 模糊查询（中文、英文名）
     * @param name
     * @return
     */
    SoftwareInfo querySoftwareByName(@Param("name") String name);

    /**
     * 根据Id 更新
     * @param softwareInfo
     * @return
     */
    int updateSoftwareInfo(SoftwareInfo softwareInfo);

    /**
     * 录入信息
     * @param softwareInfo
     * @return
     */
    int insertSoftwareInfo(SoftwareInfo softwareInfo);

    /**
     * 批量插入
     * @param softwareInfoList
     * @return
     */
    int batchInsertSoftware(List<SoftwareInfo> softwareInfoList);

    /**
     * 批量更新
     * @param softwareInfoList
     * @return
     */
    int batchUpdateSoftware(List<SoftwareInfo> softwareInfoList);
	
	List<Map<String, Object>> querySoftwaresList(Map<String, Object> paramMap);

    int querySoftwaresCount(Map<String, Object> paramMap);
	
	void addSoftware(Map<String, Object> paramMap);

    void updateSoftware(Map<String, Object> paramMap);
	
	void deleteSoftware(String softwareId);
	
	void updateSoftwareIcon(Map<String, Object> paramMap);
	
	List<Map<String, Object>> querySoftwaresByKind(String kindId);

    /**
     * 查询软件是否重名
     * @param paramMap
     * @return int
     */
    int querySoftwareNameIsRepeat(Map<String, Object> paramMap);

    /**
     * 查询软件英文名是否重名
     * @param paramMap
     * @return int
     */
    int querySoftwareEnNameIsRepeat(Map<String, Object> paramMap);

	List<ScreenShotInfo> queryScreenShotListById(String id);

	List<SoftwareInfo> querySoftList();

}
