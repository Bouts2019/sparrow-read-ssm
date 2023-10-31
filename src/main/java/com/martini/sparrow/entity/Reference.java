package com.martini.sparrow.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author martini at 2020/12/22 8:56
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Reference {
    private Integer id;
    private String from;
    private Integer count;
}
