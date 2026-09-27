package cn.hisouten.mall.user.service;

import cn.hisouten.mall.pojo.PageResult;
import cn.hisouten.mall.user.pojo.dto.AdminMerchantPageQueryDTO;
import cn.hisouten.mall.user.pojo.vo.AdminMerchantDetailVO;
import cn.hisouten.mall.user.pojo.vo.AdminMerchantPageResultVO;

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
}
