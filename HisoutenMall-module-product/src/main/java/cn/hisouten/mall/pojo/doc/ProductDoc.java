package cn.hisouten.mall.pojo.doc;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品的es文档类
 */
@Data
@Document(indexName = "product")
public class ProductDoc {

    @Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String all;

    // 搜索字段
    @Field(type = FieldType.Text, analyzer = "ik_max_word", copyTo = "all")
    private String name;

    @Field(type = FieldType.Text, analyzer = "ik_max_word", copyTo = "all")
    private String subtitle;

    // 过滤字段
    @Field(type = FieldType.Long)
    private Long categoryId;

    @Field(type = FieldType.Long)
    private Long brandId;

    @Field(type = FieldType.Long)
    private Long merchantId;

    @Field(type = FieldType.Keyword, copyTo = "all")
    private String categoryName;

    @Field(type = FieldType.Keyword, copyTo = "all")
    private String brandName;

    @Field(type = FieldType.Long)
    private Long sales; // 销量

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private LocalDateTime createTime; // 新品时间

    // 价格与库存
    @Field(type = FieldType.Double)
    private BigDecimal minPrice; // 用作区间过滤和价格排序

    @Field(type = FieldType.Integer)
    private Integer totalStock;

    @Field(type = FieldType.Integer)
    private Integer status;

    @Field(type = FieldType.Keyword, index = false)
    private String mainImage;
}
