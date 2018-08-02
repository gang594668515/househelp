package com.househelp.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.logging.Logger;

/**
 * @author YY
 * @version 1.0
 * @ClassName: FileUtil
 * @Description:
 * @date 2016年8月26日  上午10:26:17
 */
public class FileUtil {

    /**
     * 获取文件类型
     *
     * @param fileName
     * @return
     */
    public static String getFileExtName(String fileName) {
        if (fileName != null) {
            int i = fileName.lastIndexOf('.');
            if (i > -1) {
                return fileName.substring(i + 1).toLowerCase();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 上传文件
     *
     * @param fileBytes
     * @param filePath
     * @param fileName
     * @throws Exception
     */
    public static void uploadFile(byte[] fileBytes, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath + fileName);
        out.write(fileBytes);
        out.flush();
        out.close();
    }

    /**
     * 删除文件
     */
    public static boolean deleteFile(String fileName) {
        File targetFile = new File(fileName);
        if (targetFile.exists() && targetFile.isFile()) {
            if (targetFile.delete()) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

}