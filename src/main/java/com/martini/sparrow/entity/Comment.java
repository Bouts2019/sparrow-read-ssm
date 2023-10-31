package com.martini.sparrow.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @author martini at 2021/1/7 16:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Slf4j
@ToString
public class Comment {
    private Long id;
    private Long articleId;
    private Long userId;
    private Long replyUserId;
    private Date commentTime;
    private String commentContent;
    private Integer likeCount;
    private Integer dislikeCount;
}
