package com.martini.sparrow.service;

import cn.hutool.db.Page;
import com.martini.sparrow.entity.PageView;
import com.martini.sparrow.mapper.PageViewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author martini at 2020/12/21 21:20
 */
public interface PageViewService {
    int DAYS_OF_ONE_WEEK = 7;
    List<PageView> getWeekData();

    int add();

    List<PageView> getHalfOfMonthData();

    List<PageView> getMonthData();
}
