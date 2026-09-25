package cn.hisouten.mall.pojo.vo.category;

import lombok.Data;

/**
 * 通用的列表查询分类返回值
 */
@Data
public class CategoryListVO {

    private Long id;

    private Long parentId;

    private String name;

    private Integer level;

    private Integer sort;

}