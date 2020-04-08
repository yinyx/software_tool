package com.nari.software_tool.dao;

import com.nari.software_tool.entity.ScreenShotInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yinyx
 * @version 1.0 2020/3/26
 */
@Mapper
@Repository
public interface ScreenShotsMapper {

    List<ScreenShotInfo> getScreenShotsList();

    boolean insertScreenShots(List<ScreenShotInfo> screenShotInfoList);

    boolean getScreenShot(ScreenShotInfo screenShotInfo);

    void deleteScreenShotsByKind(String kindId);

    void deleteScreenShotsBySoftware(String softwareId);

}
