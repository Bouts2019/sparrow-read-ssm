package com.martini.sparrow.service;

import com.martini.sparrow.entity.Banner;

import java.util.List;

/**
 * @author martini at 2020/12/19 8:15
 */
public interface BannerService {
    List<Banner> findAll();
}
