package cn.hisouten.mall.mapper;

import cn.hisouten.mall.pojo.entity.Category;
import cn.hisouten.mall.pojo.vo.category.CategoryListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
    /**
     * 获取分类列表基础信息
     * @return 返回分类列表
     */
    List<CategoryListVO> getList();

    /**
     * 通过分类id查询分类名称
     * @param categoryId 分类id
     * @return 返回分类名称
     */
    @Select("SELECT name from category where id = #{categoryId} and deleted = 0")
    String getCategoryNameByCategoryId(Long categoryId);
}
