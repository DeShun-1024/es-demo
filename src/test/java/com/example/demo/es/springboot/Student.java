package com.example.demo.es.springboot;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


@Document(indexName = "lx-sd2")
@Data
public class Student {

    @Id
    @Field(index = false,type = FieldType.Integer)
    private Integer id;
    /**
     * index:是否设置分词  默认为true
     * analyzer：储存时使用的分词器
     * searchAnalyze:搜索时使用的分词器
     * store：是否存储  默认为false
     * type：数据类型  默认值是FieldType.Auto
     *
     */
//    @Field(analyzer = "ik_smart",searchAnalyzer = "ik_smart",store = true,type = FieldType.Text)
    @Field(store = true,type = FieldType.Text)
    private String name;

    @Field(index = false,type = FieldType.Integer)
    private  Integer age;

    private String interest;
//
//    private LocalDateTime birthday;
}
