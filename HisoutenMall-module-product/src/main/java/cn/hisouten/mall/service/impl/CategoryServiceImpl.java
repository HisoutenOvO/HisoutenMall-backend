package cn.hisouten.mall.service.impl;

import cn.hisouten.mall.mapper.CategoryMapper;
import cn.hisouten.mall.pojo.entity.Category;
import cn.hisouten.mall.pojo.vo.category.CategoryListVO;
import cn.hisouten.mall.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;


    /**
     * 分类列表查询
     * @return 返回分类列表
     */
    @Override
    public List<CategoryListVO> listQuery() {
       List<Category> categoryList = categoryMapper.selectList(null);
       List<CategoryListVO> categoryVOList = new ArrayList<>();
        for (Category category : categoryList) {
            CategoryListVO categoryListVO = new CategoryListVO();
            categoryListVO.setId(category.getId());
            categoryListVO.setName(category.getName());
            categoryListVO.setParentId(category.getParentId());
            categoryListVO.setLevel(category.getLevel());
            categoryListVO.setSort(category.getSort());
            categoryVOList.add(categoryListVO);
        }
        return categoryVOList;
    }

    /**
     * 通过分类id获取分类名称
     * @param categoryId 分类id
     * @return 分类名称
     */
    @Override
    public String getCategoryNameByCategoryId(Long categoryId) {
        return categoryMapper.getCategoryNameByCategoryId(categoryId);
    }
}
