package com.martini.sparrow.service;

import com.martini.sparrow.entity.Message;

/**
 * @author martini at 2021/1/12 14:10
 */
public interface MessageService {
    Long pushMessage(Long uid, Message message);

    Long messageToFans(Long userId, Message message);
}
