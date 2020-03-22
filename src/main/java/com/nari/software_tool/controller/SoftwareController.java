package com.nari.software_tool.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nari.software_tool.entity.SoftwareInfo;
import com.nari.software_tool.entity.SysAuthInfo;
import com.nari.software_tool.service.FileService;
import com.nari.software_tool.service.SoftwareService;
import com.nari.software_tool.service.SoftwareKindService;
import com.nari.software_tool.entity.DataTableModel;
import com.nari.software_tool.entity.DataTableParam;

import util.aes.AesUtil;
import util.aes.DatatableUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.UUID;
import java.util.HashMap;

/**
 * @author yinyx
 * @version 1.0 2020/3/19
 */
@RestController
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

    // 注入软件类别Service
    @Autowired
    private SoftwareService softwareService;

    @RequestMapping(value="/querySoftwaresList",method=RequestMethod.POST)
    public Object  querySoftwaresList(@RequestBody DataTableParam[] dataTableParams){

        DataTableModel dataTableModel = new DataTableModel();
        Map<String, String> dataTableMap = DatatableUtil.convertToMap(dataTableParams);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            dataTableModel = softwareService.querySoftwaresList(dataTableMap);
            resultMap.put("status", "success");
            resultMap.put("SoftwaresData", dataTableModel);
        }
        catch(Exception e)
        {
            resultMap.put("status", "error");
            resultMap.put("msg", "查询软件信息异常!");
        }
        return resultMap;
    }

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
        String softPath = rootPath+"/"+map.get("name_en")+"/"+ softwareInfo.getNameEn()+"/"+"master";

        String root = rootPath;
        File rootDir = new File(root);

        String typePath = rootPath+"/"+map.get("name_en");
        File typeDir = new File(typePath);

        String namePath = rootPath+"/"+map.get("name_en")+"/"+ softwareInfo.getNameEn();
        File nameDir = new File(namePath);

        String branchPath = rootPath+"/"+map.get("name_en")+"/"+ softwareInfo.getNameEn()+"/"+"master";
        File branchDir = new File(branchPath);
      if(!rootDir.exists()) {
          rootDir.mkdir();
          System.out.println("根目录已创建");
          if (!typeDir.exists()) {
              typeDir.mkdir();
              System.out.println("一级目录已创建");
              if (!nameDir.exists()) {
                  nameDir.mkdir();
                  System.out.println("二级目录已创建");
                  if (!branchDir.exists()) {
                      branchDir.mkdir();
                      System.out.println("三级级目录已创建");
                  }
              }
          }
      }
        //存储文件；
        File softDir = new File(softPath);
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
