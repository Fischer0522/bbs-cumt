package com.fischer.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import com.fischer.exception.BizException;
import com.fischer.exception.ExceptionStatus;
import com.fischer.pojo.FileBO;
import com.fischer.service.COSFileService;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.CannedAccessControlList;
import com.qcloud.cos.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fischer
 * @description 腾讯云COS(对象存储中心)服务实现类
 * @date 2021/9/10 15:43
 **/
@Service
public class COSFileServiceImpl implements COSFileService {


    @Value("${spring.tengxun.secretId}")
    private String secretId;
    @Value("${spring.tengxun.secretKey}")
    private String secretKey;

    @Value("${spring.tengxun.region}")
    private String region;

    @Value("${spring.tengxun.bucketName}")
    private String bucketName;

    @Value("${spring.tengxun.url}")
    private String path;

    @Autowired
    public COSClient cosClient;

    @Override
    public FileBO upload(MultipartFile file) {

        try {
            String originalfileName = file.getOriginalFilename();

            // 获得文件流
            InputStream inputStream = file.getInputStream();

            //设置文件key
            String filePath = getFileKey(originalfileName);

            // 上传文件
            cosClient.putObject(new PutObjectRequest(bucketName, filePath, inputStream, null));
            cosClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
            String url = path + "/" + filePath;
            Map<String, String> map = new HashMap<>();
            map.put("fileName", originalfileName);
            map.put("url", url);
            return new FileBO(originalfileName,url);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(ExceptionStatus.ERROR_FILE);
        } finally {
            cosClient.shutdown();
        }
    }

    /**
     * 生成文件路径
     *
     * @return
     */
    private String getFileKey(String originalfileName) {
        String filePath = "rgzn-icu/";
        //1.获取后缀名 2.去除文件后缀 替换所有特殊字符
        String fileType = originalfileName.substring(originalfileName.lastIndexOf("."));
        String fileStr = StrUtil.removeSuffix(originalfileName, fileType).replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5]", "_");
        filePath +=  new DateTime().toString("yyyyMMddHHmmss") + "_" + fileStr + fileType;
        return filePath;
    }
}

