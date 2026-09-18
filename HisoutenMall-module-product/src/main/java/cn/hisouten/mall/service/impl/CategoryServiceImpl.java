package cn.hisouten.mall.service.impl;

import cn.hisouten.mall.mapper.CategoryMapper;
import cn.hisouten.mall.pojo.entity.Category;
import cn.hisouten.mall.pojo.vo.category.CategoryListVO;
import cn.hisouten.mall.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        return categoryMapper.getList();
    }
}
