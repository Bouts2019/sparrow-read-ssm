package com.martini.sparrow.controller.restful;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.martini.sparrow.elastic.entity.ArticleEsEntity;
import com.martini.sparrow.elastic.repository.ArticleEsEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;

/**
 * @author martini at 2020/12/16 18:43
 */
@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/es")
public class ElasticSearchController {
    @Autowired
    private ArticleEsEntityRepository articleEsEntityRepository;
    @GetMapping("/all")
    public Object findAll() {
        return articleEsEntityRepository.findAll(Sort.by(Sort.Order.desc("publishTime")));
    }
}
