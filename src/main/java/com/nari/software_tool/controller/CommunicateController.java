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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    Logger logger =  LoggerFactory.getLogger(getClass());

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
        logger.info("开始请求软件列表 ===============");
        CommunicatePojo communicatePojo = communicateService.softReqCollect(softwareService.querySoftList(),pluginService.getPluginList());
        logger.info("列表内容："+communicatePojo.toString());
        jsonObject.put("Content",communicatePojo);
        return jsonObject;
    }

    @RequestMapping(value="getHistoryList",method= RequestMethod.POST)
    @ResponseBody
    public JSONObject getHistoryList(@RequestParam("UUID") String softId){
        JSONObject jsonObject = new JSONObject();
        logger.info("开始请求历史列表 ===============");
        String id = softwareService.getIdBySoftId(softId);
        CommunicatePojo communicatePojo = communicateService.hisReqCollect(versionService.queryHisList(id),softId);
        logger.info("列表内容："+communicatePojo.toString());
        jsonObject.put("Content",communicatePojo);
        return jsonObject;
    }

    @RequestMapping(value="checkUserAuth",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject checkUserAuth(@RequestBody JSONObject jsonStr){
        JSONObject jsonObject = new JSONObject();
        logger.info("++++++++++ 开始校验客户端用户权限 ++++++++++");
        String userName = (String) jsonStr.get("Username");
        String password = (String) jsonStr.get("Pwd");
        UserPojo userPojo = communicateService.userReqCollect(userName,password);
        jsonObject.put("Content",userPojo);
        logger.info("++++++++++ 返回校验结果 ++++++++++");
        return jsonObject;
    }

    @RequestMapping(value="getPkt",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getPkt(@RequestBody JSONObject jsonStr){
        JSONObject jsonObject = new JSONObject();
        logger.info("查询程序包信息 ===============");
        String historyId = (String) jsonStr.get("UUID");
        String MD5 = (String) jsonStr.get("MD5");
        CommunicatePacketPojo communicatePacketPojo = communicateService.pktReqCollect(versionService.queryPktInfo(historyId,MD5));
        logger.info("程序包信息内容："+communicatePacketPojo.toString());
        jsonObject.put("Content",communicatePacketPojo);
        return jsonObject;
    }


    @RequestMapping(value = "downloadPkt")
    public String downloadPkt(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        logger.info("开始文件下载 ===============");
        String softwareUrl = (request.getParameter("softwareUrl"));
        String[] pathArray = softwareUrl.split("/");
        String newPath = pathArray[0];
        for(int i = 1; i < pathArray.length-1; i++) {
            newPath = newPath + "/" + pathArray[i];
        }
        newPath += "/";
        String fileName = pathArray[pathArray.length-1];
        if(fileName != null){
            File file = new File(newPath,fileName);
            if (file.exists()) {
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition", "attachment;fileName=" + new String(fileName.getBytes("utf-8"),"ISO-8859-1"));// 设置文件名
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                        logger.info("下载中 ===============");
                    }
                    logger.info("文件下载完成 ===============");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }


}
