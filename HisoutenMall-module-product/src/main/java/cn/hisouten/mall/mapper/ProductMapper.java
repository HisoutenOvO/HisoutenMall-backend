package cn.hisouten.mall.mapper;

import cn.hisouten.mall.pojo.dto.product.ProductPageQueryDTO;
import cn.hisouten.mall.pojo.entity.Product;
import cn.hisouten.mall.pojo.bo.product.ProductListBO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.*;

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
     * 分页查询商品
     * @param page 分页参数
     * @param productPageQueryDTO 查询条件
     * @return
     */
    Page<ProductListBO> pageQuery(Page<Product> page, @Param("dto") ProductPageQueryDTO productPageQueryDTO);
}
