package com.martini.sparrow.controller;

import com.martini.sparrow.entity.User;
import com.martini.sparrow.mapper.UserMapper;
import com.martini.sparrow.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * @author martini at 2020/12/13 17:28
 */
@Controller
@Api(tags = "TestController")
public class TestController {
    @Autowired
    private UserService userService;

    @ApiOperation(httpMethod = "GET", value = "测试接口", notes = "无需参数")
    @GetMapping(value = "/test1", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String test1() {
        return "中文测试123";
    }

    @PostMapping("/test2")
    public String test2() {
        return "test";
    }

    @GetMapping("/test3")
    @ResponseBody
    public List<User> test3() {
        return userService.findAll();
    }

    @Autowired
    private RestHighLevelClient highLevelClient;

    @GetMapping("/test4")
    @ResponseBody
    public SearchHit[] test4() {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());

        SearchRequest searchRequest = new SearchRequest("poems");
        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse search = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            return search.getHits().getHits();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
