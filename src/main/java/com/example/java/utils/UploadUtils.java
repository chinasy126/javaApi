package com.example.java.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UploadUtils {
    public final static String IMG_PATH_PREFIX = "statics/upload/imgs";

    /**
     * 生成文件夹
     *
     * @param data
     * @return
     */
    public static File getImgDirFile(String data) {
        String fileDirPath = new String("src/main/resources/" + IMG_PATH_PREFIX + "/" + data);
        File fileDir = new File(fileDirPath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return fileDir;
    }
}
