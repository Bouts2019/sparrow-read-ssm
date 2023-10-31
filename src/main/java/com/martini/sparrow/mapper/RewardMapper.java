package com.martini.sparrow.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * @author martini at 2021/1/13 17:19
 */
public interface RewardMapper {
    int add(@Param("id") Long id, @Param("num") Integer number);
    int reduce(@Param("id") Long id, @Param("num") Integer number);
}
