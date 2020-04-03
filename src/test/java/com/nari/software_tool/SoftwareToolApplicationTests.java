package com.nari.software_tool;

import com.nari.software_tool.dao.SoftHistoryInfoMapper;
import com.nari.software_tool.service.SoftwareKindService;
import com.nari.software_tool.service.VersionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sun.misc.Version;
import util.aes.MD5.MD5Util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//git push test1111
@SpringBootTest
class SoftwareToolApplicationTests {
    //test git
    @Autowired
    SoftwareKindService softwareKindService;

    @Autowired
    SoftHistoryInfoMapper softHistoryInfoMapper;

    @Autowired
    VersionService versionService;

    @Test
    void contextLoads() throws IOException {
        System.out.println(versionService.getHistoryById("f9638b461440457f9d561e38a4c48835"));
    }

}
