package com.nari.software_tool.controller;

import com.nari.software_tool.entity.Communicate.CommunicatePojo;
import com.nari.software_tool.entity.Communicate.CommunicateSoftPojo;
import com.nari.software_tool.service.CommunicateService;
import com.nari.software_tool.service.SoftwareService;
import net.sf.json.JSONObject;
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
        jsonObject.put("message","请求软件列表");
        jsonObject.put("Content",communicatePojo);
        return jsonObject;
    }


}
