package com.martini.sparrow.controller.restful;

import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.ssh.Sftp;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author martini at 2020/12/19 8:09
 */
@CrossOrigin
@RestController
@Slf4j
@Api("首页的Banner对象")
@RequestMapping("/banner")
public class BannerController {
    // @Autowired
    // private BannerService bannerService;
    // @GetMapping("/all")
    // public Object findAll() {
    //     return bannerService.findAll();
    // }

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @GetMapping("/all")
    public Object findAll() {
        List banners = redisTemplate.opsForList().range("banners", 0, -1);
        List<Map> res = new ArrayList<>();
        for (Object banner : banners) {
            try {
                res.add(objectMapper.readValue((String) banner, Map.class));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    @PostMapping("/upload-image")
    public Object uploadImage(@RequestParam("image") MultipartFile image, HttpServletResponse response) {
        // TODO: 路径需要外部properties注入
        // TODO: SFTP -> FTP
        // TODO: upload 路径
        Sftp sftp = new Sftp("192.168.1.100", 22, "bouts", "120309");
        String name = IdUtil.fastSimpleUUID();
        try {
            File file = new File(name);
            image.transferTo(file);
            System.out.println(sftp.upload("/public/upload_images/banners", file));
            file.delete();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            sftp.close();
        }
        return "http://192.168.1.100/banners/" + name;
    }

    @PostMapping("/one")
    public Object addOne(@RequestBody Map<String, String> bannerInfo, HttpServletResponse response) {
        if (bannerInfo.get("imgSrc") == null || bannerInfo.get("linkSrc") == null) {
            throw new RuntimeException("banner值不能为空");
        }
        try {
            redisTemplate.opsForList().rightPush("banners", objectMapper.writeValueAsString(bannerInfo));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @DeleteMapping("/{index}")
    public Object deleteById(@PathVariable("index") Integer index, HttpServletResponse response) {
        try {
            redisTemplate.opsForList().remove("banners", 0, redisTemplate.opsForList().index("banners", index));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
