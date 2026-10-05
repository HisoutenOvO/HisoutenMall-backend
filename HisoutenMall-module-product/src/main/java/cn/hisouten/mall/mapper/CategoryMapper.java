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
    @Select("SELECT name from category where id = #{categoryId}")
    String getCategoryNameByCategoryId(Long categoryId);

    /**
     * 同级同名查重
     * @param name 新增的名字
     * @param parentId 父级id
     * @param categoryId 排除自己的id
     * @return 可能存在的名字
     */
    String selectExistCategoryName(String name, Long parentId,Long categoryId);

    /**
     * 查询该分类下子分类数量
     * @param categoryId 分类id
     * @return 返回子分类数量
     */
    @Select("select count(0) from category where parent_id = #{categoryId} and deleted = 0x")
    Integer getChildCategoryCount(Long categoryId);

    /**
     * 查询没下架且未删除的分类
     * @return 返回值
     */
    @Select("select * from category where status = 1 and deleted = 0")
    List<Category> selectListWithoutRemove();
}
