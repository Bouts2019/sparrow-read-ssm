package com.martini.sparrow.mapper;

import com.martini.sparrow.entity.Type;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author martini at 2020/12/16 9:55
 */
@Repository
@Mapper
public interface TypeMapper {
    List<Type> findAll();
    Type findById(Long id);
}
