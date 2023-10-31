package com.martini.sparrow.controller.restful;

import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.ssh.Sftp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author martini at 2020/12/17 7:37
 */
@CrossOrigin
@RestController
@Slf4j
@RequestMapping("/editor")
public class EditorController {
    /**
     * 上传图片接口
     * @param res 上传文件
     * @return 路径
     */
    @PostMapping("/uploadPic")
    public Object uploadPic(@RequestParam("image") MultipartFile res) {
        // TODO: 路径需要外部properties注入
        // TODO: SFTP -> FTP
        // TODO: upload 路径
        Sftp sftp = new Sftp("192.168.1.100", 22, "bouts", "120309");
        String name = IdUtil.fastSimpleUUID();
        try {
            File file = new File(name);
            res.transferTo(file);
            System.out.println(sftp.upload("/public/upload_images", file));
            file.delete();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            sftp.close();
        }
        return "http://192.168.1.100/" + name;
    }
}
