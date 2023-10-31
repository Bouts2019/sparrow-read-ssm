package com.martini.sparrow.controller.restful;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.RandomUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.martini.sparrow.util.EmailUtils;
import io.swagger.annotations.Api;
import org.apache.lucene.util.fst.UpToTwoPositiveIntOutputs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * @author martini at 2020/12/21 19:37
 */
@RestController
@RequestMapping("/captcha")
@CrossOrigin
@Api("验证码接口")
public class CaptchaController {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/{id}")
    public Object sendCaptcha(@PathVariable("id") Long id, @RequestBody Map<String, Object> map) {
        String emailAddress = (String)map.get("email");
        System.out.println(emailAddress);
        String placeholder = "[link]";
        int captcha = RandomUtil.randomInt(100000, 999999);
        Map<String, Object> captchaMap = new HashMap<>();
        captchaMap.put("captcha", captcha);
        captchaMap.put("timestamp", System.currentTimeMillis());
        try {
            redisTemplate.opsForHash().put("captchas", id.toString(), objectMapper.writeValueAsString(captchaMap));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String content = "<p>欢迎使用，验证码为<p><h2>" + captcha + "</h2>";
        EmailUtils.sendVerificationEmail(new String[] {emailAddress}, "麻雀阅读 - 注册验证", content, placeholder, "");
        return null;
    }

    @PutMapping("/{id}")
    public Object verifyCaptcha(@PathVariable("id") Long id, @RequestBody Map<String, Object> map, HttpServletResponse response) {
        String captcha = (String)map.get("captcha");
        // redisTemplate.opsForHash().put("captchas", id, objectMapper.writeValueAsString(captchaMap));
        String captchaJson = (String)redisTemplate.opsForHash().get("captchas", id.toString());
        System.out.println(captchaJson);
        try {
            Map jsonMap = objectMapper.readValue(captchaJson, Map.class);
            Long cacheCaptcha = Long.parseLong(jsonMap.get("captcha").toString());
            Long cacheTimestamp = Long.parseLong(jsonMap.get("timestamp").toString());
            if (!Objects.equals(Long.parseLong(captcha), cacheCaptcha)) {
                response.setStatus(402);
                return null;
            }
            if (System.currentTimeMillis() - cacheTimestamp > 5 * 60 * 1000) {
                response.setStatus(407);
                return null;
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        // System.out.println("captcha: ");
        // System.out.println(captcha);
        return null;
    }
    @GetMapping(value = "/get")
    public Object getCaptcha(HttpServletResponse response) throws IOException {
        response.setContentType("image/png;charset=utf-8");
//定义图形验证码的长和宽
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(120, 38);

//图形验证码写出，可以写出到文件，也可以写出到流
        lineCaptcha.write(response.getOutputStream());
//输出code
        System.out.println(lineCaptcha.getCode());
//验证图形验证码的有效性，返回boolean值
        lineCaptcha.verify("1234");

//重新生成验证码
        lineCaptcha.createCode();
        lineCaptcha.write("d:/line.png");
//新的验证码
        System.out.println(lineCaptcha.getCode());
//验证图形验证码的有效性，返回boolean值
        lineCaptcha.verify("1234");
        return null;
    }
}
