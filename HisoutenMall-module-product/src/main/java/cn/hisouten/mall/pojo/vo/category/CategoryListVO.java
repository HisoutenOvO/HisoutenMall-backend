package cn.hisouten.mall.pojo.vo.category;

import lombok.Data;

/**
 * 用户和商家的列表查询分类返回值——商家用于回显，用户用于展示
 */
@Data
public class CategoryListVO {

    private Long id;

    private Long parentId;

    private String name;

    private Integer level;

    private Integer sort;

}