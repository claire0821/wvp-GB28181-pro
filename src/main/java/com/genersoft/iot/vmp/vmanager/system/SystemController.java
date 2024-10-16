package com.genersoft.iot.vmp.vmanager.system;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

@SuppressWarnings("rawtypes")
@Tag(name = "服务控制")
@RestController
@RequestMapping("/api/system")

public class SystemController {
    private final static Logger logger = LoggerFactory.getLogger(SystemController.class);
    @PostMapping("/imgUpload")
    public String uploadImg(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        logger.info("图片上传事件触发");
        StringBuffer url = new StringBuffer();
        String filePath = "/file";//自定义图片上传路劲，解析字符串
        String imgFolderPath = request.getServletContext().getRealPath(filePath);
        File imgFolder = new File(imgFolderPath);
        if (!imgFolder.exists()) {
            imgFolder.mkdirs();
        }
        url.append(request.getScheme())
                .append("://")
                .append(request.getServerName())
                .append(":")
                .append(request.getServerPort())
                .append(request.getContextPath())
                .append(filePath);
        String imgName = System.currentTimeMillis() +"_"+ file.getOriginalFilename().replaceAll(" ", "");
        try {
            file.transferTo(new File(imgFolder, imgName));
            return url.append("/").append(imgName).toString();
        } catch (IOException e) {
            return "上传失败";
        }
    }
}
