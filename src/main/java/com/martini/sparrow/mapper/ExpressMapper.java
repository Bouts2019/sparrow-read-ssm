package com.martini.sparrow.mapper;

import com.martini.sparrow.entity.Express;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author martini at 2020/12/16 9:45
 */
@Repository
@Mapper
public interface ExpressMapper {
    List<Express> findAll();

    Object findById(Long id);
}
