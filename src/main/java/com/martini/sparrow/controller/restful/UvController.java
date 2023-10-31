package com.martini.sparrow.controller.restful;

import com.martini.sparrow.service.PageViewService;
import com.martini.sparrow.service.UserViewService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author martini at 2021/1/13 8:32
 */
@Slf4j
@RestController
@RequestMapping("/uv")
@Api("user view")
@CrossOrigin
public class UvController {
    @Autowired
    private UserViewService userViewService;
    /**
     * 连续一周的访问量
     */
    @GetMapping("/week")
    public Object getWeekData() {
        return userViewService.getWeekData();
    }
    @GetMapping("/half-of-month")
    public Object halfOfMonth() {
        return userViewService.getHalfOfMonthData();
    }
    @GetMapping("/month")
    public Object month() {
        return userViewService.getMonthData();
    }
    @GetMapping("/")
    public Object addPageView() {
        System.out.println("enter controller");
        userViewService.add();
        return null;
    }
}
