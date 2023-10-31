package com.martini.sparrow.service.impl;

import com.martini.sparrow.entity.Reference;
import com.martini.sparrow.mapper.ReferenceMapper;
import com.martini.sparrow.service.ReferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author martini at 2020/12/22 8:59
 */
@Service
@Transactional
public class ReferenceServiceImpl implements ReferenceService {
    @Autowired
    private ReferenceMapper referenceMapper;
    @Override
    public List<Reference> getRefData() {
        return referenceMapper.getRefData();
    }
}
