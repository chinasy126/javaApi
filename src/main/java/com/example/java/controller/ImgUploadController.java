package com.example.java.controller;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.java.service.AliyunOssUploadService;
import com.example.java.utils.QiniuImageUtils;
import com.example.java.utils.Result;
import com.example.java.utils.UploadUtils;
import com.example.java.vo.ObjectRESTResult;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin
@ResponseBody
@RequestMapping(value = "/upload")
public class ImgUploadController {

    @Value("${upload.image.prefix}")
    private String upImagePrefix;

    @Value("${upload.image.prefix.url}")
    private String upImagePrefixUrl;

    @Value("${upload.file.method}")
    private String uploadethod;

//    @Value("${build.file.prefix}")
//    private String buildFilePrefix;

    @Autowired
    private AliyunOssUploadService aliyunOssUploadService;

    @Autowired
    private QiniuImageUtils qiniuImageUtils;

    /**
     * 获取真是图片地址
     *
     * @param imageUrl
     * @return
     */
    @RequestMapping(value = "/qiniuImageUrl", method = RequestMethod.POST)
    public Result qiNiuImageUrl(@RequestBody Map<String, String> map) {
        String url = map.get("url");
        String privateImageUrl = qiniuImageUtils.generatePrivateImageUrl(url);
        return Result.ok().data("data", privateImageUrl);

    }


    public Result qiniuUploadImage(@RequestParam(value = "file", required = false) MultipartFile multipartFile,
                                   @RequestParam(value = "fullpath", required = false) String fullpath
    ) {
        String uploadImagesUrl = qiniuImageUtils.uploadImageQiniu(multipartFile);
        if (fullpath.equals("1")) {
            uploadImagesUrl = qiniuImageUtils.generatePrivateImageUrl(uploadImagesUrl);
        }
        return Result.ok().data("file", uploadImagesUrl);
    }

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
                                 HttpServletRequest request, HttpSession session,
                                 @RequestParam(value = "fullpath", required = false, defaultValue = "0") String fullpath) {

        if (uploadethod.equals("local")) {
            return localUpload(imgFile, type, request, session);
        } else if (uploadethod.equals("qiniu")) {
            return qiniuUploadImage(imgFile, fullpath);
        } else if (uploadethod.equals("oss")) {
            return ossUploadPictures(imgFile, type, request, session);
        } else {
            return Result.error().message("没有上传方法!");
        }
    }

    public Result localUpload(@RequestParam("file") MultipartFile imgFile,
                              @RequestParam(value = "type", required = false, defaultValue = "") String type,
                              HttpServletRequest request, HttpSession session) {
        ObjectRESTResult restResult = new ObjectRESTResult();
        if (imgFile.isEmpty()) {
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

        // 存储位置
        String imgDataUrl = '/' + upImagePrefixUrl + '/' + data;

        // 文件存放相对地址
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
        return Result.ok().data("file", imgDataUrl + '/' + fileNames);
    }

    /**
     * 阿里云oss
     *
     * @param imgFile
     * @param type
     * @param request
     * @param session
     * @return
     */
    public Result ossUploadPictures(@RequestParam("file") MultipartFile imgFile,
                                    @RequestParam(value = "type", required = false, defaultValue = "") String type,
                                    HttpServletRequest request, HttpSession session) {
        String fileNames = aliyunOssUploadService.upload(imgFile);
        return Result.ok().data("file", fileNames);
    }


}
