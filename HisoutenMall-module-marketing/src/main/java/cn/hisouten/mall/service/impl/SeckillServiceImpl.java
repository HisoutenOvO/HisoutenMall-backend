package cn.hisouten.mall.service.impl;

import cn.hisouten.mall.exception.BizException;
import cn.hisouten.mall.mapper.SeckillActivityMapper;
import cn.hisouten.mall.mapper.SeckillProductMapper;
import cn.hisouten.mall.pojo.PageResult;
import cn.hisouten.mall.pojo.dto.MerchantSeckillActivityPageQueryDTO;
import cn.hisouten.mall.pojo.dto.MerchantSeckillActivitySaveDTO;
import cn.hisouten.mall.pojo.dto.MerchantSeckillProductAddDTO;
import cn.hisouten.mall.pojo.dto.MerchantSeckillProductUpdateDTO;
import cn.hisouten.mall.pojo.entity.Product;
import cn.hisouten.mall.pojo.entity.ProductSku;
import cn.hisouten.mall.pojo.entity.SeckillActivity;
import cn.hisouten.mall.pojo.entity.SeckillProduct;
import cn.hisouten.mall.pojo.vo.MerchantSeckillActivityDetailVO;
import cn.hisouten.mall.pojo.vo.MerchantSeckillActivityPageResultVO;
import cn.hisouten.mall.pojo.vo.MerchantSeckillProductVO;
import cn.hisouten.mall.service.ProductService;
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
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.hisouten.mall.constant.ExceptionMessageConstant.*;
import static cn.hisouten.mall.constant.SeckillConstant.*;
import static cn.hisouten.mall.constant.StatusConstant.DISABLED;
import static cn.hisouten.mall.constant.StatusConstant.ENABLED;

@Service
@RequiredArgsConstructor
public class SeckillServiceImpl implements SeckillService {
    private final SeckillActivityMapper seckillActivityMapper;
    private final SeckillProductMapper seckillProductMapper;

    private final MerchantProfileService merchantProfileService;
    private final ProductService productService;

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
     * 商家端批量新增秒杀商品
     * @param merchantId 商家Id
     * @param addDTO 商品参数
     */
    @Override
    public void addProduct(Long merchantId, MerchantSeckillProductAddDTO addDTO) {
        SeckillActivity seckillActivity = checkValidAndTime(merchantId,addDTO.getActivityId());
        List<MerchantSeckillProductAddDTO.SeckillProductItem> seckillProductItems = addDTO.getItems();
        //取出内部类的skuId集合，通过productService提供的列表查询返回所有sku
        List<ProductSku> skuList = productService.getSkuListBySkuIds(seckillProductItems.stream().map(MerchantSeckillProductAddDTO.SeckillProductItem::getSkuId).collect(Collectors.toList()));
        //提前将seckillProductItems集合转成map，以便通过id提取对应对象
        Map<Long, MerchantSeckillProductAddDTO.SeckillProductItem> itemMap =
                seckillProductItems.stream()
                        .collect(Collectors.toMap(
                                MerchantSeckillProductAddDTO.SeckillProductItem::getSkuId,// 定义key
                                item -> item,                   //定义value
                                (a, b) -> a  // 防止前端传重复 skuId 时抛异常
                        ));
        //通过将前端返回的数据和查找到返回的数据输数量进行对比，若少了肯定是有被逻辑删除的商品
        if(seckillProductItems.size() != skuList.size()){
            throw new BizException(SKU_NOT_FOUND);
        }
        for (ProductSku sku : skuList) {
            //判断每个sku存在，未删除，状态启用
            if(sku == null || sku.getStatus() != ENABLED){
                throw new BizException(SKU_HAS_DISABLED);
            }

            //判断sku属于商家
            Product product = productService.getProductByProductId(sku.getProductId());
            if(!Objects.equals(product.getMerchantId(), merchantId)){
                throw new BizException(NO_PERMISSION);
            }

            //校验价格合理且小于原价
            MerchantSeckillProductAddDTO.SeckillProductItem item = itemMap.get(sku.getId());
            if(item.getSeckillPrice().longValue() <= 0 || item.getSeckillPrice().longValue() >= sku.getPrice().longValue()){
                throw new BizException(SECKILL_PRICE_INVALID);
            }

            //校验库存大于0且小于等于总库存
            if(item.getSeckillStock() <= 0 || item.getSeckillStock() > sku.getStock()){
                throw new BizException(SECKILL_STOCK_INVALID);
            }

            //校验限购大于等于1
            if(item.getLimitPerUser() < 1){
                throw new BizException(SECKILL_LIMIT_INVALID);
            }

            //校验同一sku不能同时存在于多个未结束的活动里
            List<SeckillActivity> activityList = seckillActivityMapper.selectActivityListBySkuIdExceptSelf(sku.getId(),seckillActivity.getId());
            if(!activityList.isEmpty()){
                throw new BizException(SECKILL_PRODUCT_INVALID);
            }
            SeckillProduct seckillProduct = SeckillProduct.builder()
                    .activityId(seckillActivity.getId())
                    .skuId(sku.getId())
                    .seckillPrice(item.getSeckillPrice())
                    .seckillStock(item.getSeckillStock())
                    .limitPerUser(item.getLimitPerUser())
                    .build();
            seckillProductMapper.insert(seckillProduct);
        }
    }

    /**
     * 商家端修改秒杀商品
     * @param merchantId 商家id
     * @param productId 秒杀商品id
     * @param updateDTO 修改参数
     */
    @Override
    public void updateProduct(Long merchantId, Long productId, MerchantSeckillProductUpdateDTO updateDTO) {
        SeckillProduct seckillProduct = seckillProductMapper.selectById(productId);
        if(seckillProduct == null){
            throw new BizException(PRODUCT_NOT_FOUND);
        }
        ProductSku sku = productService.getSkuBySkuId(seckillProduct.getSkuId());
        SeckillActivity seckillActivity = checkValidAndTime(merchantId,seckillProduct.getActivityId());

        //校验价格合理且小于原价
        if(updateDTO.getSeckillPrice().longValue() <= 0 || updateDTO.getSeckillPrice().longValue() >= sku.getPrice().longValue()){
            throw new BizException(SECKILL_PRICE_INVALID);
        }

        //校验库存大于0且小于等于总库存
        if(updateDTO.getSeckillStock() <= 0 || updateDTO.getSeckillStock() > sku.getStock()){
            throw new BizException(SECKILL_STOCK_INVALID);
        }

        //校验限购大于等于1
        if(updateDTO.getLimitPerUser() < 1){
            throw new BizException(SECKILL_LIMIT_INVALID);
        }

        seckillProduct.setSeckillPrice(updateDTO.getSeckillPrice());
        seckillProduct.setSeckillStock(updateDTO.getSeckillStock());
        seckillProduct.setLimitPerUser(updateDTO.getLimitPerUser());
        seckillProductMapper.updateById(seckillProduct);
    }

    /**
     * 商家端删除秒杀商品
     * @param merchantId 商家id
     * @param productId 商品id
     */
    @Override
    public void deleteProduct(Long merchantId, Long productId) {
        SeckillProduct seckillProduct = seckillProductMapper.selectById(productId);
        SeckillActivity seckillActivity = checkValidAndTime(merchantId,seckillProduct.getActivityId());
        seckillProductMapper.deleteById(productId);
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
