package com.martini.sparrow.mapper;

import com.martini.sparrow.entity.UserBookFinishTime;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author martini at 2020/12/20 10:30
 */
@Repository
public interface UserBookFinishTimeMapper {
    List<UserBookFinishTime> findByUserId(Long uid);
    Integer insertOne(@Param("bid") Long bid, @Param("uid") Long uid, @Param("now") Date now);
}
