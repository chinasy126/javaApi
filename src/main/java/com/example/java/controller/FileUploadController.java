package com.example.java.controller;

import com.example.java.service.FileUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;


@RestController
@CrossOrigin
public class FileUploadController {

    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public String up(String nickname, MultipartFile photo, HttpServletRequest request) throws IOException {
        System.out.println(nickname);

        System.out.println(photo.getOriginalFilename());
        System.out.println(photo.getContentType());
        String path = request.getServletContext().getRealPath("/uploader/");
        System.out.println(path);
        saveFile(photo,path);
        return "上传从成功";
    }

    public void saveFile(MultipartFile photo, String path) throws IOException {
        File dir = new File(path);
        if (!dir.exists()){
            dir.mkdir();
        }

        File file = new File(path+ photo.getOriginalFilename());
        photo.transferTo(file);
    }




    @RequestMapping("/multipleImageUpload")
    public List multipleImageUpload(@RequestParam("uploadFiles") MultipartFile[] files){
        System.out.println("上传的图片数："+files.length);

        List<Map<String,Object>> root=new ArrayList<Map<String,Object>>();

        for (MultipartFile file : files) {    //循环保存文件

            Map<String,Object> result=new HashMap<String, Object>();//一个文件上传的结果
            String result_msg="";//上传结果信息

            if (file.getSize() / 1000 > 100){
                result_msg="图片大小不能超过100KB";
            }
            else{
                //判断上传文件格式
                String fileType = file.getContentType();
                if (fileType.equals("image/jpeg") || fileType.equals("image/png") || fileType.equals("image/jpeg")) {
                    // 要上传的目标文件存放的绝对路径
                   // final String localPath="E:\\";
                    final String localPath = "D:\\dev\\java\\src\\main\\resources\\static";

                    //上传后保存的文件名(需要防止图片重名导致的文件覆盖)
                    //获取文件名
                    String fileName = file.getOriginalFilename();
                    //获取文件后缀名
                    String suffixName = fileName.substring(fileName.lastIndexOf("."));
                    //重新生成文件名
                    fileName = UUID.randomUUID()+suffixName;
                    if (FileUtils.upload(file, localPath, fileName)) {
                        //文件存放的相对路径(一般存放在数据库用于img标签的src)
                        String relativePath="img/"+fileName;
                        result.put("relativePath",relativePath);//前端根据是否存在该字段来判断上传是否成功
                        result_msg="图片上传成功";
                    }
                    else{
                        result_msg="图片上传失败";
                    }
                }
                else{
                    result_msg="图片格式不正确";
                }
            }
            result.put("result_msg",result_msg);
            root.add(result);
        }
//        String root_json = JSON.toJSONString(root);
//        System.out.println(root_json);
        return root;
    }


}
