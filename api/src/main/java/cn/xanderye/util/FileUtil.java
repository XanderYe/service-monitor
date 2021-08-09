package cn.xanderye.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author XanderYe
 * @description:
 * @date 2021/8/3 14:27
 */
public class FileUtil {
    /**
     * 保存文件
     * @param file
     * @return java.io.File
     * @author XanderYe
     * @date 2021/8/3
     */
    public static File saveMultipartFile(MultipartFile file, String parentPath, String ext) {
        File destFile;
        String destFileName = parentPath + File.separator + UUID.randomUUID().toString() + "." + ext;
        destFile = new File(destFileName);
        destFile.getParentFile().mkdirs();
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            destFile = null;
        }
        return destFile;
    }
}
