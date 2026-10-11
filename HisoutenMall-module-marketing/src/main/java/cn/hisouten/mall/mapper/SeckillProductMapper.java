package cn.hisouten.mall.mapper;

import cn.hisouten.mall.pojo.entity.SeckillProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SeckillProductMapper extends BaseMapper<SeckillProduct> {
    /**
     * 根据skuId查找秒杀商品
     * @param skuId skuId
     * @return 秒杀商品
     */
    @Select("select * from seckill_product where sku_id = #{skuId}")
    SeckillProduct selectBySkuId(Long skuId);

    /**
     * 查找开始时间在规定时间内的秒杀活动商品
     * @param time 分钟内开始的活动商品
     * @return 商品
     */
    List<SeckillProduct> selectAboutToStart(int time);

    /**
     * 查找所有正在进行中的秒杀活动商品
     * @return 商品
     */
    SeckillProduct[] selectAllActive();
}
