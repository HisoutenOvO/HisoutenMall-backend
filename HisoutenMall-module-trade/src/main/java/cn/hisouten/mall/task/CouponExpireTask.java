package cn.hisouten.mall.task;

import cn.hisouten.mall.mapper.UserCouponMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 过期优惠券处理任务
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CouponExpireTask {

    private final UserCouponMapper userCouponMapper;

    /**
     * 每分钟扫描一次，把过期的未使用优惠券状态改为已过期
     */
    @Scheduled(cron = "0 * * * * ?")
    public void expireCoupons() {
        LocalDateTime now = LocalDateTime.now();
        int count = userCouponMapper.expireCoupons(now);
        if (count > 0) {
            log.info("[优惠券定时任务] 有 {} 张优惠券已过期", count);
        }
    }
}