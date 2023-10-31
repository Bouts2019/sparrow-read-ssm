package com.martini.sparrow.controller.restful;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * TODO: better way for advanced operation, such as sort by follow time
 * 1:follow，1的关注列表
 * 2:fans，2的粉丝列表
 * @author martini at 2020/12/28 14:17
 */
@RestController
@CrossOrigin
@RequestMapping("/follow")
@Slf4j
@Api("关注操作相关api")
public class FollowController {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 判断是否存在关注关系
     * from 关注了 to 吗
     * @param from 粉丝id
     * @param to 被关注者的id
     * @return 布尔值，如果为真表示to在from的关注列表中
     */
    @GetMapping("/isFollowed")
    public Object isFollowed(@RequestParam("from") Object from, @RequestParam("to") Object to) {
        return redisTemplate.opsForSet().isMember(from + ":follow", to);
    }

    @PostMapping("/follow")
    public Object follow(@RequestBody Map<String, String> info, HttpServletResponse response) {
        Long from = Long.parseLong(info.get("from"));
        Long to = Long.parseLong(info.get("to"));
        if (from == null || to == null) {
            response.setStatus(402);
            throw new RuntimeException("空参数");
        }
        redisTemplate.opsForSet().add(from + ":follow", to + "");
        redisTemplate.opsForSet().add(to + ":fans", from + "");
        return null;
    }

    @PostMapping("/unfollow")
    public Object unfollow(@RequestBody Map<String, String> info, HttpServletResponse response) {
        Long from = Long.parseLong(info.get("from"));
        Long to = Long.parseLong(info.get("to"));
        if (from == null || to == null) {
            response.setStatus(402);
            throw new RuntimeException("空参数");
        }
        redisTemplate.opsForSet().remove(from + ":follow", to + "");
        redisTemplate.opsForSet().remove(to + ":fans", from + "");
        return null;
    }

    /**
     * 获取id为路径中id用户关注列表总数
     * @param id 用户id
     * @return 关注总数
     */
    @GetMapping("/count/follow/{id}")
    public Object getFollowCount(@PathVariable("id") Long id) {
        return redisTemplate.opsForSet().size(id + ":follow");
    }

    /**
     * 获取id为路径中id用户的被关注的列表总数
     * @param id 用户id
     * @return 粉丝总数
     */
    @GetMapping("/count/fans/{id}")
    public Object getFansCount(@PathVariable("id") Long id) {
        return redisTemplate.opsForSet().size(id + ":fans");
    }

    /**
     * 根据用户id获取关注列表的id
     * @param uid 用户id
     * @return 关注id列表(set)
     */
    @GetMapping("/{uid}")
    public Object findFollowByUserId(@PathVariable("uid") Long uid) {
        return redisTemplate.opsForSet().members(uid + ":follow");
    }
    @GetMapping("/fans/{uid}")
    public Object findFansByUserId(@PathVariable("uid") Long uid) {
        return redisTemplate.opsForSet().members(uid + ":fans");
    }
}
