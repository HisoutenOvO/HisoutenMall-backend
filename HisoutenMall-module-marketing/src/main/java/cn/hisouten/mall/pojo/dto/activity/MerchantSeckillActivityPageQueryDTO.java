package cn.hisouten.mall.pojo.dto.activity;

import cn.hisouten.mall.pojo.BasePageQueryDTO;
import lombok.Data;

/**
 * 商家端分页查询秒杀活动参数
 */
@Data
public class MerchantSeckillActivityPageQueryDTO extends BasePageQueryDTO {

    private String keyword;

    private Integer status;

    private Integer deleted;
}
