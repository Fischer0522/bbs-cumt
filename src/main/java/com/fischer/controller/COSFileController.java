package com.fischer.controller;


import com.fischer.pojo.FileBO;
import com.fischer.result.CommonResult;
import com.fischer.result.ResponseResult;
import com.fischer.service.COSFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@ResponseResult
@RequestMapping("file")
@RestController
public class COSFileController {

    @Autowired
    private COSFileService cosFileService;

    @PostMapping
    public ResponseEntity<FileBO> uploadFile(MultipartFile file) {
        FileBO upload = cosFileService.upload(file);
        return ResponseEntity.ok(upload);
    }
}
