package com.martini.sparrow.controller.restful;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author martini at 2020/12/27 19:48
 */
@RestController
@CrossOrigin
@Slf4j
@Api("推荐作者控制器")
@RequestMapping("/recommend-author")
public class RecommendAuthorController {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @GetMapping("/all")
    public Object findAll() {
        Set<String> members = redisTemplate.opsForSet().members("recommend-authors");
        Set<Map> res = new HashSet<>();
        for (String member : members) {
            try {
                res.add(objectMapper.readValue(member, Map.class));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return res;
    }
    @PostMapping("/one")
    public Object addOne(@RequestBody Map<String, String> info, HttpServletResponse response) {
        if (info.get("avatar") == null || info.get("nickname") == null || info.get("id") == null) {
            response.setStatus(402);
            throw new RuntimeException("错误：不完全的参数。");
        }
        try {
            redisTemplate.opsForSet().add("recommend-authors", objectMapper.writeValueAsString(info));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 随机获取**3**个推荐的作者
     * @return
     */
    @GetMapping("/random")
    public Object getRandomItem() {
        Set<String> members = redisTemplate.opsForSet().distinctRandomMembers("recommend-authors", 3);
        Set<Map> res = new HashSet<>();
        for (String member : members) {
            try {
                res.add(objectMapper.readValue(member, Map.class));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    @DeleteMapping("/one")
    public Object deleteOneItem(@RequestBody String obj) {
        redisTemplate.opsForSet().remove("recommend-authors", obj);
        return null;
    }
}
