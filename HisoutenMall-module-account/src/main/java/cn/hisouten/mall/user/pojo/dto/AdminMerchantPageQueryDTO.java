package cn.hisouten.mall.user.pojo.dto;

import cn.hisouten.mall.pojo.BasePageQueryDTO;
import lombok.Data;

/**
 * 管理员分页查询商家参数
 */
@Data
public class AdminMerchantPageQueryDTO extends BasePageQueryDTO {
    private String keyword;

    private Integer status;

    private Integer deleted;
}
