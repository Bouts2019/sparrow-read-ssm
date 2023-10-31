package com.martini.sparrow.mapper;

import com.martini.sparrow.entity.Book;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author martini at 2020/12/14 20:47
 */
@Repository
// @Mapper
public interface BookMapper {
    int addOne(Book book);
    List<Book> findAll();
    Book findById(Long id);

    Integer count();

    List<Book> findLimit(@Param("startIndex") Integer startIndex, @Param("size") Integer size);

    int updateById(Book book);

    int deleteById(Long id);

    int deleteByIdIn(List<Long> ids);

    Book findByTitleLike(String title);
}
