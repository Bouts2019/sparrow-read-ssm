package com.martini.sparrow.mapper;

import com.martini.sparrow.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author martini at 2021/1/7 16:58
 */
@Repository
@Mapper
public interface CommentMapper {
    List<Comment> findAllByArticleId(Long articleId);

    int addOne(Comment comment);
}
