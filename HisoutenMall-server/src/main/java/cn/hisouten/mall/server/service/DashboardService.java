package cn.hisouten.mall.server.service;

import cn.hisouten.mall.server.vo.AdminDashboardVO;
import cn.hisouten.mall.server.vo.MerchantDashboardVO;

public interface DashboardService {

    /**
     * 商家获取统计信息
     * @param merchantId 商家id
     * @return 返回值
     */
    MerchantDashboardVO merchantStatsQuery(Long merchantId);

    /**
     * 管理员获取统计信息
     * @return 返回值
     */
    AdminDashboardVO adminSTatsQuery();
}
