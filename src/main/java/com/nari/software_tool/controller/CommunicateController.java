package com.nari.software_tool.controller;

import com.alibaba.fastjson.JSONObject;
import com.nari.software_tool.entity.Communicate.CommunicatePojo;
import com.nari.software_tool.entity.Communicate.CommunicateSoftPojo;
import com.nari.software_tool.service.CommunicateService;
import com.nari.software_tool.service.SoftwareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @RequestMapping(value="getSoftList",method= RequestMethod.POST)
    @ResponseBody
    public JSONObject getSoftList(){
        JSONObject jsonObject = new JSONObject();
        CommunicatePojo communicatePojo = communicateService.softReqCollect(softwareService.querySoftList());
        System.out.println(communicatePojo);
        jsonObject.put("Content",communicatePojo);
        return jsonObject;
    }


}
