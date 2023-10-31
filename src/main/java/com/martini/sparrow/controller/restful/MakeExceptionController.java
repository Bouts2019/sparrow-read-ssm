package com.martini.sparrow.controller.restful;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author martini at 2020/12/15 9:58
 */
@RestController
@RequestMapping("/test")
public class MakeExceptionController {
    @GetMapping("/exception")
    public Object makeException() {
        int i = 1 / 0;
        return null;
    }
}
