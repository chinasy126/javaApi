package com.example.java.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.java.service.AliyunOssUploadService;
import com.example.java.utils.Result;
import com.example.java.utils.UploadUtils;
import com.example.java.vo.ObjectRESTResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RestController
@CrossOrigin
@ResponseBody
@RequestMapping(value = "/upload")
public class ImgUploadController {

    @Value("${upload.image.prefix}")
    private String upImagePrefix;

//    @Value("${build.file.prefix}")
//    private String buildFilePrefix;

    @Autowired
    private AliyunOssUploadService aliyunOssUploadService;

    /**
     * 图片上传
     *
     * @param imgFile
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/pictures", method = RequestMethod.POST)
    public Result uploadPictures(@RequestParam("file") MultipartFile imgFile,
                                 @RequestParam(value = "type", required = false, defaultValue = "") String type,
                                 HttpServletRequest request, HttpSession session) {
        ObjectRESTResult restResult = new ObjectRESTResult();
        if (imgFile.isEmpty() ) {
            return Result.error().message("上传失败，请选择文件");
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
        if (StringUtils.isNotBlank(type)) {
            data = type + '/' + data;
        }

        data = upImagePrefix + '/' + data;

        //File fileDir = UploadUtils.getImgDirFile(data);
        UploadUtils uploadUtils = new UploadUtils();

//        File fileDir = uploadUtils.getImgDirFile(buildFilePrefix,data);
        File fileDir = uploadUtils.getImgDirFile(data);

        try {
            File newFile = new File(fileDir.getAbsolutePath() + File.separator + fileNames);
            imgFile.transferTo(newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.ok().data("file", data + '/' + fileNames);
    }

    /**
     * 阿里云oss
     * @param imgFile
     * @param type
     * @param request
     * @param session
     * @return
     */
    public Result ossUploadPictures(@RequestParam("file") MultipartFile imgFile,
                                 @RequestParam(value = "type", required = false, defaultValue = "") String type,
                                 HttpServletRequest request, HttpSession session){
        String fileNames = aliyunOssUploadService.upload(imgFile);
        return Result.ok().data("file", fileNames);
    }



}
