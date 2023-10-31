package com.martini.sparrow.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotEmpty;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Date;

/**
 * @author martini at 2020/12/14 17:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Book implements Serializable {
    private static final long serialVersionUID = 4136224787950698018L;
    private Long id;
    private String isbn;
    private String imgUrl;
    @NotEmpty
    private String title;
    /**
     * for translated book, it's useful
     * 原始书名
     */
    @Nullable
    private String originTitle = "";
    @Nullable
    private String subtitle = "";
    @Nullable
    private String originSubtitle = "";
    private Long authorId;
    private Long typeId;
    private Long expressId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expressDate;
    private Integer stars1;
    private Integer stars2;
    private Integer stars3;
    private Integer stars4;
    private Integer stars5;
    private String description;
}
