package cn.hisouten.mall.pojo.bo.activity;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 用于中转秒杀活动分页查询参数的BO类
 */
@Data
public class SeckillActivityPageQueryBO {

    @Size(max = 100, message = "搜索关键词过长")
    private String keyword;

    private Integer status;

    private Integer deleted;

    private String merchantName; //模糊查询
}
