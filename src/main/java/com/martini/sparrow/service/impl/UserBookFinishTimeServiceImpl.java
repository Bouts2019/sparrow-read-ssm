package com.martini.sparrow.service.impl;

import com.martini.sparrow.entity.UserBookFinishTime;
import com.martini.sparrow.mapper.UserBookFinishTimeMapper;
import com.martini.sparrow.service.UserBookFinishTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author martini at 2020/12/20 10:34
 */
@Service
@Transactional
public class UserBookFinishTimeServiceImpl implements UserBookFinishTimeService {
    @Autowired
    private UserBookFinishTimeMapper userBookFinishTimeMapper;
    @Override
    public List<UserBookFinishTime> findByUserId(Long uid) {
        return userBookFinishTimeMapper.findByUserId(uid);
    }

    @Override
    public Integer insertOne(Long bid, Long uid, Date now) {
        return userBookFinishTimeMapper.insertOne(bid, uid, now);
    }
}
