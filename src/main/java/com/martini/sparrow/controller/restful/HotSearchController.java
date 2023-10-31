package com.martini.sparrow.controller.restful;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author martini at 2020/12/23 11:20
 */
@RestController
@Slf4j
@CrossOrigin
@RequestMapping("/hot-search")
@Api("Hot Word Controller")
public class HotSearchController {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @PutMapping("/{id}")
    public Object updateHotSearch(@PathVariable("id") Integer index, @RequestBody Map<String, String> form) {
        try {
            redisTemplate.opsForList().set("hot-search", index, objectMapper.writeValueAsString(form));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
    @DeleteMapping("/{id}")
    public Object deleteItem(@PathVariable("id") Integer index, @RequestBody Map<String, String> form) {
        try {
            redisTemplate.opsForList().remove("hot-search", 0, objectMapper.writeValueAsString(form));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
    @PostMapping("/one")
    public Object addOne(@RequestBody Map<String, String> form) {
        try {
            redisTemplate.opsForList().rightPush("hot-search", objectMapper.writeValueAsString(form));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @PutMapping("/up/{id}")
    public Object upHotSearchItem(@PathVariable("id") Integer index) {
        Object curObj = redisTemplate.opsForList().index("hot-search", index);
        redisTemplate.opsForList().set("hot-search", index, redisTemplate.opsForList().index("hot-search", index - 1));
        redisTemplate.opsForList().set("hot-search", index - 1, curObj);
        // redisTemplate.getConnectionFactory().getConnection().lInsert()
        return null;
    }

    @PutMapping("/down/{id}")
    public Object downHotSearchItem(@PathVariable("id") Integer index) {
        Object curObj = redisTemplate.opsForList().index("hot-search", index);
        redisTemplate.opsForList().set("hot-search", index, redisTemplate.opsForList().index("hot-search", index + 1));
        redisTemplate.opsForList().set("hot-search", index + 1, curObj);
        // redisTemplate.getConnectionFactory().getConnection().lInsert()
        return null;
    }

    @GetMapping("/findAll")
    public Object findAllHotSearchs() {
        // Set<ZSetOperations.TypedTuple<String>> keyword = redisTemplate.opsForZSet().reverseRangeWithScores("keywords", 0, Long.MAX_VALUE);
        List range = redisTemplate.opsForList().range("hot-search", 0, -1);
        List<Map<String, String>> ranks = new ArrayList<>();

        range.forEach(item -> {
            try {
                ranks.add(objectMapper.readValue(item.toString(), Map.class));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        return ranks;
    }
}
