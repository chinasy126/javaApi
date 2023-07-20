package com.example.java.utils;

<<<<<<< HEAD
=======
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

>>>>>>> master
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

<<<<<<< HEAD
public class UploadUtils {
    public final static String IMG_PATH_PREFIX = "statics/upload/imgs";
=======
@Component
public class UploadUtils {
    // public final static String IMG_PATH_PREFIX = "statics/upload/imgs";
    // 控制图片存储位置

    @Value("${build.file.prefix}")
    private String buildFilePrefix;
>>>>>>> master

    /**
     * 生成文件夹
     *
     * @param data
     * @return
     */
<<<<<<< HEAD
    public static File getImgDirFile(String data) {
        String fileDirPath = new String("src/main/resources/" + IMG_PATH_PREFIX + "/" + data);
=======
    public static File getImgDirFilet(String data) {
        String fileDirPath = new String("src/main/resources/" + "/" + data);
>>>>>>> master
        File fileDir = new File(fileDirPath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return fileDir;
    }
<<<<<<< HEAD
=======

    public File getImgDirFile(String data) {
        String fileDirPath = new String(data);
        File fileDir = new File(fileDirPath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return fileDir;
    }


>>>>>>> master
}
