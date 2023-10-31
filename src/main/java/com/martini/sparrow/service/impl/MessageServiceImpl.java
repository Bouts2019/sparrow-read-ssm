package com.martini.sparrow.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.martini.sparrow.entity.Message;
import com.martini.sparrow.entity.User;
import com.martini.sparrow.service.MessageService;
import com.martini.sparrow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * @author martini at 2021/1/12 14:11
 */
@Service
@Transactional
public class MessageServiceImpl implements MessageService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserService userService;
    @Override
    public Long pushMessage(Long uid, Message message) {
        try {
            return redisTemplate.opsForList().rightPush("user:" + uid + ":unread-messages", objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Long messageToFans(Long userId, Message message) {
        Set<String> members = redisTemplate.opsForSet().members(userId + ":fans");
        User byId = userService.findById(userId);
        for (String member : members) {
            long fanId = Long.parseLong(member);
            pushMessage(fanId, message);
        }
        return null;
    }
}
