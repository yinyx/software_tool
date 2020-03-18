package com.nari.software_tool.dao;

import com.nari.software_tool.entity.SoftPathInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yinyx
 * @version 1.0 2020/3/16
 */
@Mapper
@Repository
public interface SoftPathInfoMapper {

    /**
     * 获取列表
     * @return
     */
    List<SoftPathInfo> querySoftPathList();

    /**
     * 条件查询（软件Id)
     * @param softId
     * @return
     */
    SoftPathInfo querySoftPathById(@Param("softId") int softId);

    /**
     * 根据Id 更新
     * @param softPathInfo
     * @return
     */
    int updateSoftPathInfo(SoftPathInfo softPathInfo);

    /**
     * 录入
     * @param softPathInfo
     * @return
     */
    int insertSoftPathInfo(SoftPathInfo softPathInfo);

    /**
     * 批量插入
     * @param softPathInfoList
     * @return
     */
    int batchInsertSoftPath(List<SoftPathInfo> softPathInfoList);

    /**
     * 批量更新
     * @param softPathInfoList
     * @return
     */
    int batchUpdateSoftPath(List<SoftPathInfo> softPathInfoList);
}
