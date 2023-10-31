package com.martini.sparrow.service.impl;

import com.martini.sparrow.entity.Type;
import com.martini.sparrow.mapper.TypeMapper;
import com.martini.sparrow.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author martini at 2020/12/16 9:58
 */
@Service
@Transactional
public class TypeServiceImpl implements TypeService {
    @Autowired
    private TypeMapper typeMapper;
    @Override
    public List<Type> findAll() {
        return typeMapper.findAll();
    }

    @Override
    public Object findById(Long id) {
       return typeMapper.findById(id);
    }
}
