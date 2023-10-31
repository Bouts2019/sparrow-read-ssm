package com.martini.sparrow.controller.restful;

import com.github.houbb.segment.api.ISegmentResult;
import com.github.houbb.segment.support.segment.result.impl.SegmentResultHandlers;
import com.github.houbb.segment.util.SegmentHelper;
import com.martini.sparrow.entity.Poem;
import com.martini.sparrow.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

/**
 * @author martini at 2020/12/14 11:21
 */
@RestController
@Slf4j
@CrossOrigin
public class SearchController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SearchService searchService;

    @GetMapping("/search")
    public Object search(@RequestParam("q") String queryParam,
                         @RequestParam(value = "t", required = false) String order,
                         @RequestParam(value = "d", required = false) String desc) {
        System.out.println("q: " + queryParam);
        System.out.println("t: " + order);
        System.out.println("d: " + desc);
        // 1. 对搜索的进行分段操作
        //     用来放入redis做查询排行
        List<String> segment = SegmentHelper.segment(queryParam, SegmentResultHandlers.word());
        segment.forEach(keyword -> {
            if (keyword.length() > 1) {
                System.out.println(keyword);
                redisTemplate.opsForZSet().incrementScore("hot-word", keyword, 1);
            }
        });
        // TODO: remove this is production enviroment
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<Object> articles = searchService.searchArticle(queryParam, order, desc);
        System.out.println(articles);
        return articles;
    }
}
