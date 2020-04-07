package com.nari.software_tool.controller;

import com.alibaba.fastjson.JSONObject;
import com.nari.software_tool.entity.Communicate.CommunicatePojo;
import com.nari.software_tool.entity.Communicate.CommunicateSoftPojo;
import com.nari.software_tool.entity.Communicate.UserPojo;
import com.nari.software_tool.service.CommunicateService;
import com.nari.software_tool.service.SoftwareService;
import com.nari.software_tool.service.VersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value="checkUserAuth",method= RequestMethod.POST)
    @ResponseBody
    public JSONObject checkUserAuth(@RequestParam("Username") String userName ,@RequestParam("Pwd") String password){
        JSONObject jsonObject = new JSONObject();
        UserPojo userPojo = communicateService.userReqCollect(userName,password);
        jsonObject.put("Content",userPojo);
        return jsonObject;
    }

    @PostMapping("/downloadSoftware")
    @ResponseBody
    public String downloadSoftware(HttpServletRequest request, HttpServletResponse response) throws IOException {
        net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
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
