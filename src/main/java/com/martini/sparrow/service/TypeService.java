package com.martini.sparrow.service;

import com.martini.sparrow.entity.Type;

import java.util.List;

/**
 * @author martini at 2020/12/16 9:58
 */
public interface TypeService {
    List<Type> findAll();

    Object findById(Long id);
}
