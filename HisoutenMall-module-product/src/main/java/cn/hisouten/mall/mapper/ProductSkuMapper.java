package cn.hisouten.mall.mapper;

import cn.hisouten.mall.pojo.entity.ProductSku;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

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
}
