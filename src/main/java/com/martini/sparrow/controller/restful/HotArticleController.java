package com.martini.sparrow.controller.restful;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 排行榜不是热搜，排行榜是HotArticle
 * 排行榜接口，主要用于对排行榜的CRUD操作
 * 注意对于普通用户，只有R(Read)权限
 * TODO: 分层
 * @author martini at 2020/12/14 17:28
 *
 * hot-article -> recommend->article
 */
@RestController
@Slf4j
@CrossOrigin
@RequestMapping("/hot-article")
@Api("Rank Controller")
public class HotArticleController {
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private RedisTemplate redisTemplate;
    @GetMapping("/findAll")
    public Object getRank() {
        List<String> range = redisTemplate.opsForList().range("hot-article", 0, -1);
        List<Map> res = new ArrayList<>();
        range.forEach(item -> {
            try {
                res.add(mapper.readValue(item, Map.class));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        return res;
    }

    @PutMapping("/update")
    public Object updateRank(String title, Double score) {
        return null;
    }

    @PostMapping("/one")
    public Object addOne(@RequestBody Map<String, String> hotArticle, HttpServletResponse response) {
        try {
            redisTemplate.opsForList().rightPush("hot-article", mapper.writeValueAsString(hotArticle));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
    @DeleteMapping("/{index}")
    public Object deleteIndex(@PathVariable("index") Integer index) {
        try {
            redisTemplate.opsForList().remove("hot-article", 0, redisTemplate.opsForList().index("hot-article", index));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }
    @PutMapping("/up/{index}")
    public Object upHotArticle(@PathVariable("index") Integer index) {
        Object prev = redisTemplate.opsForList().index("hot-article", index - 1);
        redisTemplate.opsForList().set("hot-article", index - 1, redisTemplate.opsForList().index("hot-article", index));
        redisTemplate.opsForList().set("hot-article", index, prev);
        return null;
    }
    @PutMapping("/down/{index}")
    public Object downHotArticle(@PathVariable("index") Integer index) {
        Object next = redisTemplate.opsForList().index("hot-article", index + 1);
        redisTemplate.opsForList().set("hot-article", index + 1, redisTemplate.opsForList().index("hot-article", index));
        redisTemplate.opsForList().set("hot-article", index, next);
        return null;
    }
}
