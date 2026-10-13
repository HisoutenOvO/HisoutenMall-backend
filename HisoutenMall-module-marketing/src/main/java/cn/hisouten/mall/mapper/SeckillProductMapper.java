package cn.hisouten.mall.mapper;

import cn.hisouten.mall.pojo.bo.SeckillProductBO;
import cn.hisouten.mall.pojo.entity.SeckillProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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

    /**
     * 根据秒杀活动id查询对应的秒杀商品BO
     * @param activityId 活动id
     * @return 返回值
     */
    List<SeckillProductBO> selectSkusByActivityId(Long activityId);

    /**
     * 根据活动id删除秒杀商品
     * @param activityId 活动id
     */
    @Delete("delete from seckill_product where activity_id = #{activityId}")
    void deleteByActivityId(Long activityId);

    /**
     * 扣减秒杀商品库存
     * @param seckillSkuId 秒杀商品id
     * @param quantity 扣减数量
     * @return 返回影响数
     */
    @Update("update seckill_product set seckill_stock = seckill_stock - #{quantity} where id = #{seckillSkuId} and seckill_stock >= #{quantity}")
    int deductSeckillStock(Long seckillSkuId, int quantity);

    /**
     * 根据活动id查询对应秒杀商品
     * @param activityId 活动id
     * @return 返回值
     */
    @Select("select * from seckill_product where activity_id = #{activityId}")
    List<SeckillProduct> selectByActivityId(Long activityId);

    /**
     * 回滚秒杀商品库存方法
     * @param skuId skuId
     * @param quantity 回滚数量
     */
    @Update("UPDATE seckill_product SET seckill_stock = seckill_stock + #{quantity} WHERE sku_id = #{skuId}")
    void restoreSeckillStock(Long skuId, Integer quantity);

    /**
     * 查询秒杀订单数
     * @param userId 用户id
     * @param skuId skuId
     * @return 返回值
     */
    @Select("SELECT COUNT(1) FROM `order` o " +
            "INNER JOIN order_item oi ON o.id = oi.order_id " +
            "WHERE o.user_id = #{userId} AND o.source = 1 AND oi.sku_id = #{skuId} AND o.deleted = 0 AND o.status != 2")
    int countSeckillOrder(Long userId, Long skuId);
}
