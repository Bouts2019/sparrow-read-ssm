package com.martini.sparrow.service;

import com.martini.sparrow.entity.Author;

import java.util.List;

/**
 * @author martini at 2020/12/16 9:34
 */
public interface AuthorService {
    List<Author> findAll();

    Object findById(Long id);
}
