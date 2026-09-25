package cn.hisouten.mall.mapper;

import cn.hisouten.mall.pojo.entity.ProductSKU;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductSkuMapper extends BaseMapper<ProductSKU> {

    /**
     * 批量插入商品SKU
     * @param productSkuList 新增的SKU列表
     */
    void insertBatch(List<ProductSKU> productSkuList);
}
