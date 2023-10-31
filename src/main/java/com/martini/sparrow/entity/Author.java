package com.martini.sparrow.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 图书作者实体
 * @author martini at 2020/12/16 9:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Author implements Serializable {

    private static final long serialVersionUID = -5520394960775614280L;

    private Long id;
    private String name;
    private String desc;
}
