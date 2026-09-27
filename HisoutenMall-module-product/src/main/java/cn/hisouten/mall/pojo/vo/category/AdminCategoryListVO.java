package cn.hisouten.mall.pojo.vo.category;

import lombok.Data;

/**
 * 管理员列表查询分类返回值
 */
@Data
public class AdminCategoryListVO {

    private Long id;

    private Long parentId;

    private String name;

    private Integer level;

    private Integer sort;

    private Integer status;

    private Integer deleted;

}
