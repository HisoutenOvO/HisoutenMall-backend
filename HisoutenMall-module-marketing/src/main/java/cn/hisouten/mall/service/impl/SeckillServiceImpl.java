package cn.hisouten.mall.service.impl;

import cn.hisouten.mall.exception.BizException;
import cn.hisouten.mall.mapper.SeckillActivityMapper;
import cn.hisouten.mall.mapper.SeckillProductMapper;
import cn.hisouten.mall.pojo.PageResult;
import cn.hisouten.mall.pojo.dto.MerchantSeckillActivityPageQueryDTO;
import cn.hisouten.mall.pojo.dto.MerchantSeckillActivitySaveDTO;
import cn.hisouten.mall.pojo.entity.SeckillActivity;
import cn.hisouten.mall.pojo.vo.MerchantSeckillActivityDetailVO;
import cn.hisouten.mall.pojo.vo.MerchantSeckillActivityPageResultVO;
import cn.hisouten.mall.pojo.vo.MerchantSeckillProductVO;
import cn.hisouten.mall.service.SeckillService;
import cn.hisouten.mall.user.service.MerchantProfileService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static cn.hisouten.mall.constant.ExceptionMessageConstant.*;
import static cn.hisouten.mall.constant.SeckillConstant.*;
import static cn.hisouten.mall.constant.StatusConstant.DISABLED;

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
        String activityName = checkValidTimeAndName(merchantId,merchantSeckillActivitySaveDTO);
        //创建时校验开始时间
        if (merchantSeckillActivitySaveDTO.getStartTime().isBefore(LocalDateTime.now())) {
            throw new BizException(ACTIVITY_TIME_INVALID);
        }
        SeckillActivity seckillActivity = new SeckillActivity();
        seckillActivity.setName(activityName);
        seckillActivity.setStartTime(merchantSeckillActivitySaveDTO.getStartTime());
        seckillActivity.setEndTime(merchantSeckillActivitySaveDTO.getEndTime());
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
        SeckillActivity seckillActivity = checkValidAndTime(merchantId,activityId);
        String activityName = checkValidTimeAndName(merchantId, merchantSeckillActivitySaveDTO);
        seckillActivity.setName(activityName);
        seckillActivity.setStartTime(merchantSeckillActivitySaveDTO.getStartTime());
        seckillActivity.setEndTime(merchantSeckillActivitySaveDTO.getEndTime());
        seckillActivityMapper.updateById(seckillActivity);
    }


    /**
     * 商家端分页查询秒杀商品
     * @param merchantId 商家id
     * @param pageQueryDTO 分页参数
     * @return 返回值
     */
    @Override
    public PageResult<MerchantSeckillActivityPageResultVO> pageQueryMerchant(Long merchantId, MerchantSeckillActivityPageQueryDTO pageQueryDTO) {
        Page<SeckillActivity> page = new Page<>(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());
        if(pageQueryDTO.getDeleted() == null){
            pageQueryDTO.setDeleted(DISABLED);
        }
        Page<SeckillActivity> result = seckillActivityMapper.pageQuery(merchantId,page,pageQueryDTO);
        long total = result.getTotal();
        List<SeckillActivity> seckillActivityList = result.getRecords();
        List<MerchantSeckillActivityPageResultVO> records = new ArrayList<>();
        for (SeckillActivity activity : seckillActivityList) {
            MerchantSeckillActivityPageResultVO vo = new MerchantSeckillActivityPageResultVO();
            BeanUtils.copyProperties(activity,vo);
            records.add(vo);
        }
        return new PageResult<>(total,records);
    }

    /**
     * 商家端查询秒杀活动详情
     * @param merchantId 商家id
     * @param activityId 活动id
     * @return 返回值
     */
    @Override
    public MerchantSeckillActivityDetailVO detailQuery(Long merchantId, Long activityId) {
        SeckillActivity seckillActivity = checkValid(merchantId,activityId);
        MerchantSeckillActivityDetailVO detailVO = new MerchantSeckillActivityDetailVO();
        BeanUtils.copyProperties(seckillActivity,detailVO);
        //查询秒杀商品列表
        List<MerchantSeckillProductVO> productItemVOList = seckillProductMapper.selectSkusByActivityId(activityId);
        //拼装商品列表
        detailVO.setProducts(productItemVOList);
        return detailVO;
    }


    /**
     * 商家删除秒杀活动
     * @param merchantId 商家id
     * @param activityId 活动id
     */
    @Override
    @Transactional
    public void delete(Long merchantId, Long activityId) {
        checkValidAndTime(merchantId,activityId);
        seckillActivityMapper.deleteById(activityId);
        //级联删除秒杀商品
        seckillProductMapper.deleteByActivityId(activityId);
    }

    /**
     * 抽取公共方法——校验活动归属及是否存在
     * @param merchantId 商家id
     * @param activityId 活动id
     * @return 返回活动
     */
    private SeckillActivity checkValid(Long merchantId,Long activityId){
        SeckillActivity seckillActivity = seckillActivityMapper.selectById(activityId);
        if(seckillActivity == null){
            throw new BizException(ACTIVITY_NOT_FOUND);
        }
        //校验归属
        if(!seckillActivity.getMerchantId().equals(merchantId)){
            throw new BizException(NO_PERMISSION);
        }
        return seckillActivity;
    }

    /**
     * 抽取公共方法——校验活动归属及是否存在及是否在活动期间
     * @param merchantId 商家id
     * @param activityId 活动id
     * @return 返回商家
     */
    private SeckillActivity checkValidAndTime(Long merchantId,Long activityId){
        SeckillActivity seckillActivity = checkValid(merchantId, activityId);
        //活动正在进行则拒绝
        if (!Objects.equals(seckillActivity.getStatus(), PENDING)) {
            throw new BizException(ACTIVITY_HAS_START);
        }
        return seckillActivity;
    }

    /**
     * 抽取公共方法——校验时间和活动名的有效性
     * @param merchantId 商家id
     * @param merchantSeckillActivitySaveDTO 保存活动参数
     * @return 返回活动名
     */
    private String checkValidTimeAndName(Long merchantId,MerchantSeckillActivitySaveDTO merchantSeckillActivitySaveDTO){
        LocalDateTime startTime = merchantSeckillActivitySaveDTO.getStartTime();
        LocalDateTime endTime = merchantSeckillActivitySaveDTO.getEndTime();
        //只校验开始结束时间是否反了
        if (startTime == null || endTime == null || !endTime.isAfter(startTime)) {
            throw new BizException(ACTIVITY_TIME_INVALID);
        }
        //校验活动名
        String activityName = merchantSeckillActivitySaveDTO.getName();
        if (activityName == null || activityName.isEmpty()) {
            String merchantName = merchantProfileService.getMerchantNameByMerchantId(merchantId);
            activityName = merchantName + DEFAULT_ACTIVITY_NAME;
        }
        return activityName;
    }


}
