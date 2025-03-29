package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.UUID;

/**
 * @description: 一个通用接口
 * @author: Excell
 * @data 2025年03月19日 20:38
 */
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;

    /**
     * @description: 上传文件
     * @title: uploadFile
     * @param: [file]
     */
    @ApiOperation("上传文件")
    @PostMapping("/upload")
    public Result<String> uploadFile(MultipartFile file) {
        // 给传输过来的文件起一个新的文件名
        String fileNamePre = UUID.randomUUID().toString();
        String fileNext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String newFileName = fileNamePre + fileNext;

        try {
            String uploadUrl = aliOssUtil.upload(file.getBytes(), newFileName);
            return Result.success(uploadUrl);
        } catch (IOException e) {
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }
    }
}
