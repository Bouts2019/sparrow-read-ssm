package com.martini.sparrow.service;

import com.martini.sparrow.entity.UserBookFinishTime;

import java.util.Date;
import java.util.List;

/**
 * @author martini at 2020/12/20 10:33
 */
public interface UserBookFinishTimeService {
    List<UserBookFinishTime> findByUserId(Long uid);
    Integer insertOne(Long bid, Long uid, Date now);
}
