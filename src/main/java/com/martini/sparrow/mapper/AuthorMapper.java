package com.martini.sparrow.mapper;

import com.martini.sparrow.entity.Author;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author martini at 2020/12/16 9:32
 */
@Repository
@Mapper
public interface AuthorMapper {
    List<Author> findAll();
    Author findById(Long id);
}
