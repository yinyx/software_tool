package com.nari.software_tool.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nari.software_tool.entity.SoftwareInfo;
import com.nari.software_tool.entity.SysAuthInfo;
import com.nari.software_tool.service.FileService;
import com.nari.software_tool.service.SoftwareKindService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * @author yinyx
 * @version 1.0 2020/3/19
 */
@Controller
@RequestMapping(value = "/software")
public class SoftwareController {

    @Value("${iconPath}")
    private String iconPath;

    @Value("${rootPath}")
    private String rootPath;

    @Autowired
    private SoftwareKindService softwareKindService;

    @Autowired
    private FileService fileService;

    @PostMapping("/uploadSoftware")
    @ResponseBody
    public JSONObject uploadSoftware(@RequestParam("icon") MultipartFile icon, @RequestParam("soft") MultipartFile soft, @RequestParam("softwareForm") String softwareStr) throws IOException {
        JSONObject jsonObject = new JSONObject();
        //存储图标；
        File iconDir = new File(iconPath);
        String iconDirAbsolutePath = iconDir.getAbsolutePath();
        if(!iconDir.exists()){
            iconDir.mkdir();
        }
        String iconName = icon.getOriginalFilename();
        try {
            icon.transferTo(new File(iconDirAbsolutePath,iconName));
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject software = JSONObject.fromObject(softwareStr);
        ObjectMapper objectMapper = new ObjectMapper();
        SoftwareInfo softwareInfo = objectMapper.readValue(software.toString(),SoftwareInfo.class);

        String kindId = softwareInfo.getKind();
        Map<String,Object> map = softwareKindService.getKindById(kindId);
        System.out.println(map+softwareInfo.toString());
        //存储文件；
        File softDir = new File(rootPath+map.get("kind_name")+"\\"+ softwareInfo.getNameEn()+"\\"+"master");
        String softDirAbsolutePath = softDir.getAbsolutePath();
        if(!softDir.exists()){
            softDir.mkdir();
        }
        String softName = soft.getOriginalFilename();
        try {
            soft.transferTo(new File(softDirAbsolutePath,softName));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
}
