package com.pu.es;

import lombok.Data;
import java.time.LocalDateTime;
/*import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Data
@Document(indexName = "job_index")*/
public class JobES {
/*
    @Id
    private Long id;

    // 可全文搜索字段
    @Field(type = FieldType.Text,
            analyzer = "ik_max_word",
            searchAnalyzer = "ik_smart")
    private String title;

    @Field(type = FieldType.Text,
            analyzer = "ik_max_word")
    private String description;

    // 精确过滤字段
    @Field(type = FieldType.Keyword)
    private String company;

    @Field(type = FieldType.Double)
    private Double salary;

    @Field(type = FieldType.Integer)
    private Integer status;

    @Field(type = FieldType.Date)
    private LocalDateTime createTime;*/
}
