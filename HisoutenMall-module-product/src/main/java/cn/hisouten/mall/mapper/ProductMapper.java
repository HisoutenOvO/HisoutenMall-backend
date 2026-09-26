package cn.hisouten.mall.mapper;

import cn.hisouten.mall.pojo.bo.product.ProductPageQueryBO;
import cn.hisouten.mall.pojo.entity.Product;
import cn.hisouten.mall.pojo.bo.product.ProductPageResultBO;
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
    void realDeleteById(Long productId);

    /**
     * 分页查询商品
     * @param page 分页参数
     * @param productPageQueryBO 查询条件-用PageQueryBO中转
     * @return 返回值，用PageResultBO中转
     */
    Page<ProductPageResultBO> pageQuery(Page<ProductPageResultBO> page, @Param("dto") ProductPageQueryBO productPageQueryBO);

    /**
     * 检查同一商家下是否有同名的商品
     * @param name 商品名
     * @param merchantId 商家id
     * @param productId 商品id，用于排除自己
     * @return 可能存在的名字
     */
    String selectExistedProductName(String name,Long merchantId,Long productId);

    /**
     * 根据品牌id查询商品数量
     * @param brandId 品牌id
     * @return 返回值
     */
    @Select("select count(0) from product where brand_id = #{brandId} ")
    Long getCountByBrandId(Long brandId);
}
