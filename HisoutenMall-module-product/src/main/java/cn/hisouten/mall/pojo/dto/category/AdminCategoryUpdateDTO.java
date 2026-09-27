package cn.hisouten.mall.pojo.dto.category;

import lombok.Data;

/**
 * 管理端修改分类参数
 */
@Data
public class AdminCategoryUpdateDTO {

    private String name;

    private Integer sort;

}
