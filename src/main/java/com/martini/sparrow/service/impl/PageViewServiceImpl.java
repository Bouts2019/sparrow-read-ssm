package com.martini.sparrow.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.martini.sparrow.entity.PageView;
import com.martini.sparrow.mapper.PageViewMapper;
import com.martini.sparrow.service.PageViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author martini at 2020/12/21 21:25
 */
@Service
@Transactional
public class PageViewServiceImpl implements PageViewService {
    @Autowired
    private PageViewMapper pageViewMapper;

    @Override
    public List<PageView> getWeekData() {
        List<String> dates = new ArrayList<>();
        Calendar instance = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 1; i <= DAYS_OF_ONE_WEEK; i++) {
            instance.add(Calendar.DAY_OF_MONTH, -1);
            dates.add(sdf.format(instance.getTime()));
        }
        System.out.println(dates);
        return pageViewMapper.findByDateIn(dates);
    }

    @Override
    public int add() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String format = sdf.format(new Date());
        if (pageViewMapper.existsDate(format) == 0) {
            pageViewMapper.createDate(format);
        }
        return pageViewMapper.add(RandomUtil.randomInt(100, 200), format);
    }

    @Override
    public List<PageView> getHalfOfMonthData() {
        List<String> dates = new ArrayList<>();
        Calendar instance = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 1; i <= 15; i++) {
            instance.add(Calendar.DAY_OF_MONTH, -1);
            dates.add(sdf.format(instance.getTime()));
        }
        return pageViewMapper.findByDateIn(dates);
    }

    @Override
    public List<PageView> getMonthData() {
        List<String> dates = new ArrayList<>();
        Calendar instance = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 1; i <= 30; i++) {
            instance.add(Calendar.DAY_OF_MONTH, -1);
            System.out.println("date " + instance.get(Calendar.DAY_OF_MONTH));
            dates.add(sdf.format(instance.getTime()));
        }
        return pageViewMapper.findByDateIn(dates);
    }
}
