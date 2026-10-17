package cn.hisouten.mall.pojo.dto.activity;

import cn.hisouten.mall.common.pojo.BasePageQueryDTO;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 商家端分页查询秒杀活动参数
 */
@Data
public class MerchantSeckillActivityPageQueryDTO extends BasePageQueryDTO {

    @Size(max = 100, message = "搜索关键词过长")
    private String keyword;

    private Integer status;

    private Integer deleted;
}