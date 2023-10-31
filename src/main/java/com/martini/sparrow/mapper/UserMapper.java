package com.martini.sparrow.mapper;

import com.martini.sparrow.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author martini at 2020/12/13 18:08
 */
@Repository
// @Mapper
public interface UserMapper {
    List<User> findAll();
    User findByPhone(String phone);
    User findByEmail(String email);
    User findById(Long id);

    User findByNickname(String nickname);

    Integer insertOne(User user);

    Integer count();

    List<User> findLimit(@Param("startIndex") int startIndex, @Param("size") int size);

    Integer getArticleCountById(Long id);
    Integer getTalkCountById(Long id);

    List<User> findUsersByIds(List<Long> ids);

    Integer updateById(User user);
}
