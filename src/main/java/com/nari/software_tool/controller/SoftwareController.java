package com.nari.software_tool.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nari.software_tool.entity.SoftwareInfo;
import com.nari.software_tool.service.FileService;
import com.nari.software_tool.service.SoftwareService;
import com.nari.software_tool.service.SoftwareKindService;
import com.nari.software_tool.entity.DataTableModel;
import com.nari.software_tool.entity.DataTableParam;

import util.aes.DatatableUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Arrays;
import java.util.Map;
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
		int install_type = softwareInfo.getInstallType();
		if (0==install_type)
        {
            //解压安装包
        }

        Map<String,Object> map = softwareKindService.getKindById(kindId);
        String softPath = rootPath+"/"+map.get("name_en")+"/"+ softwareInfo.getNameEn()+"/"+"master"+"/"+softwareInfo.getLatestVersion();
        
        String root = rootPath;
        File rootDir = new File(root);
        
        String typePath = rootPath+"/"+map.get("name_en");
        File typeDir = new File(typePath);
        
        String namePath = rootPath+"/"+map.get("name_en")+"/"+ softwareInfo.getNameEn();
        File nameDir = new File(namePath);
        
        String branchPath = rootPath+"/"+map.get("name_en")+"/"+ softwareInfo.getNameEn()+"/"+"master";
        File branchDir = new File(branchPath);

        String versionPath = rootPath+"/"+map.get("name_en")+"/"+ softwareInfo.getNameEn()+"/"+"master"+"/"+softwareInfo.getLatestVersion();
        File versionDir = new File(versionPath);
        
		if(!rootDir.exists()) {
          rootDir.mkdir();
		}
		
		if(!rootDir.exists()) {
          rootDir.mkdir();
		}
		
		if(!typeDir.exists()) {
          typeDir.mkdir();
		}
		
		if(!nameDir.exists()) {
          nameDir.mkdir();
		}
		
		if(!branchDir.exists()) {
          branchDir.mkdir();
		}

        if(!versionDir.exists()) {
            versionDir.mkdir();
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

    @PostMapping("/downloadSoftware")
    @ResponseBody
    public String downloadSoftware(HttpServletRequest request,HttpServletResponse response) throws IOException {
        JSONObject jsonObject = new JSONObject();
        Map<String,String[]> res = request.getParameterMap();
        System.out.println("-----"+ Arrays.toString(res.get("fileName"))
                +"  "+ Arrays.toString(res.get("softwareUrl")) +"-----");
        String fileName = Arrays.toString(res.get("fileName")).substring(Arrays.toString(res.get("fileName")).indexOf("[")+1,Arrays.toString(res.get("fileName")).indexOf("]"));
        if(fileName != null){
            File file = new File(Arrays.toString(res.get("softwareUrl")).substring(Arrays.toString(res.get("softwareUrl")).indexOf("[")+1,Arrays.toString(res.get("softwareUrl")).indexOf("]")));
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
