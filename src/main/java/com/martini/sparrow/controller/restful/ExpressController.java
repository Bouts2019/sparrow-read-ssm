package com.martini.sparrow.controller.restful;

import com.martini.sparrow.service.ExpressService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author martini at 2020/12/16 9:48
 */
@CrossOrigin
@RestController
@Slf4j
@Api("出版社信息")
@RequestMapping("/express")
public class ExpressController {
    @Autowired
    private ExpressService expressService;

    @GetMapping("/all")
    public Object findAll() {
        return expressService.findAll();
    }

    @GetMapping("/{id}")
    public Object findById(@PathVariable("id") Long id) {
        return expressService.findById(id);
    }
}
