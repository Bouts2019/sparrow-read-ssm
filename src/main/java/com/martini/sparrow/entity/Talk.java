package com.martini.sparrow.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;

/**
 * @author martini at 2020/12/30 11:47
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Slf4j
@ToString
public class Talk {
    private String talkId;
    private Long userId;
    private String userAvatar;
    private String userNickname;
    private String userDesc;
    private Date publishTime;
    private Integer userGender;
    private String content;
    private List<String> imgList;
    private Integer tagId;
    private String tagName;
    // 以下两个属性应当从redis中获取
    private Integer likeCount;
    private Integer commentCount;
}
