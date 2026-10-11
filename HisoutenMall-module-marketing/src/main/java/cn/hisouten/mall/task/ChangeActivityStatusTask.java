package cn.hisouten.mall.task;

import cn.hisouten.mall.mapper.SeckillActivityMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 改变秒杀活动状态任务
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ChangeActivityStatusTask {
    private final SeckillActivityMapper seckillActivityMapper;

    /**
     * 每分钟扫描一次是否有秒杀活动需要改变状态
     */
    @Scheduled(cron = "0 * * * * ?")
    public void changeStatus() {
        LocalDateTime now = LocalDateTime.now();
        int started = seckillActivityMapper.startActivities(now);
        int ended = seckillActivityMapper.endActivities(now);
        if (started > 0 || ended > 0) {
            log.info("[秒杀状态] 开始 {} 个，结束 {} 个", started, ended);
        }
    }
}
