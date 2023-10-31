package com.martini.sparrow.controller.restful;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.martini.sparrow.entity.TalkComment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 区别于文章的评论，talk板块的评论直接保存在redis中
 * 通过 talk:{id}:comments的方式保存
 * 每一个中使用的也是TalkComment这个entity
 * @author martini at 2021/1/8 13:30
 */
@CrossOrigin
@RestController
@Slf4j
@RequestMapping("/talk-comment")
public class TalkCommentController {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @GetMapping("/all/{id}")
    public Object findAllByTalkId(@PathVariable("id") String talkId) {
        List<String> range = redisTemplate.opsForList().range("talk:" + talkId + ":comments", 0L, -1L);
        List<TalkComment> talkComments = new ArrayList<>();
        try {
            for (String commentJson : range) {
                talkComments.add(objectMapper.readValue(commentJson, TalkComment.class));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return talkComments;
    }

    @GetMapping("/size/{id}")
    public Object getSizeByTalkId(@PathVariable("id") String talkId ) {
        System.out.println(talkId);
        Long size = redisTemplate.opsForList().size("talk:" + talkId + ":comments");
        System.out.println(size);
        return size;
    }

    @PostMapping("/add/{id}")
    public Object addOneComment(@PathVariable("id") String talkId, @RequestBody Map<String, String> talkComment) {
        Long userId = Long.parseLong(talkComment.get("userId"));
        Long replyUserId = talkComment.get("replyUserId") == null ? null : Long.parseLong(talkComment.get("replyUserId"));
        String commentContent = talkComment.get("commentContent");
        Date date = new Date();
        TalkComment comment = new TalkComment()
                .setUserId(userId)
                .setReplyUserId(replyUserId)
                .setCommentContent(commentContent)
                .setCommentTime(date)
                .setLikeCount(0)
                .setDislikeCount(0);
        String commentJson = null;
        try {
             commentJson = objectMapper.writeValueAsString(comment);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(commentJson);
        return redisTemplate.opsForList().rightPush("talk:" + talkId + ":comments", commentJson);
    }
}
