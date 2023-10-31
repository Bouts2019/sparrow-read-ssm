package com.martini.sparrow.controller.restful;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Properties;

/**
 * @author martini at 2020/12/22 9:33
 */
@RestController
@Slf4j
@CrossOrigin
@RequestMapping("/info")
@Api("获取服务器信息")
public class ServerInfoController {
    @GetMapping("/all")
    public Object getAllInfos() {
        Properties properties = System.getProperties();
        System.out.println(properties);
        return properties;
    }

}
