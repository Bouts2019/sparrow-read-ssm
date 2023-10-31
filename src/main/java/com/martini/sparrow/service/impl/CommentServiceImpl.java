package com.martini.sparrow.service.impl;

import com.martini.sparrow.entity.Comment;
import com.martini.sparrow.mapper.CommentMapper;
import com.martini.sparrow.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author martini at 2021/1/7 17:01
 */
@Service
@Transactional
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Override
    public List<Comment> findAllByArticleId(Long articleId) {
        return commentMapper.findAllByArticleId(articleId);
    }

    @Override
    public int addOne(Comment comment) {
        return commentMapper.addOne(comment);
    }
}
