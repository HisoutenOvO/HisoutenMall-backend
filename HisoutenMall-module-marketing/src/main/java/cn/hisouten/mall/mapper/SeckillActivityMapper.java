package cn.hisouten.mall.mapper;

import cn.hisouten.mall.pojo.bo.activity.SeckillActivityPageQueryBO;
import cn.hisouten.mall.pojo.dto.activity.MerchantSeckillActivityPageQueryDTO;
import cn.hisouten.mall.pojo.entity.SeckillActivity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface SeckillActivityMapper extends BaseMapper<SeckillActivity> {

    /**
     * 更新开始的秒杀活动状态
     * @param now 现在时间
     * @return 返回值
     */
    @Update("UPDATE seckill_activity SET status = 1 " +
            "WHERE status = 0 AND start_time <= #{now} AND end_time > #{now}")
    int startActivities(LocalDateTime now);

    /**
     * 更新结束的秒杀活动状态
     * @param now 当前时间
     * @return 返回值
     */
    @Update("UPDATE seckill_activity SET status = 2 " +
            "WHERE status = 1 AND end_time <= #{now}")
    int endActivities( LocalDateTime now);

    /**
     * 分页查询秒杀活动
     * @param merchantId 商家id
     * @param page 分页条件
     * @param pageQueryBO 查询条件
     * @return 返回值
     */
    Page<SeckillActivity> pageQuery(Long merchantId,Page<SeckillActivityPageQueryBO> page, SeckillActivityPageQueryBO pageQueryBO);

    /**
     * 根据skuId查询秒杀活动，排出自己
     * @param skuId skuId
     * @param activityId 活动id
     * @return 返回值
     */
    List<SeckillActivity> selectActivityListBySkuIdExceptSelf(Long skuId,Long activityId);

    /**
     * 查询未开始或进行中的秒杀活动
     * @return 返回值
     */
    List<SeckillActivity> selectValidActivityList();

    /**
     * 查询已结束但还没来得及修改状态的活动
     * @param now 现在时间
     * @return 返回值
     */
    @Select("select * from seckill_activity " +
            "where status = 1 and end_time <= #{now} and deleted = 0")
    List<SeckillActivity> selectEndingActivities(LocalDateTime now);
}
