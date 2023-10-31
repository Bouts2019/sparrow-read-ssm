package com.martini.sparrow.controller.restful;

import cn.hutool.core.util.IdUtil;
import com.martini.sparrow.elastic.entity.ArticleEsEntity;
import com.martini.sparrow.elastic.repository.ArticleEsEntityRepository;
import com.martini.sparrow.entity.Article;
import com.martini.sparrow.entity.Comment;
import com.martini.sparrow.entity.Message;
import com.martini.sparrow.entity.User;
import com.martini.sparrow.service.ArticleService;
import com.martini.sparrow.service.CommentService;
import com.martini.sparrow.service.MessageService;
import com.martini.sparrow.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author martini at 2021/1/7 16:56
 */
@CrossOrigin
@RestController
@Slf4j
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private ArticleService articleService;

    @GetMapping("/all/{id}")
    public Object findAllByArticleId(@PathVariable("id") Long articleId) {
        List<Comment> allByArticleId = commentService.findAllByArticleId(articleId);
        return allByArticleId;
    }

    @Autowired
    private ArticleEsEntityRepository articleEsEntityRepository;

    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;

    @PostMapping("/one")
    public Object postOneComment(@RequestBody Map<String, String> data) {
        String content = data.get("content");
        Long userId = Long.parseLong(data.get("userId"));
        Long articleId = Long.parseLong(data.get("articleId"));
        Long replyUserId = data.get("replyUserId") == null ? null : Long.parseLong(data.get("replyUserId"));

        Article article = articleService.findById(articleId);
        User user = userService.findById(userId);
        if (!article.getUserId().equals(user.getUid())) {
            messageService.pushMessage(
                    article.getUserId(),
                    new Message()
                            .setId(IdUtil.simpleUUID())
                            .setDatetime(new Date())
                            .setTitle("消息通知：您的文章有新的评论")
                            .setContent(
                                    "<a href='/user/" +
                                            userId +
                                            "' target='_blank'>" + user.getNickname() +
                                            "</a>评论了您的文章：<a target='_blank' href='/article/" + article.getId() + "'>《" +
                                            article.getTitle() + "》</a>"));
        }

        Iterator<ArticleEsEntity> foundArticleIterator = articleEsEntityRepository.search(QueryBuilders.matchQuery("articleId", articleId)).iterator();
        if (foundArticleIterator.hasNext()) {
            ArticleEsEntity foundArticle = foundArticleIterator.next();
            foundArticle.setCommentCount(foundArticle.getCommentCount() + 1);
            articleEsEntityRepository.save(foundArticle);
        }
        Comment comment = new Comment()
                .setArticleId(articleId)
                .setCommentContent(content)
                .setCommentTime(new Date())
                .setUserId(userId)
                .setReplyUserId(replyUserId)
                .setLikeCount(0)
                .setDislikeCount(0);
        articleService.addCommentCount(articleId);
        return commentService.addOne(comment);
    }
}
