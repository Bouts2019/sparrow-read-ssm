package com.martini.sparrow.controller.restful;

import com.martini.sparrow.service.AuthorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author martini at 2020/12/16 9:36
 */
@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/author")
public class AuthorController {
    @Autowired
    private AuthorService authorService;
    @GetMapping("/all")
    public Object findAll() {
        return authorService.findAll();
    }

    @GetMapping("/{id}")
    public Object findById(@PathVariable("id") Long id) {
        return authorService.findById(id);
    }
}
