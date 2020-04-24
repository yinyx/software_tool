package com.nari.software_tool.controller;

import com.alibaba.fastjson.JSONObject;
import com.nari.software_tool.entity.Communicate.CommunicatePacketPojo;
import com.nari.software_tool.entity.Communicate.CommunicatePojo;
import com.nari.software_tool.entity.Communicate.CommunicateSoftPojo;
import com.nari.software_tool.entity.Communicate.UserPojo;
import com.nari.software_tool.service.CommunicateService;
import com.nari.software_tool.service.PluginService;
import com.nari.software_tool.service.SoftwareService;
import com.nari.software_tool.service.VersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.plugin2.main.server.Plugin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

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
    @Autowired
    private PluginService pluginService;

    @RequestMapping(value="getSoftList",method= RequestMethod.POST)
    @ResponseBody
    public JSONObject getSoftList(){
        JSONObject jsonObject = new JSONObject();
        CommunicatePojo communicatePojo = communicateService.softReqCollect(softwareService.querySoftList(),pluginService.getPluginList());
        System.out.println(communicatePojo);
        jsonObject.put("Content",communicatePojo);
        return jsonObject;
    }

    @RequestMapping(value="getHistoryList",method= RequestMethod.POST)
    @ResponseBody
    public JSONObject getHistoryList(@RequestParam("UUID") String softId){
        JSONObject jsonObject = new JSONObject();
        String id = softwareService.getIdBySoftId(softId);
        CommunicatePojo communicatePojo = communicateService.hisReqCollect(versionService.queryHisList(id),softId);
        System.out.println(communicatePojo);
        jsonObject.put("Content",communicatePojo);
        return jsonObject;
    }

    @RequestMapping(value="checkUserAuth",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject checkUserAuth(@RequestBody JSONObject jsonStr){
        JSONObject jsonObject = new JSONObject();
        String userName = (String) jsonStr.get("Username");
        String password = (String) jsonStr.get("Pwd");
        UserPojo userPojo = communicateService.userReqCollect(userName,password);
        jsonObject.put("Content",userPojo);
        return jsonObject;
    }

    @RequestMapping(value="getPkt",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getPkt(@RequestBody JSONObject jsonStr){
        JSONObject jsonObject = new JSONObject();
        String historyId = (String) jsonStr.get("UUID");
        String MD5 = (String) jsonStr.get("MD5");
        CommunicatePacketPojo communicatePacketPojo = communicateService.pktReqCollect(versionService.queryPktInfo(historyId,MD5));
        jsonObject.put("Content",communicatePacketPojo);
        return jsonObject;
    }


    @RequestMapping(value = "downloadPkt",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String downloadPkt(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject jsonObject = new JSONObject();
        String fileName = (request.getParameter("fileName"));
        if(fileName != null){
            File file = new File(request.getParameter("softwareUrl"));
            if(file.exists()){
                response.setContentType("application/force-download");
                response.addHeader("Content-Disposition","attachment;fileName="+fileName);
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try{
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while(i != -1){
                        os.write(buffer,0,i);
                        i = bis.read(buffer);
                    }
                    jsonObject.put("result","download success");
                    return jsonObject.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    bis.close();
                    fis.close();
                }
            }
        }
        jsonObject.put("result","failure");
        return jsonObject.toString();
    }

}
