package com.martini.sparrow.service.impl;

import com.martini.sparrow.entity.Admin;
import com.martini.sparrow.mapper.AdminMapper;
import com.martini.sparrow.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author martini at 2020/12/15 20:33
 */
@Service
@Transactional
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;
    @Override
    public Admin findByJobNumber(String jobNumber) {
        return adminMapper.findByJobNumber(jobNumber);
    }
}
