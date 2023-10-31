package com.martini.sparrow.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @author martini at 2021/1/13 14:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Slf4j
@ToString
public class Feedback {
    private Long id;
    private Long uid;
    private String title;
    private String content;
    private Date datetime;
}
