package com.baidu.controller;

import com.baidu.utils.R;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class UploadController {
    //处理文件上传的方法
    @RequestMapping("/upload")
    @ResponseBody
    public R myUpload(MultipartFile file, HttpSession
            session){
        System.out.println("执行文件上传：file="+file);
        System.out.println("上传文件的名称："+file.getOriginalFilename());
        //获取原始文件名
        String name = file.getOriginalFilename();
        // 获取当前工作空间路径 (D:\project\IdeaProjects\gbs12)
        String root = System.getProperty("user.dir");
        System.out.println("root:"+root);
        // 【路径 1】：源码目录 (保证 clean 后不丢失，能长久保存)
        String srcPath = root + "/shop-parent/shop-web/src/main/resources/static/img/";
        // 【路径 2】：编译目录 (保证上传后不用重启，前端页面能立刻显示图片)
        String path = root+"/shop-parent/shop-web/target/classes/static/img/";
        // 如果目录不存在，自动创建（用 mkdirs() 支持多级目录创建）
        new File(srcPath).mkdirs();
        File file2 = new File(path);

        if(!file2.exists()) {//目录不存在
            file2.mkdir();
        }
        String realName = UUID.randomUUID()+name;
        System.out.println("path:"+path);
        System.out.println("realName:"+realName);
        File srcFile = new File(srcPath,realName);
        File realFile=new File(file2,realName);

        try {
            // 1. 先将用户上传的文件保存到 src 源码目录中永存
            // 注意：MultipartFile 的 transferTo 方法一次请求只能调用一次！
            file.transferTo(srcFile);
            // 2. 然后利用 Java NIO 的 Files.copy 方法，将刚才存好的图片神不知鬼不觉地复制一份到 target 里
            java.nio.file.Files.copy(srcFile.toPath(), realFile.toPath(), java.nio.file.StandardCopyOption.
                    REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        R r=R.right();
        r.put("data","/img/"+realName);
        return r;
    }
}
