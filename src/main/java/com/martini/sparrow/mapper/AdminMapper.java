package com.martini.sparrow.mapper;

import com.martini.sparrow.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author martini at 2020/12/15 20:32
 */
@Repository
@Mapper
public interface AdminMapper {
    Admin findByJobNumber(String jobNumber);
}
