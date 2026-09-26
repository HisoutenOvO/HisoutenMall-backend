package cn.hisouten.mall.mapper;

import cn.hisouten.mall.pojo.entity.ProductSku;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductSkuMapper extends BaseMapper<ProductSku> {

    /**
     * 批量插入商品SKU
     * @param productSkuList 新增的SKU列表
     */
    void insertBatch(List<ProductSku> productSkuList);

    /**
     * 查找该商品已有的SKU列表
     * @param productId 商品id
     * @return 返回可能存在的sku列表
     */
    List<ProductSku> selectExistSkuList(Long productId);

    /**
     * 物理删除商品对应的sku
     * @param productId 商品id
     */
    @Delete("delete from product_sku where product_id = #{productId}")
    void realDeleteByProductId(Long productId);

    /**
     * 根据商品id查找商品sku列表，不包括逻辑删除的和下架的
     * @param productId 商品id
     * @return 返回商品列表
     */
    @Select("select * from product_sku where product_id = #{productId} and status = 1 and deleted = 0")
    List<ProductSku> selectEnabledByProductId(Long productId);

    /**
     * 根据商品id查找商品sku列表，不包括逻辑删除的
     * @param productId 商品id
     * @return 返回商品列表
     */
    @Select("select * from product_sku where product_id = #{productId} and deleted = 0")
    List<ProductSku> selectByProductId(Long productId);

    /**
     * 根据商品id查找所有商品sku列表，包括逻辑删除的
     * @param productId 商品id
     * @return 返回商品列表
     */
    @Select("select * from product_sku where product_id = #{productId}")
    List<ProductSku> selectAllByProductId(Long productId);
}
