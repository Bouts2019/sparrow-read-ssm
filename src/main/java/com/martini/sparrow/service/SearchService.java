package com.martini.sparrow.service;

import com.martini.sparrow.entity.Article;

import java.util.List;

/**
 * TODO: 前端根据不同的界面搜索不同的东西，指定文章搜索文章，指定用户搜索用户，诸如此类
 * @author martini at 2020/12/15 16:36
 */
public interface SearchService {
    // List<Article> searchArticle();
    List<Object> searchArticle(String keyword, String order, String desc);
}
