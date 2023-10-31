package com.martini.sparrow.controller.restful;

import com.martini.sparrow.service.UserBookFinishTimeService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * @author martini at 2020/12/20 10:29
 */
@RestController
@Slf4j
@CrossOrigin
@RequestMapping("/ubft")
public class UserBookFinishTimeController {
    @Autowired
    private UserBookFinishTimeService userBookFinishTimeService;
    @GetMapping("/u/{id}")
    public Object findByUserId(@PathVariable("id") Long uid) {
        return userBookFinishTimeService.findByUserId(uid);
    }

    @PostMapping("/read")
    public Object addReadBook(@RequestBody Map<String, String> form, HttpServletResponse response) {
        Long uid = Long.parseLong(form.get("uid"));
        Long bid = Long.parseLong(form.get("bid"));
        if (uid == null || bid == null) {
            response.setStatus(402);
            throw new RuntimeException("参数错误");
        }
        userBookFinishTimeService.insertOne(bid, uid, new Date());
        return null;
    }
}
