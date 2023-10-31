package com.martini.sparrow.mapper;

import com.martini.sparrow.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author martini at 2020/12/15 8:12
 */
@Repository
@Mapper
public interface ArticleMapper {
    List<Article> findAll();
    Article findById(Long id);
    Integer insertOne(Article article);

    /**
     * 通过id更新字段
     * @param id id
     * @param article 文章
     * @return 受影响的行数
     */
    Integer updateById(@Param("id") Long id, @Param("article") Article article);

    Integer count();

    List<Article> findLimit(@Param("startIndex") Integer startIndex, @Param("size") Integer size);

    List<Article> findByUserId(Long uid);

    int cancelLikeArticle(long aid);

    int likeArticle(long aid);

    Integer approvedCount();

    int addReadCount(@Param("id") Long id, @Param("number") int randomInt);

    int addCommentCount(@Param("id") Long articleId);
}
