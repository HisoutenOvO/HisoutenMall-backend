package cn.hisouten.mall.service;

import cn.hisouten.mall.pojo.dto.category.AdminCategoryAddDTO;
import cn.hisouten.mall.pojo.dto.category.AdminCategoryUpdateDTO;
import cn.hisouten.mall.pojo.vo.category.AdminCategoryListVO;
import cn.hisouten.mall.pojo.vo.category.CategoryListVO;
import cn.hisouten.mall.pojo.vo.category.AdminCategoryTreeVO;
import cn.hisouten.mall.pojo.vo.category.CategoryTreeVO;

import java.util.List;

public interface CategoryService {
    /**
     * 用户端和商家端分类列表查询
     * @return 返回分类列表
     */
    List<CategoryListVO> listQuery();

    /**
     * 通过分类id获取分类名称
     * @param categoryId 分类id
     * @return 分类名称
     */
    String getCategoryNameByCategoryId(Long categoryId);

    /**
     * 管理端分类列表查询
     * @return 返回值
     */
    List<AdminCategoryListVO> adminListQuery();

    /**
     * 管理端分类树形查询
     * @return 返回值
     */
    List<AdminCategoryTreeVO> treeQuery();

    /**
     * 新增分类
     * @param adminCategoryAddDTO 分类参数
     */
    void addCategory(AdminCategoryAddDTO adminCategoryAddDTO);

    /**
     * 修改分类
     * @param categoryId 分类id
     * @param adminCategoryUpdateDTO 修改分类参数
     */
    void updateCategory(Long categoryId, AdminCategoryUpdateDTO adminCategoryUpdateDTO);

    /**
     * 修改分类状态
     * @param categoryId 分类id
     * @param status 状态
     */
    void changeStatus(Long categoryId, Integer status);

    /**
     * 逻辑删除分类
     * @param categoryId 分类id
     */
    void logicDelete(Long categoryId);

    /**
     * 用户和商家端查询分类树形结构
     * @return 返回值
     */
    List<CategoryTreeVO> treeQueryOthers();
}
