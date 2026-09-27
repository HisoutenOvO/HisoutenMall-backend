package cn.hisouten.mall.mapper;

import cn.hisouten.mall.pojo.entity.Category;
import cn.hisouten.mall.pojo.vo.category.CategoryListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 通过分类id查询分类名称
     * @param categoryId 分类id
     * @return 返回分类名称
     */
    @Select("SELECT name from category where id = #{categoryId} and deleted = 0")
    String getCategoryNameByCategoryId(Long categoryId);

    /**
     * 同级同名查重
     * @param name 新增的名字
     * @param parentId 父级id
     * @return 可能存在的名字
     */
    @Select("select name from category where name = #{name} and parent_id = #{parentId}")
    String selectExistCategoryName(String name, Long parentId);
}
