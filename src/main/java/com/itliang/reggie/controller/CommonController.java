package com.itliang.reggie.controller;

import com.itliang.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;


/**
 * 文件上传和下载
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) throws IOException {
        log.info(file.toString());
        //获取原始文件名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //防止名称重复造成的文件覆盖
        String filename = UUID.randomUUID().toString() + suffix;
        //判断目录是否存在
        File dir = new File(basePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        //file是一个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会删除
        file.transferTo(new File(basePath + filename));
        return R.success(filename);
    }

    /**
     * 文件下载
     * @param name
     * @param httpServletResponse
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse httpServletResponse) throws IOException {
        //输入流 通过输入流读取文件内容
        FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
        //输出流 通过输出流将文件写回浏览器，在浏览器展示图片了
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();

        httpServletResponse.setContentType("/image/jpeg"    );

        byte[] bytes = new byte[1024];
        int len = 0;
        while((len = fileInputStream.read(bytes))!=-1){
            outputStream.write(bytes,0,len);
            outputStream.flush();
        }
        outputStream.close();
        fileInputStream.close();
    }
}
