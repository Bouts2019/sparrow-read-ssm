package com.martini.sparrow.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * TODO: 测试用实体类
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Document(type = "poem", indexName = "poems")
public class Poem {

    @Id
    private String id;
    @Field(analyzer = "ik_max_word", type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Keyword)
    private String author;

    @Field(type = FieldType.Keyword)
    private String type;

    @Field(analyzer = "ik_max_word", type = FieldType.Text)
    private String content;

    @Field(type = FieldType.Keyword)
    private String href;

    @Field(analyzer = "ik_max_word", type = FieldType.Text)
    private String authordes;

    @Field(type = FieldType.Keyword)
    private String origin;

}

