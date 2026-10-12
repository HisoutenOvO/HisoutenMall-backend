package cn.hisouten.mall.service.impl;

import cn.hisouten.mall.exception.BizException;
import cn.hisouten.mall.mapper.SeckillActivityMapper;
import cn.hisouten.mall.mapper.SeckillProductMapper;
import cn.hisouten.mall.pojo.dto.MerchantSeckillActivitySaveDTO;
import cn.hisouten.mall.pojo.entity.SeckillActivity;
import cn.hisouten.mall.service.SeckillService;
import cn.hisouten.mall.user.service.MerchantProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static cn.hisouten.mall.constant.ExceptionMessageConstant.*;
import static cn.hisouten.mall.constant.SeckillConstant.*;

@Service
@RequiredArgsConstructor
public class SeckillServiceImpl implements SeckillService {
    private final SeckillActivityMapper seckillActivityMapper;
    private final SeckillProductMapper seckillProductMapper;

    private final MerchantProfileService merchantProfileService;

    /**
     * 商家端新增秒杀活动
     * @param merchantSeckillActivitySaveDTO 活动参数
     */
    @Override
    public void createActivity(Long merchantId, MerchantSeckillActivitySaveDTO merchantSeckillActivitySaveDTO) {
        //校验时间是否有效
        LocalDateTime startTime = merchantSeckillActivitySaveDTO.getStartTime();
        LocalDateTime endTime = merchantSeckillActivitySaveDTO.getEndTime();
        if(endTime.isBefore(startTime) || startTime.isBefore(LocalDateTime.now())){
            throw new BizException(ACTIVITY_TIME_INVALID);
        }
        //校验活动名
        String activityName = merchantSeckillActivitySaveDTO.getName();
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

    /**
     * 商家端修改秒杀活动
     * @param activityId 活动id
     * @param merchantSeckillActivitySaveDTO 活动参数
     */
    @Override
    public void updateActivity(Long merchantId, Long activityId, MerchantSeckillActivitySaveDTO merchantSeckillActivitySaveDTO) {
        SeckillActivity seckillActivity = seckillActivityMapper.selectById(activityId);
        if(seckillActivity == null){
            throw new BizException(ACTIVITY_NOT_FOUND);
        }
        //校验归属
        if(!seckillActivity.getMerchantId().equals(merchantId)){
            throw new BizException(NO_PERMISSION);
        }
        //校验时间是否有效
        LocalDateTime startTime = merchantSeckillActivitySaveDTO.getStartTime();
        LocalDateTime endTime = merchantSeckillActivitySaveDTO.getEndTime();
        if(endTime.isBefore(startTime) || startTime.isBefore(LocalDateTime.now())){
            throw new BizException(ACTIVITY_TIME_INVALID);
        }
        //活动正在进行则拒绝
        if(seckillActivity.getStatus() == RUNNING){
            throw new BizException(ACTIVITY_HAS_START);
        }
        //校验活动名
        String activityName = merchantSeckillActivitySaveDTO.getName();
        if (activityName == null || activityName.isEmpty()) {
            String merchantName = merchantProfileService.getMerchantNameByMerchantId(merchantId);
            activityName = merchantName + DEFAULT_ACTIVITY_NAME;
        }
        seckillActivity.setName(activityName);
        seckillActivity.setStartTime(startTime);
        seckillActivity.setEndTime(endTime);
        seckillActivityMapper.updateById(seckillActivity);
    }


}
