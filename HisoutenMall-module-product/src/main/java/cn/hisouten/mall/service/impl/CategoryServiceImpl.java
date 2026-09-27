package cn.hisouten.mall.service.impl;

import cn.hisouten.mall.exception.businessexception.*;
import cn.hisouten.mall.mapper.CategoryMapper;
import cn.hisouten.mall.mapper.ProductMapper;
import cn.hisouten.mall.pojo.dto.category.AdminCategoryAddDTO;
import cn.hisouten.mall.pojo.dto.category.AdminCategoryUpdateDTO;
import cn.hisouten.mall.pojo.entity.Category;
import cn.hisouten.mall.pojo.vo.category.AdminCategoryListVO;
import cn.hisouten.mall.pojo.vo.category.CategoryListVO;
import cn.hisouten.mall.pojo.vo.category.CategoryTreeVO;
import cn.hisouten.mall.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static cn.hisouten.mall.constant.ExceptionMessageConstant.*;
import static cn.hisouten.mall.constant.StatusConstant.ENABLED;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;


    /**
     * 商家端和用户端分类列表查询
     * @return 返回分类列表
     */
    @Override
    public List<CategoryListVO> listQuery() {
       List<Category> categoryList = categoryMapper.selectList(null);
       List<CategoryListVO> categoryVOList = new ArrayList<>();
        for (Category category : categoryList) {
            //只返回上架的
            if(category.getStatus() == ENABLED){
                CategoryListVO categoryListVO = new CategoryListVO();
                categoryListVO.setId(category.getId());
                categoryListVO.setName(category.getName());
                categoryListVO.setParentId(category.getParentId());
                categoryListVO.setLevel(category.getLevel());
                categoryListVO.setSort(category.getSort());
                categoryVOList.add(categoryListVO);
            }

        }
        return categoryVOList;
    }

    /**
     * 管理端分类列表查询
     * @return 返回分类列表
     */
    @Override
    public List<AdminCategoryListVO> adminListQuery() {
        List<Category> categoryList = categoryMapper.selectList(null);
        List<AdminCategoryListVO> categoryVOList = new ArrayList<>();
        for (Category category : categoryList) {
            AdminCategoryListVO categoryListVO = new AdminCategoryListVO();
            categoryListVO.setId(category.getId());
            categoryListVO.setName(category.getName());
            categoryListVO.setParentId(category.getParentId());
            categoryListVO.setLevel(category.getLevel());
            categoryListVO.setSort(category.getSort());
            categoryListVO.setStatus(category.getStatus());
            categoryListVO.setDeleted(category.getDeleted());
            categoryVOList.add(categoryListVO);
        }
        return categoryVOList;
    }

    /**
     * 分类树形查询
     * @return 返回值
     */
    @Override
    public List<CategoryTreeVO> treeQuery() {
        List<Category> categoryList = categoryMapper.selectList(null);
        //组装树结构，用抽取端方法递归
        return buildTree(categoryList,0L); //从0——根节点开始
    }

    /**
     * 新增分类
     * @param adminCategoryAddDTO 分类参数
     */
    @Override
    public void addCategory(AdminCategoryAddDTO adminCategoryAddDTO) {
        //同级同名校验
        String existedName = categoryMapper.selectExistCategoryName(adminCategoryAddDTO.getName(),adminCategoryAddDTO.getParentId(),null);
        if(existedName != null){
            throw new CategoryNameAlreadyExistException(CATEGORY_NAME_ALREADY_EXIST);
        }
        //设置层级属性level
        Integer level;
        if (adminCategoryAddDTO.getParentId() == 0L) {
            level = 1;
        } else {
            Category parent = categoryMapper.selectById(adminCategoryAddDTO.getParentId());
            if (parent == null) {
                throw new CategoryNotFoundException(CATEGORY_NOT_FOUND);
            }
            if (parent.getLevel() >= 3) {
                throw new LevelOverflowException(LEVEL_OVERFLOW);
            }
            level = parent.getLevel() + 1;
        }
        Category category = new Category();
        BeanUtils.copyProperties(adminCategoryAddDTO, category);
        category.setLevel(level);
        category.setStatus(ENABLED);
        categoryMapper.insert(category);
    }

    /**
     * 修改分类
     * @param categoryId 分类id
     * @param adminCategoryUpdateDTO 修改分类参数
     */
    @Override
    public void updateCategory(Long categoryId, AdminCategoryUpdateDTO adminCategoryUpdateDTO) {
        Category category = categoryMapper.selectById(categoryId);
        if(category == null){
            throw new CategoryNotFoundException(CATEGORY_NOT_FOUND);
        }
        // 同级同名检查，排除自己
        String existed = categoryMapper.selectExistCategoryName(adminCategoryUpdateDTO.getName(),category.getParentId(), categoryId);
        if (existed != null) {
            throw new CategoryNameAlreadyExistException(CATEGORY_NAME_ALREADY_EXIST);
        }
        category.setName(adminCategoryUpdateDTO.getName());
        category.setSort(adminCategoryUpdateDTO.getSort());
        categoryMapper.updateById(category);
    }

    /**
     * 修改分类状态
     * @param categoryId 分类id
     * @param status 状态
     */
    @Override
    public void changeStatus(Long categoryId, Integer status) {
        Category category = categoryMapper.selectById(categoryId);
        if(category == null){
            throw new CategoryNotFoundException(CATEGORY_NOT_FOUND);
        }
        category.setStatus(status);
        categoryMapper.updateById(category);
    }

    /**
     * 逻辑删除分类
     * @param categoryId 分类id
     */
    @Override
    public void logicDelete(Long categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        if(category == null){
            throw new CategoryNotFoundException(CATEGORY_NOT_FOUND);
        }
        //查询是否有子分类
        Integer categoryCount = categoryMapper.getChildCategoryCount(categoryId);
        if(categoryCount > 0){
            throw new CategoryRelatedChildrenException(CATEGORY_RELATED_CHILDREN);
        }
        //查询分类下是否有商品数据
        Integer productCount = productMapper.getProductCountByCategoryId(categoryId);
        if(productCount != 0){
            throw new CategoryRelatedProductException(CATEGORY_RELATED_PRODUCT);
        }
        categoryMapper.deleteById(categoryId);
    }

    /**
     * 组装树递归方法
     * @param all 所有的分类
     * @param parentId 父级分类id
     * @return 返回值
     */
    private List<CategoryTreeVO> buildTree(List<Category> all, Long parentId) {
        List<CategoryTreeVO> result = new ArrayList<>();
        for (Category c : all) {
            if (c.getParentId().equals(parentId)) {
                CategoryTreeVO vo = new CategoryTreeVO();
                BeanUtils.copyProperties(c, vo);
                vo.setChildren(buildTree(all, c.getId()));
                result.add(vo);
            }
        }
        return result;
    }

    /**
     * 通过分类id获取分类名称
     * @param categoryId 分类id
     * @return 分类名称
     */
    @Override
    public String getCategoryNameByCategoryId(Long categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        if(category == null){
            throw new CategoryNotFoundException(CATEGORY_NOT_FOUND);
        }
        return categoryMapper.getCategoryNameByCategoryId(categoryId);
    }
}
