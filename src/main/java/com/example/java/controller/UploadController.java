package com.example.java.controller;

import com.example.java.utils.UploadUtils;
import com.example.java.vo.ObjectRESTResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin
@ResponseBody
@RequestMapping
public class UploadController {

    @Value("${build.file.prefix}")
    private String buildFilePrefix;

    /**
     * 图片上传
     *
     * @param imgFile
     * @param request
     * @param session
     * @return
     */
    @RequestMapping("/uploadimg")
    public ObjectRESTResult upload(@RequestParam("file") MultipartFile imgFile, HttpServletRequest request, HttpSession session) {
        ObjectRESTResult restResult = new ObjectRESTResult();
        if (imgFile.isEmpty()) {
            restResult.setMsg("上传失败，请选择文件");
            return restResult;
        }

        String uuid = UUID.randomUUID().toString().trim();
        String filename = imgFile.getOriginalFilename();
        int index = filename.indexOf(".");
        String fileNames = uuid + filename.substring(index); // 获取文件名称

        // 获取年+月
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String data = sdf.format(dt);

        // 调用UploadUtils工具类将图片存放到服务器上
       // File fileDir = UploadUtils.getImgDirFile(data);
        UploadUtils uploadUtils = new UploadUtils();
        File fileDir = uploadUtils.getImgDirFile(data);
        try {
            File newFile = new File(fileDir.getAbsolutePath() + File.separator + fileNames);
            imgFile.transferTo(newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, Object> emp = new HashMap<>();
        emp.put("fileName", data + '/' + fileNames);

        restResult.setReturnObj(emp);
        return restResult;
    }


    @RequestMapping("/uploadPictures")
    public ObjectRESTResult uploadPictures(@RequestParam("file") MultipartFile imgFile, HttpServletRequest request, HttpSession session) {
        ObjectRESTResult restResult = new ObjectRESTResult();
        if (imgFile.isEmpty()) {
            restResult.setMsg("上传失败，请选择文件");
            return restResult;
        }

        String uuid = UUID.randomUUID().toString().trim();
        String filename = imgFile.getOriginalFilename();
        int index = filename.indexOf(".");
        String fileNames = uuid + filename.substring(index); // 获取文件名称

        // 获取年+月
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String data = sdf.format(dt);

        // 调用UploadUtils工具类将图片存放到服务器上
       // File fileDir = UploadUtils.getImgDirFile(data);
        UploadUtils uploadUtils = new UploadUtils();
        File fileDir = uploadUtils.getImgDirFile(data);
        try {
            File newFile = new File(fileDir.getAbsolutePath() + File.separator + fileNames);
            imgFile.transferTo(newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, Object> emp = new HashMap<>();
        emp.put("fileName", data + '/' + fileNames);

        restResult.setReturnObj(emp);
        return restResult;
    }

}
