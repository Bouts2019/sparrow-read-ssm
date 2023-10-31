package com.martini.sparrow.controller.restful;

import com.alibaba.druid.util.StringUtils;
import com.martini.sparrow.entity.Admin;
import com.martini.sparrow.exception.AdminException;
import com.martini.sparrow.service.AdminService;
import com.martini.sparrow.util.JwtUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author martini at 2020/12/15 20:27
 */
@RestController
@Slf4j
@CrossOrigin
@RequestMapping("/admin")
@Api("管理员控制器")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @PostMapping("/login")
    public Object login(@RequestBody Map<String, String> form, HttpServletResponse response) {
        String jobNumber = form.get("jobNumber");
        String password = form.get("password");
        if (StringUtils.isEmpty(jobNumber)) {
            response.setStatus(401);
            throw new AdminException("参数非法");
        }
        Admin admin = adminService.findByJobNumber(jobNumber);
        if (admin == null) {
            response.setStatus(402);
            throw new AdminException("未知工号");
        }
        if (!Objects.equals(admin.getPassword(), password)) {
            response.setStatus(403);
            throw new AdminException("密码错误");
        }
        Map<String, String> payload = new HashMap<>();
        payload.put("jobNumber", admin.getJobNumber());
        payload.put("name", admin.getName());
        return JwtUtils.sign(admin.getPassword(), payload);
    }
}
