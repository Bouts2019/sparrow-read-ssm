package com.martini.sparrow.controller.restful;

import com.martini.sparrow.service.PageViewService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * PV(Page View) 页面访问量控制器
 * @author martini at 2020/12/21 21:13
 */
@Slf4j
@RestController
@RequestMapping("/pv")
@Api("page view")
@CrossOrigin
public class PvController {
    @Autowired
    private PageViewService pageViewService;
    /**
     * 连续一周的访问量
     */
    @GetMapping("/week")
    public Object getWeekData() {
        return pageViewService.getWeekData();
    }
    @GetMapping("/half-of-month")
    public Object halfOfMonth() {
        return pageViewService.getHalfOfMonthData();
    }
    @GetMapping("/month")
    public Object month() {
        return pageViewService.getMonthData();
    }
    @GetMapping("/")
    public Object addPageView() {
        System.out.println("enter controller");
        pageViewService.add();
        return null;
    }
}
