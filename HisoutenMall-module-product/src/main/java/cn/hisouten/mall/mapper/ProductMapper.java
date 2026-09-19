package cn.hisouten.mall.mapper;

import cn.hisouten.mall.pojo.entity.Product;
import cn.hisouten.mall.pojo.vo.category.CategoryListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    /**
     * 绕过mybatis检测逻辑删除来查找真正的记录
     * @param productId 商品id
     * @return 返回真正的记录
     */
    @Select("select * from product where id = #{productId}")
    Product selectByIdIgnoreLogic(Long productId);

    /**
     * 恢复逻辑删除的商品
     * @param productId 商品id
     */
    @Update("update product set deleted = 0 where id = #{productId}")
    void recoveryProduct(Long productId);

    /**
     * 彻底删除商品数据
     * @param productId 商品id
     */
    @Delete("delete from product where id = #{productId}")
    void realDelete(Long productId);

    /**
     * 分页查询商品分类
     * @param page 分页参数
     * @param keyword 关键词
     * @return
     */
    Page<CategoryListVO> pageQuery(Page<Product> page, String keyword);
}
