package com.martini.sparrow.service.impl;

import cn.hutool.Hutool;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HtmlUtil;
import com.martini.sparrow.elastic.entity.ArticleEsEntity;
import com.martini.sparrow.elastic.repository.ArticleEsEntityRepository;
import com.martini.sparrow.entity.*;
import com.martini.sparrow.mapper.*;
import com.martini.sparrow.service.ArticleService;
import com.martini.sparrow.service.MessageService;
import org.apache.ibatis.annotations.Param;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author martini at 2020/12/15 8:13
 */
@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private ArticleEsEntityRepository articleEsEntityRepository;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AuthorMapper authorMapper;
    @Autowired
    private TypeMapper typeMapper;


    @Override
    public List<Article> findAll() {
        return articleMapper.findAll();
    }

    @Override
    public Integer postOne(Article article) {
        return articleMapper.insertOne(article);
    }

    @Override
    public Article findById(Long id) {
        return articleMapper.findById(id);
    }

    @Override
    public Integer updateById(Long id, Article article) {
        // TODO: 状态的常量抽离
        Integer integer = articleMapper.updateById(id, article);
        if (article.getStatus() != null) {
            if (article.getStatus() == 0) {
                // restHighLevelClient.search();
            }
        }
        return integer;
    }

    /**
     * 文章通过审核逻辑
     * @param id 文章id
     * @return 操作ok与否
     */
    @Autowired
    private MessageService messageService;
    @Override
    public Boolean approve(Long id) {
        Article article = articleMapper.findById(id);
        article.setStatus(0);
        Integer updated = articleMapper.updateById(id, article);
        // 插入到es中
        Book book = bookMapper.findById(article.getBookId());
        Author author = authorMapper.findById(book.getAuthorId());
        Type type = typeMapper.findById(book.getTypeId());
        User user = userMapper.findById(article.getUserId());
        ArticleEsEntity articleEsEntity = new ArticleEsEntity();
        articleEsEntity.setArticleId(article.getId())
                .setArticleTitle(article.getTitle())
                .setAuthorId(author.getId())
                .setAuthorName(author.getName())
                .setBookId(book.getId())
                .setBookName(book.getTitle())
                .setCommentCount(article.getCommentCount())
                .setContent(StrUtil.sub(HtmlUtil.cleanHtmlTag(article.getContent()), 0, 100))
                .setFavoriteCount(article.getFavoriteCount())
                .setLikeCount(article.getLikeCount())
                .setReadCount(article.getReadCount())
                .setUserAvatar(user.getAvatar())
                // 自定义
                .setPicUrl((article.getPicUrl() == null || article.getPicUrl().length() == 0) ? book.getImgUrl() : article.getPicUrl())
                .setPublishTime(article.getPublishTime())
                .setTypeId(type.getId())
                .setTypeName(type.getName())
                // TODO: 这里
                .setUpdateTime(article.getUpdateTime())
                .setUserId(user.getUid())
                .setUserName(user.getNickname())
                .setId(IdUtil.fastSimpleUUID());
        articleEsEntityRepository.save(articleEsEntity);
        messageService.pushMessage(
                article.getUserId(),
                new Message()
                        .setId(IdUtil.simpleUUID())
                        .setDatetime(new Date())
                        .setTitle("消息通知：文章过审")
                        .setContent(
                                "您的文章：<a target='_blank' href='/article/" + article.getId() + "'>《" +
                                        article.getTitle() + "》</a>已过审"));
        messageService.messageToFans(
                article.getUserId(),
                new Message()
                    .setContent("您关注的UP：<a href='/user/" + user.getUid() + "'>" + user.getNickname() + "</a>发布了文章《<a href='/article/" + article.getId() +"'>" + article.getTitle() + "</a>》")
                    .setTitle("关注通知：发布文章")
                    .setId(IdUtil.simpleUUID())
                    .setDatetime(new Date()));
        return true;
    }

    @Override
    public Integer count() {
        return articleMapper.count();
    }

    @Override
    public List<Article> findLimit(Integer page, Integer limit) {
        int startIndex = (page - 1) * limit;
        int size = limit;
        return articleMapper.findLimit(startIndex, size);
    }

    @Override
    public Boolean disapprove(Long id) {
        Article article = articleMapper.findById(id);
        article.setStatus(2);
        Integer updated = articleMapper.updateById(id, article);
        // 从es删除
        Iterable<ArticleEsEntity> esArticle = articleEsEntityRepository.search(QueryBuilders.matchQuery("articleId", id));
        if (esArticle.iterator().hasNext()) {
            articleEsEntityRepository.delete(esArticle.iterator().next());
        }
        return true;
    }

    @Override
    public List<Article> findByUserId(Long uid) {
        return articleMapper.findByUserId(uid);
    }

    @Override
    public int cancelLikeArticle(long aid) {
        return articleMapper.cancelLikeArticle(aid);
    }

    @Override
    public int likeArticle(long aid) {
        return articleMapper.likeArticle(aid);
    }

    @Override
    public Integer approvedCount() {
        return articleMapper.approvedCount();
    }

    @Override
    public int addReadCount(Long id, int randomInt) {
        return articleMapper.addReadCount(id, randomInt);
    }

    @Override
    public int addCommentCount(Long articleId) {
        return articleMapper.addCommentCount(articleId);
    }
}
