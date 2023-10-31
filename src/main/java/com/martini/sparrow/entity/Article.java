package com.martini.sparrow.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * 文章
 * @author martini at 2020/12/15 8:06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Slf4j
@ToString
public class Article {
    private Long id;
    private Long bookId;
    private Long userId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private String title;
    private String content;
    private Integer likeCount;
    private Integer favoriteCount;
    private Integer commentCount;
    private Integer readCount;
    private String picUrl;
    /**
     * status:
     *  0 -> ok,
     *  1 -> waiting for approving,
     *  2 -> disapprove
     *  3 -> deleted
     */
    private Integer status;
}
