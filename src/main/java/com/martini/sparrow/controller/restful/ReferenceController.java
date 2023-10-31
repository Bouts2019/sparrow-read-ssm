package com.martini.sparrow.controller.restful;

import com.martini.sparrow.service.ReferenceService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author martini at 2020/12/22 9:00
 */
@RestController
@RequestMapping("/ref")
@Slf4j
@Api("网页引用统计")
@CrossOrigin
public class ReferenceController {
    @Autowired
    private ReferenceService referenceService;
    @GetMapping("/all")
    public Object getRefData() {
        return referenceService.getRefData();
    }
}
