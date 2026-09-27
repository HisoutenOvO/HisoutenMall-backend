package cn.hisouten.mall.pojo.vo.category;

import lombok.Data;

/**
 * 管理员查询分类详情返回值
 */
@Data
public class AdminCategoryDetailVO {

    private Long id;

    private Long parentId;

    private String name;

    private Integer level;

    private Integer sort;

    private Integer status;

}
