package com.martini.sparrow.controller.restful;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 热词接口，主要用于后台管理员使用
 * 当用户在搜索栏输入数据的时候，通过{@link com.github.houbb.segment.util.SegmentHelper}
 * 来对内容进行分词，分词结果保存到redis中，key为hotwords
 * 管理员界面将会显示这些热词，通过这些热词来通过接口控制排行榜显示的内容。
 * 管理员根据热词判断流行词，随后加入到es远程词典中
 * TODO: 分层，将数据搜索从controller层中分离出去
 * @author martini at 2020/12/14 11:44
 */
@RestController
@Slf4j
@CrossOrigin
@RequestMapping("/hot-word")
@Api("Hot Word Controller")
public class HotWordController {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/findAll")
    public Object findAllHotWord() {
        return redisTemplate.opsForZSet().reverseRangeWithScores("hot-word", 0, -1);
    }
}
