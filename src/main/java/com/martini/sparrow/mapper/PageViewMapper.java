package com.martini.sparrow.mapper;

import com.martini.sparrow.entity.PageView;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author martini at 2020/12/21 21:15
 */
@Repository
public interface PageViewMapper {
    List<PageView> findByDateIn(List<String> dates);

    int add(@Param("number") Integer number, @Param("date") String date);

    int existsDate(String date);

    int createDate(String date);
}
