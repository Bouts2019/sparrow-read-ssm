package com.martini.sparrow.service;

import com.martini.sparrow.entity.Book;
import com.martini.sparrow.entity.User;

import java.util.List;

/**
 * @author martini at 2020/12/13 18:54
 */
public interface UserService {
    List<User> findAll();
    User findByPhone(String phone);
    User findByEmail(String email);
    User findById(Long id);

    Integer register(String phone, String password, String nickname);

    Integer count();

    List<User> findLimit(Integer page, Integer limit);
    Integer getArticleCountById(Long id);
    Integer getTalkCountById(Long id);
    List<User> findUsersByIds(List<Long> ids);

    Integer updateById(User user);
}
