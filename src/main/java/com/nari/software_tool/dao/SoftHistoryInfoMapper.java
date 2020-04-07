package com.nari.software_tool.dao;

import com.nari.software_tool.entity.SoftHistoryInfo;
import com.nari.software_tool.entity.SoftwareInfo;
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
public interface SoftHistoryInfoMapper {
    Map<String, Object> queryHistoryById(@Param("id") String historyId);
	
	Map<String, Object> getVersionPkgCfgById(@Param("id") String historyId);

    List<Map<String, Object>> queryHistoryListBySoftwareId(String softId);

    List<Map<String, Object>> queryHistoryList(Map<String, Object> paramMap);

    int queryHistoryCount(Map<String, Object> paramMap);

    int addVersion(Map<String, Object> paramMap);

    int updateVersion(Map<String,Object> paramMap);
	
	void setVersionInstllconfig(Map<String,Object> paramMap);

	int queryHistoryVersionCount(@Param("softId") String softId);

    List<String> queryHistoryVersionUrl(@Param("softId") String softId);

    SoftHistoryInfo queryHistoryVersion(@Param("softId") String softId);

    List<SoftHistoryInfo> queryHisList(@Param("softId") String softId);

    void deleteVersionsByKind(String kindId);
}
