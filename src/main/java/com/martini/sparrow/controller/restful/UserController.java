package com.martini.sparrow.controller.restful;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.ssh.Sftp;
import com.alibaba.druid.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.martini.sparrow.entity.User;
import com.martini.sparrow.exception.UserException;
import com.martini.sparrow.service.UserService;
import com.martini.sparrow.util.JwtUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author martini at 2020/12/14 8:29
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
@Api("用户控制")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public Object findAll() {
        return userService.findAll();
    }

    @GetMapping("/limit")
    public Object findLimit(@RequestParam  Map<String, String> form, HttpServletResponse response) {
        // 检查参数
        if (form.get("page") == null || form.get("limit") == null) {
            // TODO: 401
            response.setStatus(401);
            return null;
        }
        Integer page = Integer.parseInt(form.get("page"));
        Integer limit = Integer.parseInt(form.get("limit"));
        return userService.findLimit(page, limit);
    }

    @GetMapping("/home")
    public Object home() {
        return "This is home page";
    }

    /**
     * 通过账号密码登录（非手机号/验证码方式）
     * @param json 前端payload中包含的数据
     * @param response 响应体，设置响应头使用
     * @return 统一响应结果，如果成功data中为token
     */
    @PostMapping(value = "/login-account", consumes = "application/json;charset=utf-8")
    public Object login(@RequestBody Map<String, String> json, HttpServletResponse response) {
        String account = json.get("account");
        String password = json.get("password");
        if (StringUtils.isEmpty(account)) {
            response.setStatus(401);
            throw new UserException("参数非法");
        }
        User user = account.contains("@") ? userService.findByEmail(account) : userService.findByPhone(account);
        if (user == null) {
            response.setStatus(402);
            throw new UserException("未知的用户名");
        }
        if (!Objects.equals(user.getPassword(), password)) {
            response.setStatus(403);
            throw new UserException("密码错误");
        }
        Map<String, String> payload = new HashMap<>();
        payload.put("avatar", user.getAvatar());
        payload.put("nickname", user.getNickname());
        payload.put("uid", String.valueOf(user.getUid()));
        return JwtUtils.sign(password, payload);
    }

    @PostMapping("/register")
    public Object register(@RequestBody Map<String, String> form, HttpServletResponse response) {
        // TODO: 后端校验
        // Controller只做数据转换
        // 即从form表单中提取有用的信息
        String phone = form.get("account");
        String password = form.get("password");
        String nickname = form.get("nickname");
        int state = userService.register(phone, password, nickname);
        // TODO: 定义0为ok
        // TODO: 错误永远是4xx
        response.setStatus(state);
        return null;
    }

    // TODO: 返回密码安全问题
    @GetMapping("/{id}")
    public Object findById(@PathVariable("id") Long id) {
        return userService.findById(id);
    }

    @PutMapping("/{id}")
    public Object updateById(@PathVariable("id") Long id, @RequestBody User user) {
        user.setUid(id);
        return userService.updateById(user);
    }

    /**
     * 获取安全账号信息（无密码）
     * @param id
     * @return
     */
    @GetMapping("/sec/{id}")
    public Object findByIdSec(@PathVariable("id") Long id) {
        User user = userService.findById(id);
        return user.setPassword(null);
    }

    /**
     * 获取用户总数
     * @return 用户总数
     */
    @GetMapping("/count")
    public Object getCount() {
        return userService.count();
    }

    /**
     * 获取id对应用户的文章数量
     * @param id 用户id
     * @return 数量
     */
    @GetMapping("/{id}/count/article")
    public Object getArticleCountById(@PathVariable("id") Long id) {
        return userService.getArticleCountById(id);
    }

    /**
     * 根据id数组查找用户
     * @param ids id列表
     * @return 用户列表
     */
    @GetMapping("/ids")
    public Object findUsersByIds(@RequestParam("ids") List<Long> ids) {
        return userService.findUsersByIds(ids);
    }

    @PostMapping("/avatar")
    public Object uploadAvatar(@RequestParam("file") MultipartFile uploadFiles) {
        // TODO: 路径需要外部properties注入
        // TODO: SFTP -> FTP
        // TODO: upload 路径
        Sftp sftp = new Sftp("192.168.1.100", 22, "bouts", "120309");
        String name = IdUtil.fastSimpleUUID();
        try {
            File file = new File(name);
            uploadFiles.transferTo(file);
            System.out.println(sftp.upload("/public/upload_images/avatars", file));
            file.delete();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            sftp.close();
        }
        return "http://192.168.1.100/avatars/" + name;
    }
}
