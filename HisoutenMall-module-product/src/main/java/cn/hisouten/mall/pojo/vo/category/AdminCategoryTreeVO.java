package cn.hisouten.mall.pojo.vo.category;

import lombok.Data;

import java.util.List;

/**
 * 管理端查询分类树形结构返回值
 */
@Data
public class AdminCategoryTreeVO {

    private Long id;

    private Long parentId;

    private String name;

    private Integer level;

    private Integer sort;

    private Integer status;

    private List<AdminCategoryTreeVO> children;

}
