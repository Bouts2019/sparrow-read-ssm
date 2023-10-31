package com.martini.sparrow.service.impl;

import cn.hutool.core.lang.Validator;
import com.martini.sparrow.entity.Book;
import com.martini.sparrow.entity.User;
import com.martini.sparrow.mapper.BookMapper;
import com.martini.sparrow.mapper.UserMapper;
import com.martini.sparrow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author martini at 2020/12/13 18:54
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BookMapper bookMapper;

    @Override
    public List<User> findAll() {
        return userMapper.findAll();
    }

    @Override
    public User findByPhone(String phone) {
        return userMapper.findByPhone(phone);
    }

    @Override
    public User findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    @Override
    public User findById(Long id) {
        return userMapper.findById(id);
    }

    @Override
    public Integer register(String phone, String password, String nickname) {
        if (phone == null || password == null || nickname == null) return 403;
        if (!Validator.isNumber(phone) || password.length() < 6 || nickname.length() > 10 || nickname.length() < 3)
            return 404;
        User byPhone = userMapper.findByPhone(phone);
        User byNickname = userMapper.findByNickname(nickname);
        // TODO: 使用throw代替return
        // TODO: 值200等对应的状态的定义
        if (byPhone != null || byNickname != null) return 401;
        User user = new User();
        user.setNickname(nickname);
        user.setPhone(phone);
        user.setPassword(password);
        user.setAvatar("http://192.168.1.100/avatars/unnamed.jpg");
        user.setDescription("默认描述");
        Date date = new Date();
        user.setRegTime(date);
        user.setGender(0);
        user.setBirthday(date);
        Integer res = userMapper.insertOne(user);
        return res > 0 ? 200 : 402;
    }

    @Override
    public Integer count() {
        return userMapper.count();
    }

    @Override
    public List<User> findLimit(Integer page, Integer limit) {
        int startIndex = (page - 1) * limit;
        int size = limit;
        return userMapper.findLimit(startIndex, size);
    }

    @Override
    public Integer getArticleCountById(Long id) {
        return userMapper.getArticleCountById(id);
    }

    @Override
    public Integer getTalkCountById(Long id) {
        return userMapper.getTalkCountById(id);
    }

    @Override
    public List<User> findUsersByIds(List<Long> ids) {
        return userMapper.findUsersByIds(ids);
    }

    @Override
    public Integer updateById(User user) {
        return userMapper.updateById(user);
    }
}

