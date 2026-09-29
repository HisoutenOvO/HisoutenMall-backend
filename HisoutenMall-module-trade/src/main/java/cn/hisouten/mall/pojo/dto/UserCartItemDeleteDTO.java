package cn.hisouten.mall.pojo.dto;

import lombok.Data;

import java.util.List;

/**
 * 用户批量删除购物车参数
 */
@Data
public class UserCartItemDeleteDTO {
    private List<Long> ids;
}
