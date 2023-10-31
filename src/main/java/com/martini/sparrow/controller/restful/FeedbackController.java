package com.martini.sparrow.controller.restful;

import com.martini.sparrow.entity.Feedback;
import com.martini.sparrow.mapper.FeedbackMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author martini at 2021/1/13 14:09
 */
@CrossOrigin
@RestController
@Slf4j
@RequestMapping("/feedback")
public class FeedbackController {
    @Autowired
    private FeedbackMapper feedbackMapper;
    @PostMapping("")
    public Object postFeedback(@RequestBody Feedback feedback) {
        feedback.setDatetime(new Date());
        return feedbackMapper.postOne(feedback);
    }

    @GetMapping("/all")
    public Object findAll() {
        return feedbackMapper.findAll();
    }
}
