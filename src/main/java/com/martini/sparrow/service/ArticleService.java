package com.martini.sparrow.service;

import com.martini.sparrow.entity.Article;
import org.apache.ibatis.annotations.Param;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * @author martini at 2020/12/15 8:13
 */
public interface ArticleService {
    List<Article> findAll();
    Integer postOne(Article article);
    Article findById(Long id);
    /**
     *
     * @param id 书本id
     * @param article 文章
     * @return 定义返回的状态常量
     */
    Integer updateById(Long id, Article article);

    /**
     * 文章通过审核
     * @param id 文章id
     * @return 操作ok与否
     */
    Boolean approve(Long id);

    Integer count();

    List<Article> findLimit(Integer page, Integer limit);

    Boolean disapprove(Long id);

    List<Article> findByUserId(Long uid);

    int cancelLikeArticle(long aid);

    int likeArticle(long aid);

    Integer approvedCount();

    int addReadCount(Long id, int randomInt);

    int addCommentCount(Long articleId);
}
