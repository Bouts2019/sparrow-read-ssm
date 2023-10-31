package com.martini.sparrow.controller.restful;

import cn.hutool.core.util.IdUtil;
import com.martini.sparrow.entity.Message;
import com.martini.sparrow.mapper.RewardMapper;
import com.martini.sparrow.service.ArticleService;
import com.martini.sparrow.service.MessageService;
import com.martini.sparrow.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

/**
 * @author martini at 2021/1/13 17:17
 */
@RestController
@CrossOrigin
@RequestMapping("/reward")
@Slf4j
public class RewardController {
    @Autowired
    private RewardMapper rewardMapper;
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;
    @PostMapping("/reward")
    public Object reward(@RequestBody Map<String, Object> map) {
        System.out.println(map);
        Long from = Long.parseLong(map.get("from").toString());
        Long to = Long.parseLong(map.get("to").toString());
        Integer number = Integer.parseInt(map.get("number").toString());
        String nickname = userService.findById(from).getNickname();
        messageService.pushMessage(to, new Message()
                .setId(IdUtil.simpleUUID())
                .setDatetime(new Date())
                .setTitle("消息提醒：新的投币")
                .setContent("用户: <a href='/user/" + from + "'>" + nickname + "</a>给您投了 " + number + " 个币"));
        rewardMapper.add(to, number);
        rewardMapper.reduce(from, number);
        return null;
    }
}
