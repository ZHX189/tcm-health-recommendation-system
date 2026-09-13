package com.tcm.common.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.tcm.common.constant.CommonConstant;
import com.tcm.common.exception.BusinessException;
import com.tcm.common.result.ResultCode;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

/**
 * 文件上传工具类
 *
 * @author Ti
 * @since 2026-02-03
 */
public class FileUploadUtil {

    /**
     * 上传图片
     *
     * @param file       文件
     * @param uploadPath 上传路径
     * @return 文件访问路径
     */
    public static String uploadImage(MultipartFile file, String uploadPath) {
        // 校验文件
        validateImage(file);

        // 生成文件名
        String originalFilename = file.getOriginalFilename();
        String extension = FileUtil.extName(originalFilename);
        String fileName = IdUtil.simpleUUID() + "." + extension;

        // 生成日期目录
        String datePath = DateUtil.format(new Date(), "yyyy/MM/dd");
        String relativePath = datePath + "/" + fileName;
        
        // 获取绝对路径（解决相对路径导致的上传失败问题）
        File uploadDir = new File(uploadPath);
        if (!uploadDir.isAbsolute()) {
            uploadDir = new File(System.getProperty("user.dir"), uploadPath);
        }
        String fullPath = uploadDir.getAbsolutePath() + File.separator + relativePath.replace("/", File.separator);

        // 创建目录
        File destFile = new File(fullPath);
        FileUtil.mkParentDirs(destFile);

        // 保存文件
        try {
            file.transferTo(destFile.getAbsoluteFile());
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(ResultCode.FILE_UPLOAD_ERROR, "文件保存失败: " + e.getMessage());
        }

        return "/uploads/" + relativePath;
    }

    /**
     * 校验图片
     */
    public static void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ResultCode.FILE_UPLOAD_ERROR, "请选择要上传的文件");
        }

        // 校验文件大小
        if (file.getSize() > CommonConstant.MAX_FILE_SIZE) {
            throw new BusinessException(ResultCode.FILE_SIZE_ERROR, "文件大小不能超过10MB");
        }

        // 校验文件类型
        String contentType = file.getContentType();
        boolean isAllowed = Arrays.stream(CommonConstant.ALLOWED_IMAGE_TYPES)
                .anyMatch(type -> type.equalsIgnoreCase(contentType));
        if (!isAllowed) {
            throw new BusinessException(ResultCode.FILE_TYPE_ERROR, "只支持jpg、png、gif、webp格式的图片");
        }
    }

    /**
     * 删除文件
     *
     * @param filePath   文件路径
     * @param uploadPath 上传根路径
     */
    public static void deleteFile(String filePath, String uploadPath) {
        if (StrUtil.isBlank(filePath)) {
            return;
        }
        // 去掉/uploads/前缀
        String relativePath = filePath.replace("/uploads/", "");
        String fullPath = uploadPath + "/" + relativePath;
        FileUtil.del(fullPath);
    }
}
