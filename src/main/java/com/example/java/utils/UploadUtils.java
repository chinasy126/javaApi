package com.example.java.utils;

import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UploadUtils {
    // public final static String IMG_PATH_PREFIX = "statics/upload/imgs";
    // 控制图片存储位置


    /**
     * 生成文件夹
     *
     * @param data
     * @return
     */
    public static File getImgDirFile(String data) {
        String fileDirPath = new String("src/main/resources/" + "/" + data);
        File fileDir = new File(fileDirPath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return fileDir;
    }



}
