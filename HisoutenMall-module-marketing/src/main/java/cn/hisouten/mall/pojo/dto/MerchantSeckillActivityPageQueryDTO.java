package cn.hisouten.mall.pojo.dto;

import cn.hisouten.mall.pojo.BasePageQuery;
import lombok.Data;

/**
 * 商家端分页查询秒杀活动参数
 */
@Data
public class MerchantSeckillActivityPageQueryDTO extends BasePageQuery {

    private String keyword;

    private Integer status;

    private Integer deleted;
}
