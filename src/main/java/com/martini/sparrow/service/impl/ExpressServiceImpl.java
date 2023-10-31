package com.martini.sparrow.service.impl;

import com.martini.sparrow.entity.Express;
import com.martini.sparrow.mapper.ExpressMapper;
import com.martini.sparrow.service.ExpressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author martini at 2020/12/16 9:47
 */
@Service
@Transactional
public class ExpressServiceImpl implements ExpressService {
    @Autowired
    private ExpressMapper expressMapper;
    @Override
    public List<Express> findAll() {
        return expressMapper.findAll();
    }

    @Override
    public Object findById(Long id) {
        return expressMapper.findById(id);
    }
}
