package cn.hisouten.mall.pojo.dto.category;

import lombok.Data;

/**
 * 管理员新增分类参数
 */
@Data
public class AdminCategoryAddDTO {
    private Long parentId;

    private String name;

    private Integer sort;

    private Integer status;
}
