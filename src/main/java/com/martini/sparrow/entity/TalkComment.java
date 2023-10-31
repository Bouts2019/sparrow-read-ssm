package com.martini.sparrow.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * talk板块的评论
 * @author martini at 2021/1/8 19:19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Slf4j
@ToString
public class TalkComment {
    private Long userId;
    private Long replyUserId;
    private Date commentTime;
    private String commentContent;
    private Integer likeCount;
    private Integer dislikeCount;
}
