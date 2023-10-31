package com.martini.sparrow.mapper;

import com.martini.sparrow.entity.Reference;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author martini at 2020/12/22 8:57
 */
@Repository
public interface ReferenceMapper {
    List<Reference> getRefData();
}
