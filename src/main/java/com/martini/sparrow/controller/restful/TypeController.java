package com.martini.sparrow.controller.restful;

import com.martini.sparrow.service.TypeService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author martini at 2020/12/16 9:59
 */
@RestController
@RequestMapping("/type")
@CrossOrigin
@Slf4j
@Api("图书类型")
public class TypeController {
    @Autowired
    private TypeService typeService;
    @GetMapping("/all")
    public Object findAll() {
        return typeService.findAll();
    }
    @GetMapping("/{id}")
    public Object findById(@PathVariable("id") Long id) {
        return typeService.findById(id);
    }
}
