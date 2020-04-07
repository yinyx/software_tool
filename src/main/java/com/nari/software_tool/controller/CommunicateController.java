package com.nari.software_tool.controller;

import com.alibaba.fastjson.JSONObject;
import com.nari.software_tool.entity.Communicate.CommunicatePojo;
import com.nari.software_tool.entity.Communicate.CommunicateSoftPojo;
import com.nari.software_tool.service.CommunicateService;
import com.nari.software_tool.service.SoftwareService;
import com.nari.software_tool.service.VersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author yinyx
 * @version 1.0 2020/4/2
 */
@Controller
@RequestMapping(value="/communicate")
public class CommunicateController
{
    @Autowired
    private CommunicateService communicateService;
    @Autowired
    private SoftwareService softwareService;
    @Autowired
    private VersionService versionService;

    @RequestMapping(value="getSoftList",method= RequestMethod.POST)
    @ResponseBody
    public JSONObject getSoftList(){
        JSONObject jsonObject = new JSONObject();
        CommunicatePojo communicatePojo = communicateService.softReqCollect(softwareService.querySoftList());
        System.out.println(communicatePojo);
        jsonObject.put("Content",communicatePojo);
        return jsonObject;
    }

    @RequestMapping(value="getHistoryList",method= RequestMethod.POST)
    @ResponseBody
    public JSONObject getHistoryList(@RequestParam("ID") String id){
        JSONObject jsonObject = new JSONObject();
        CommunicatePojo communicatePojo = communicateService.hisReqCollect(versionService.queryHisList(id));
        System.out.println(communicatePojo);
        jsonObject.put("Content",communicatePojo);
        return jsonObject;
    }


}
