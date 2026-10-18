package cn.hisouten.mall.user.pojo.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * 管理员审核商家参数
 */
@Data
public class AdminAuditMerchantDTO {

    @NotNull(message = "审核状态不能为空")
    @Min(value = 1, message = "审核状态不合法")
    @Max(value = 2, message = "审核状态不合法")
    private Integer auditStatus; //1-pass 2-reject

    private String auditReason;

}
