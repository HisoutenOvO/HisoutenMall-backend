package cn.hisouten.mall.service.impl;

import cn.hisouten.mall.exception.BizException;
import cn.hisouten.mall.mapper.SeckillActivityMapper;
import cn.hisouten.mall.mapper.SeckillProductMapper;
import cn.hisouten.mall.pojo.dto.MerchantSeckillActivityAddDTO;
import cn.hisouten.mall.pojo.entity.SeckillActivity;
import cn.hisouten.mall.service.SeckillService;
import cn.hisouten.mall.user.pojo.entity.MerchantProfile;
import cn.hisouten.mall.user.service.MerchantProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static cn.hisouten.mall.constant.ExceptionMessageConstant.ACTIVITY_TIME_INVALID;
import static cn.hisouten.mall.constant.SeckillConstant.DEFAULT_ACTIVITY_NAME;
import static cn.hisouten.mall.constant.SeckillConstant.PENDING;
import static cn.hisouten.mall.constant.StatusConstant.DISABLED;

@Service
@RequiredArgsConstructor
public class SeckillServiceImpl implements SeckillService {
    private final SeckillActivityMapper seckillActivityMapper;
    private final SeckillProductMapper seckillProductMapper;

    private final MerchantProfileService merchantProfileService;

    /**
     * 商家端新增秒杀活动
     * @param merchantSeckillActivityAddDTO 活动参数
     */
    @Override
    public void createActivity(Long merchantId, MerchantSeckillActivityAddDTO merchantSeckillActivityAddDTO) {
        //校验时间是否有效
        LocalDateTime startTime = merchantSeckillActivityAddDTO.getStartTime();
        LocalDateTime endTime = merchantSeckillActivityAddDTO.getEndTime();
        if(endTime.isBefore(startTime) || startTime.isBefore(LocalDateTime.now())){
            throw new BizException(ACTIVITY_TIME_INVALID);
        }
        //校验活动名
        String activityName = merchantSeckillActivityAddDTO.getName();
        if (activityName == null || activityName.isEmpty()) {
            String merchantName = merchantProfileService.getMerchantNameByMerchantId(merchantId);
            activityName = merchantName + DEFAULT_ACTIVITY_NAME;
        }
        SeckillActivity seckillActivity = new SeckillActivity();
        seckillActivity.setName(activityName);
        seckillActivity.setStartTime(startTime);
        seckillActivity.setEndTime(endTime);
        seckillActivity.setMerchantId(merchantId);
        seckillActivity.setStatus(PENDING);
        seckillActivityMapper.insert(seckillActivity);
    }
}
