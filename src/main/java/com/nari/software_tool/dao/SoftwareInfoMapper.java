package com.nari.software_tool.dao;

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
    SoftwareInfo querySoftwareById(@Param("softId") String softId);

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

}
