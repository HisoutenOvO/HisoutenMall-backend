package cn.hisouten.mall.user.service;

import cn.hisouten.mall.common.pojo.PageResult;
import cn.hisouten.mall.user.pojo.dto.AdminAuditMerchantDTO;
import cn.hisouten.mall.user.pojo.dto.AdminMerchantPageQueryDTO;
import cn.hisouten.mall.user.pojo.vo.admin.AdminMerchantDetailVO;
import cn.hisouten.mall.user.pojo.vo.admin.AdminMerchantPageResultVO;
import jakarta.validation.Valid;

public interface AdminMerchantService {

    /**
     * 商家分页查询
     * @param adminMerchantPageQueryDTO 分页查询参数
     * @return 返回值
     */
    PageResult<AdminMerchantPageResultVO> pageQuery(AdminMerchantPageQueryDTO adminMerchantPageQueryDTO);

    /**
     * 查询商家详情
     * @param merchantId 商家id
     * @return 返回值
     */
    AdminMerchantDetailVO detailQuery(Long merchantId);

    /**
     * 修改商家状态
     * @param merchantId 商家id
     * @param status 状态
     */
    void changeStatus(Long merchantId, Integer status);

    /**
     * 管理员审核商家
     * @param adminId 管理员id
     * @param merchantId 商家id
     * @param auditDTO 审核参数
     */
    void auditMerchant(Long adminId, Long merchantId, @Valid AdminAuditMerchantDTO auditDTO);
}
