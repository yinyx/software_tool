package com.nari.software_tool;

import com.nari.software_tool.service.SoftwareKindService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

//git push test1111
@SpringBootTest
class SoftwareToolApplicationTests {
    //test git
    @Autowired
    SoftwareKindService softwareKindService;
    @Test
    void contextLoads() {
        HashMap<String,Object> map = (HashMap<String, Object>) softwareKindService.getKindById("94f048541927437bb86040a189ba72b2");
    }

}
