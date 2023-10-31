package com.martini.sparrow.service;

import com.martini.sparrow.entity.Admin;

/**
 * @author martini at 2020/12/15 20:33
 */
public interface AdminService {
    Admin findByJobNumber(String jobNumber);
}
