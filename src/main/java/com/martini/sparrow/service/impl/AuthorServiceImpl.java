package com.martini.sparrow.service.impl;

import com.martini.sparrow.entity.Author;
import com.martini.sparrow.mapper.AuthorMapper;
import com.martini.sparrow.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author martini at 2020/12/16 9:35
 */
@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {
    @Autowired
    private AuthorMapper authorMapper;
    @Override
    public List<Author> findAll() {
        return authorMapper.findAll();
    }

    @Override
    public Object findById(Long id) {
        return authorMapper.findById(id);
    }
}
