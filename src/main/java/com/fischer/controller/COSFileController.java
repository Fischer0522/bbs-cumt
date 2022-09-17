package com.fischer.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import com.fischer.exception.BizException;
import com.fischer.pojo.FileBO;
import com.fischer.result.CommonResult;
import com.fischer.result.ResponseResult;
import com.fischer.service.COSFileService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@ResponseResult
@RequestMapping("api/file")
@RestController
@ConfigurationProperties(prefix = "spring.tengxun")
@Setter
public class COSFileController {

    @Autowired
    private COSFileService cosFileService;

    private String[] fileType;
    private Set<String> allowType;
    @SaCheckLogin
    @PostMapping
    public ResponseEntity<FileBO> uploadFile(MultipartFile file) {
        allowType = Arrays.stream(fileType).collect(Collectors.toSet());
        int begin = file.getOriginalFilename().indexOf(".");
        int last = file.getOriginalFilename().length();
//获得文件后缀名
        String suffix  = file.getOriginalFilename().substring(begin, last).toLowerCase(Locale.ROOT);
        System.out.println(suffix);
        if (!allowType.contains(suffix)) {
            throw new BizException(400,"当前文件格式不支持，请上传以"+ Arrays.toString(fileType) +"结尾的文件");
        }

        FileBO upload = cosFileService.upload(file);
        return ResponseEntity.ok(upload);
    }
}
