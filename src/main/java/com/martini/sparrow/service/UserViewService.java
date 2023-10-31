package com.martini.sparrow.service;

import com.martini.sparrow.entity.PageView;
import com.martini.sparrow.entity.UserView;

import java.util.List;

/**
 * @author martini at 2021/1/13 8:29
 */
public interface UserViewService {
    int DAYS_OF_ONE_WEEK = 7;
    List<UserView> getWeekData();

    int add();

    List<UserView> getHalfOfMonthData();

    List<UserView> getMonthData();
}
