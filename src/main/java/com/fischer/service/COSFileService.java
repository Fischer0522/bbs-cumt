package com.fischer.service;

import com.fischer.pojo.FileBO;
import com.fischer.result.CommonResult;
import com.fischer.result.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

public interface COSFileService {
    FileBO upload(MultipartFile file);
}
