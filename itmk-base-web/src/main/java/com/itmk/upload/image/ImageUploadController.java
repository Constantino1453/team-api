package com.itmk.upload.image;

import com.itmk.utils.ResultUtils;
import com.itmk.utils.ResultVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

/**
 * @Author java实战基地
 * @Version 2383404558
 */
@RequestMapping("/api/upload")
@RestController
public class ImageUploadController {
    @Value("${web.uploadpath}")
    private String webUploadpath;

    @RequestMapping("/uploadImage")
    public ResultVo uploadImage(@RequestParam("file")MultipartFile file){
        String Url = "";
        String fileName = file.getOriginalFilename();
        //扩展名 png  jpg
        String fileExtenionName = fileName.substring(fileName.indexOf("."));
        //重新生成新的文件名  4564646.png
        String newName = UUID.randomUUID().toString() + fileExtenionName;
        //图片上传路径
        String path = webUploadpath;
        File fileDir = new File(path);
        if(!fileDir.exists()){
            fileDir.mkdirs();
            //设置权限
            fileDir.setWritable(true);
        }
        File targetFile = new File(path, newName);
        try{
            file.transferTo(targetFile);
            Url = "/" + targetFile.getName();
        }catch (Exception e){
            return  null;
        }
        return ResultUtils.success("上传成功","/images"+Url);
    }
}
