package com.martini.sparrow.service;

import com.martini.sparrow.entity.Express;

import java.util.List;

/**
 * @author martini at 2020/12/16 9:46
 */
public interface ExpressService {
    List<Express> findAll();

    Object findById(Long id);
}
