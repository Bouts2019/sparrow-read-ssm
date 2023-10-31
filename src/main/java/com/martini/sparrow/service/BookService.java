package com.martini.sparrow.service;

import com.martini.sparrow.entity.Book;

import java.util.List;

/**
 * @author martini at 2020/12/14 20:46
 */
public interface BookService {
    int addOne(Book book);
    List<Book> findAll();

    Object findById(Long id);

    Integer count();

    List<Book> findLimit(Integer page, Integer limit);

    int updateById(Book book);

    int deleteById(Long id);

    int deleteByIdIn(List<Long> ids);

    Book findByTitleLike(String title);
}
