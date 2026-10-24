package cn.hisouten.mall.user.pojo.dto;

import cn.hisouten.mall.common.pojo.BasePageQueryDTO;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 管理员分页查询商家参数
 */
@Data
public class AdminMerchantPageQueryDTO extends BasePageQueryDTO {

    @Size(max = 50, message = "搜索关键词过长")
    private String keyword;

    private Integer status;

    private Integer deleted;

    private Integer auditStatus;
}