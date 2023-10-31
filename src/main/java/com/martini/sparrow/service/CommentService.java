package com.martini.sparrow.service;

import com.martini.sparrow.entity.Comment;

import java.util.List;

/**
 * @author martini at 2021/1/7 17:01
 */
public interface CommentService {
    List<Comment> findAllByArticleId(Long articleId);

    int addOne(Comment comment);
}
