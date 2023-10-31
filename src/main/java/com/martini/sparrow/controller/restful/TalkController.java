package com.martini.sparrow.controller.restful;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.ssh.Sftp;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.martini.sparrow.entity.Message;
import com.martini.sparrow.entity.Talk;
import com.martini.sparrow.entity.User;
import com.martini.sparrow.mapper.UserMapper;
import com.martini.sparrow.service.MessageService;
import com.martini.sparrow.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author martini at 2020/12/30 11:51
 */
@RestController
@CrossOrigin
@RequestMapping("/talk")
@Slf4j
public class TalkController {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @GetMapping("/count-all")
    public Object getCountAll() {
        return redisTemplate.opsForHash().size("talks-hash");
    }

    @GetMapping("/count/{id}")
    public Object getCountByUserId(@PathVariable("id") Long userId) {
        return redisTemplate.opsForSet().size("user:" + userId + ":talks");
    }

    @GetMapping("")
    public Object findTalkAfterDatetime(@RequestParam String time, @RequestParam Integer filter, HttpServletResponse response) {
        Long startMilliseconds = null;
        try {
            startMilliseconds = sdf.parse(time).getTime();
        } catch (ParseException e) {
            response.setStatus(402);
            e.printStackTrace();
            return null;
        }
        if (startMilliseconds == null) throw new RuntimeException("解析错误");
        Set<ZSetOperations.TypedTuple<String>> res = redisTemplate.opsForZSet().reverseRangeByScoreWithScores("talks-millisecond", 0, startMilliseconds, 0, 10);
        List<String> talksJson = new ArrayList<>();
        res.forEach(uuid -> {
            talksJson.add(redisTemplate.opsForHash().get("talks-hash", uuid.getValue()).toString());
        });
        List<Talk> talks = new ArrayList<>();
        try {
            for (String json : talksJson) {
                talks.add(objectMapper.readValue(json, Talk.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (filter != null && filter != 0) {
            talks = talks.stream().filter(item -> Objects.equals(item.getTagId(), filter)).collect(Collectors.toList());
        }
        for (Talk talk : talks) {
            User byId = userService.findById(talk.getUserId());
            talk.setUserDesc(byId.getDescription());
            talk.setUserNickname(byId.getNickname());
            talk.setUserGender(byId.getGender());
            talk.setUserAvatar(byId.getAvatar());
        }
        return talks;
    }

    @PostMapping("/image")
    public Object uploadAvatar(@RequestParam("file") MultipartFile uploadFiles) {
        // TODO: 路径需要外部properties注入
        // TODO: SFTP -> FTP
        // TODO: upload 路径
        Sftp sftp = new Sftp("192.168.1.100", 22, "bouts", "120309");
        String name = IdUtil.fastSimpleUUID();
        try {
            name = name + "." + FileUtil.getSuffix(uploadFiles.getResource().getFilename());
            File file = new File(name);
            uploadFiles.transferTo(file);
            sftp.upload("/public/upload_images/talk_pics", file);
            file.delete();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            sftp.close();
        }
        return "http://192.168.1.100/talk_pics/" + name;
    }

    /**
     * 使用uuid -> talk实体映射，保存在hash中
     * zset用来记录时间排序
     *
     * @param talk
     * @return
     */
    @PostMapping("/one")
    public Object postOneTalk(@RequestBody Talk talk) {
        Date now = new Date();
        String uuid = IdUtil.simpleUUID();
        talk
                .setTalkId(uuid)
                .setCommentCount(0)
                .setLikeCount(0)
                .setPublishTime(now);
        try {
            redisTemplate.opsForSet().add("user:" + talk.getUserId() + ":talks", uuid);
            redisTemplate.opsForHash().put("talks-hash", uuid, objectMapper.writeValueAsString(talk));
            redisTemplate.opsForZSet().add("talks-millisecond", uuid, now.getTime());
            messageService.pushMessage(
                    talk.getUserId(),
                    new Message()
                            .setId(IdUtil.simpleUUID())
                            .setTitle("消息通知：发布动态")
                            .setContent(userService.findById(talk.getUserId()).getNickname() + "，您发布了一条动态。")
                            .setDatetime(new Date()));
            return uuid;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/user/{id}")
    public Object findTalksByUserId(@PathVariable("id") Long id) {
        Set<String> uuids = redisTemplate.opsForSet().members("user:" + id + ":talks");
        List<Talk> talks = new ArrayList<>();
        try {
            for (String uuid : uuids) {
                String talkJsonData = redisTemplate.opsForHash().get("talks-hash", uuid).toString();
                talks.add(objectMapper.readValue(talkJsonData, Talk.class));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        User byId = userService.findById(id);
        for (Talk talk : talks) {
            talk.setUserDesc(byId.getDescription());
            talk.setUserNickname(byId.getNickname());
            talk.setUserGender(byId.getGender());
            talk.setUserAvatar(byId.getAvatar());
        }
        return talks;
    }

    @PostMapping("/toggle-like")
    public Object toggleLike(@RequestBody Map<String, Object> map, HttpServletResponse response) {
        String tid = map.get("tid").toString();
        String uid = map.get("uid").toString();
        if (redisTemplate.opsForSet().isMember("talk:" + tid + ":likes", uid)) {
            redisTemplate.opsForSet().remove("talk:" + tid + ":likes", uid);
        } else {
            redisTemplate.opsForSet().add("talk:" + tid + ":likes", uid);
        }
        return null;
    }

    @GetMapping("/is-like")
    public Object isLike(@RequestParam Map<String, Object> map, HttpServletResponse response) {
        String tid = map.get("tid").toString();
        String uid = map.get("uid").toString();
        return redisTemplate.opsForSet().isMember("talk:" + tid + ":likes", uid);
    }

    @GetMapping("/count/like/{id}")
    public Object count(@PathVariable("id") String talkId) {
        System.out.println(talkId);
        System.out.println("likes: " + redisTemplate.opsForSet().size("talk:" + talkId + ":likes"));
        return redisTemplate.opsForSet().size("talk:" + talkId + ":likes");
    }
}
