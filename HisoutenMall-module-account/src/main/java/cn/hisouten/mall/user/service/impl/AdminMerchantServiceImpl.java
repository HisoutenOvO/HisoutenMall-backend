package cn.hisouten.mall.user.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.exception.BizException;
import cn.hisouten.mall.common.pojo.PageResult;
import cn.hisouten.mall.service.ProductService;
import cn.hisouten.mall.user.mapper.AuthMapper;
import cn.hisouten.mall.user.mapper.MerchantProfileMapper;
import cn.hisouten.mall.user.pojo.dto.AdminAuditMerchantDTO;
import cn.hisouten.mall.user.pojo.dto.AdminMerchantPageQueryDTO;
import cn.hisouten.mall.user.pojo.entity.MerchantProfile;
import cn.hisouten.mall.user.pojo.entity.User;
import cn.hisouten.mall.user.pojo.vo.merchant.AdminMerchantDetailVO;
import cn.hisouten.mall.user.pojo.vo.merchant.AdminMerchantPageResultVO;
import cn.hisouten.mall.user.service.AdminMerchantService;
import cn.hisouten.mall.util.CacheClientUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static cn.hisouten.mall.constant.ExceptionMessageConstant.*;
import static cn.hisouten.mall.constant.RedisConstant.CACHE_MERCHANT_DETAIL_PREFIX;
import static cn.hisouten.mall.constant.RoleConstant.ADMIN_ROLE;
import static cn.hisouten.mall.constant.RoleConstant.MERCHANT_ROLE;
import static cn.hisouten.mall.constant.StatusConstant.*;

@Service
@RequiredArgsConstructor
public class AdminMerchantServiceImpl implements AdminMerchantService {
    private final MerchantProfileMapper merchantProfileMapper;
    private final AuthMapper authMapper;
    private final ProductService productService;

    private final CacheClientUtils cacheClientUtils;

    /**
     * 商家分页查询
     * @param adminMerchantPageQueryDTO 分页查询参数
     * @return 返回值
     */
    @Override
    public PageResult<AdminMerchantPageResultVO> pageQuery(AdminMerchantPageQueryDTO adminMerchantPageQueryDTO) {
        Page<AdminMerchantPageResultVO> page = new Page<>(adminMerchantPageQueryDTO.getPage(),adminMerchantPageQueryDTO.getPageSize());
        if(adminMerchantPageQueryDTO.getDeleted() == null){
            adminMerchantPageQueryDTO.setDeleted(DISABLED);
        }
        Page<AdminMerchantPageResultVO> result = merchantProfileMapper.pageQuery(page,adminMerchantPageQueryDTO);
        long total = result.getTotal();
        List<AdminMerchantPageResultVO> records = result.getRecords();
        return new PageResult<>(total,records);
    }

    /**
     * 查询商家详情
     * @param merchantId 商家id
     * @return 返回值
     */
    @Override
    public AdminMerchantDetailVO detailQuery(Long merchantId) {
        User user = authMapper.selectById(merchantId);
        if(user == null){
            throw new BizException(USER_NOT_FOUND);
        }
        MerchantProfile merchantProfile = merchantProfileMapper.selectProfileByMerchantId(merchantId);
        return AdminMerchantDetailVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .status(user.getStatus())
                .shopName(merchantProfile.getShopName())
                .shopLogo(merchantProfile.getShopLogo())
                .shopDescription(merchantProfile.getShopDescription())
                .contactPhone(merchantProfile.getContactPhone())
                .businessLicense(merchantProfile.getBusinessLicense())
                .auditStatus(merchantProfile.getAuditStatus())
                .auditReason(merchantProfile.getAuditReason())
                .build();
    }

    /**
     * 修改商家状态
     * @param merchantId 商家id
     * @param status 状态
     */
    @Override
    @Transactional
    public void changeStatus(Long merchantId, Integer status) {
        User user = authMapper.selectById(merchantId);
        if(user == null){
            throw new BizException(USER_NOT_FOUND);
        }
        if(!user.getRole().equals(MERCHANT_ROLE)){
            throw new BizException(ROLE_NOT_MERCHANT);
        }
        user.setStatus(status);
        authMapper.updateById(user);

        //如果是下架则下架所有商品
        if(status == DISABLED){
            productService.disableAllProductByMerchantId(merchantId);
        }
        StpUtil.kickout(merchantId);
        cacheClientUtils.delete(CACHE_MERCHANT_DETAIL_PREFIX,merchantId);
    }

    /**
     * 管理员审核商家
     * @param adminId 管理员id
     * @param merchantId 商家id
     * @param auditDTO 审核参数
     */
    @Override
    public void auditMerchant(Long adminId, Long merchantId, AdminAuditMerchantDTO auditDTO) {
        User merchant = authMapper.selectById(merchantId);
        if(merchant == null || merchant.getRole() != MERCHANT_ROLE){
            throw new BizException(ROLE_NOT_MERCHANT);
        }
        MerchantProfile merchantProfile = merchantProfileMapper.selectProfileByMerchantId(merchantId);
        if(merchantProfile == null){
            throw new BizException(MERCHANT_NOT_FOUND);
        }
        if(merchantProfile.getAuditStatus() != PENDING_AUDIT){
            throw new BizException(MERCHANT_AUDIT_ERROR);
        }
        merchantProfile.setAuditStatus(auditDTO.getAuditStatus());
        if(auditDTO.getAuditStatus() == REJECT){
            merchantProfile.setAuditReason(auditDTO.getAuditReason());
        }
        merchantProfileMapper.updateById(merchantProfile);
    }
}
