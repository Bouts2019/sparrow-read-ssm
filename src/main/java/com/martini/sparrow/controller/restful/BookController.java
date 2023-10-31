package com.martini.sparrow.controller.restful;

import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.ssh.Sftp;
import com.martini.sparrow.entity.Book;
import com.martini.sparrow.exception.BookException;
import com.martini.sparrow.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 图书的增删改查，基本信息
 * @author martini at 2020/12/14 17:48
 */
@RestController
@Slf4j
@RequestMapping("/book")
@CrossOrigin
public class BookController {
    @Autowired
    private BookService bookService;

    @PostMapping("/one")
    public Object addOne(@RequestBody Book book) {
        int updated = bookService.addOne(book);

        if (updated == 0) throw new BookException("添加异常");
        return updated;
    }

    @GetMapping("/all")
    public Object findAll() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public Object findById(@PathVariable("id") Long id) {
        return bookService.findById(id);
    }
    @PutMapping("/{id}")
    public Object updateById(@RequestBody Book book) {
        return bookService.updateById(book);
    }
    @DeleteMapping("/{id}")
    public Object deleteById(@PathVariable("id") Long id) {
        return bookService.deleteById(id);
    }
    @DeleteMapping("/ids")
    public Object deleteByIdIn(@RequestBody List<Long> ids) {
        return bookService.deleteByIdIn(ids);
    }

    @GetMapping("/count")
    public Object getCount() {
        return bookService.count();
    }

    @GetMapping("/limit")
    public Object findLimit(@RequestParam Map<String, String> params, HttpServletResponse response) {
        // 检查参数
        if (params.get("page") == null || params.get("limit") == null) {
            // TODO: 401
            response.setStatus(401);
            return null;
        }
        Integer page = Integer.parseInt(params.get("page"));
        Integer limit = Integer.parseInt(params.get("limit"));
        return bookService.findLimit(page, limit);
    }
    @PostMapping("/upload-cover")
    public Object uploadCover(@RequestParam("image")MultipartFile uploadFiles) {
        // TODO: 路径需要外部properties注入
        // TODO: SFTP -> FTP
        // TODO: upload 路径
        Sftp sftp = new Sftp("192.168.1.100", 22, "bouts", "120309");
        String name = IdUtil.fastSimpleUUID();
        try {
            File file = new File(name);
            uploadFiles.transferTo(file);
            System.out.println(sftp.upload("/public/upload_images/book_covers", file));
            file.delete();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            sftp.close();
        }
        return "http://192.168.1.100/book_covers/" + name;
    }

    @GetMapping("/title-like")
    public Object findByTitleLike(@RequestParam("title") String title) {
        return bookService.findByTitleLike(title);
    }
}
