package com.martini.sparrow.elastic.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * 我们要给用户返回什么，看什么
 * 包括首页，搜索结果
 * @author martini at 2020/12/16 17:15
 */
@Document(indexName = "article", type = "article")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ArticleEsEntity {
    @Id
    private String id;

    @Field(type = FieldType.Keyword, index = false)
    private String picUrl;

    @Field(type = FieldType.Long)
    private Long articleId;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String articleTitle;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String content;

    @Field(type = FieldType.Long)
    private Long typeId;
    @Field(type = FieldType.Keyword)
    private String typeName;

    @Field(type = FieldType.Long)
    private Long bookId;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String bookName;

    @Field(type = FieldType.Long)
    private Long authorId;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String authorName;

    @Field(type = FieldType.Long)
    private Long userId;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String userName;
    @Field(type = FieldType.Keyword, index = false)
    private String userAvatar;

    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss")
    // JsonFormat冲突
    private Date publishTime;

    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @Field(type = FieldType.Integer)
    private Integer readCount;
    @Field(type = FieldType.Integer)
    private Integer likeCount;
    @Field(type = FieldType.Integer)
    private Integer favoriteCount;
    @Field(type = FieldType.Integer)
    private Integer commentCount;
}
