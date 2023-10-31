package com.martini.sparrow.service.impl;

import com.martini.sparrow.entity.Book;
import com.martini.sparrow.mapper.BookMapper;
import com.martini.sparrow.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author martini at 2020/12/14 20:48
 */
@Service
@Transactional
public class BookServiceImpl implements BookService {
    @Autowired
    private BookMapper bookMapper;
    @Override
    public int addOne(Book book) {
        book.setStars1(0)
            .setStars2(0)
            .setStars3(0)
            .setStars4(0)
            .setStars5(0);
        return bookMapper.addOne(book);
    }

    @Override
    public List<Book> findAll() {
        return bookMapper.findAll();
    }

    @Override
    public Object findById(Long id) {
        return bookMapper.findById(id);
    }

    @Override
    public Integer count() {
        return bookMapper.count();
    }

    @Override
    public List<Book> findLimit(Integer page, Integer limit) {
        int startIndex = (page - 1) * limit;
        int size = limit;
       return bookMapper.findLimit(startIndex, size);
    }

    @Override
    public int updateById(Book book) {
        return bookMapper.updateById(book);
    }

    @Override
    public int deleteById(Long id) {
        return bookMapper.deleteById(id);
    }

    @Override
    public int deleteByIdIn(List<Long> ids) {
        return bookMapper.deleteByIdIn(ids);
    }

    @Override
    public Book findByTitleLike(String title) {
        return bookMapper.findByTitleLike(title);
    }
}
