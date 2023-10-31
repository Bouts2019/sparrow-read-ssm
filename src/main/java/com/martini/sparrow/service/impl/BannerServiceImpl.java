package com.martini.sparrow.service.impl;

import com.martini.sparrow.entity.Banner;
import com.martini.sparrow.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author martini at 2020/12/19 8:16
 */
@Service
@Transactional
public class BannerServiceImpl implements BannerService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public List<Banner> findAll() {
        // TODO: 再抽象一层
        List range = redisTemplate.opsForList().range("banners", 0, -1);
        System.out.println(range);
        return range;
    }
}
