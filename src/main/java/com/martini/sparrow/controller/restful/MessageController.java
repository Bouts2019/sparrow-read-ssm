package com.martini.sparrow.controller.restful;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.martini.sparrow.entity.Message;
import com.martini.sparrow.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author martini at 2021/1/12 13:56
 */
@CrossOrigin
@RestController
@Slf4j
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MessageService messageService;
    @GetMapping("/count/unread/{uid}")
    public Object unreadCount(@PathVariable("uid") Long userId) {
        return redisTemplate.opsForList().size("user:" + userId + ":unread-messages");
    }

    /**
     * 两个部分，未读的和已读的
     * right push 最新的
     * left pop 最旧的
     * @return
     */
    @GetMapping("/all/{uid}")
    public Object allMessages(@PathVariable("uid") Long userId) {
        Long size = redisTemplate.opsForList().size("user:" + userId + ":unread-messages");
        for (int i = 0; i < size; i++) {
            String message = redisTemplate.opsForList().leftPop("user:" + userId + ":unread-messages");
            redisTemplate.opsForList().rightPush("user:" + userId + ":read-messages", message);
        }
        return redisTemplate.opsForList().range("user:" + userId + ":read-messages", 0, -1);
    }

    @PostMapping("/push-message/{uid}")
    public Object pushMessage(@PathVariable("uid") Long uid, @RequestBody Message message) {
        message.setDatetime(new Date());
        return messageService.pushMessage(uid, message);
    }
}
