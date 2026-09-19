package cn.hisouten.mall.service;

import cn.hisouten.mall.pojo.vo.category.CategoryListVO;

import java.util.List;

public interface CategoryService {
    /**
     * 用户列表查询分类
     * @return 返回分类列表
     */
    List<CategoryListVO> listQuery();

    /**
     * 通过分类id获取分类名称
     * @param categoryId 分类id
     * @return 分类名称
     */
    String getCategoryNameByCategoryId(Long categoryId);
}
