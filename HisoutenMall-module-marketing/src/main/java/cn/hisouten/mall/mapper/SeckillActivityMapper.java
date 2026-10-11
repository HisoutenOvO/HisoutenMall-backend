package cn.hisouten.mall.mapper;

import cn.hisouten.mall.pojo.entity.SeckillActivity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;

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
}
