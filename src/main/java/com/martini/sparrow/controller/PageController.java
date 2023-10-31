package com.martini.sparrow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author martini at 2020/12/14 9:38
 */
@Controller
public class PageController {
    @GetMapping("/login")
    public String toLoginPage() {
        return "login";
    }
}
