package cn.hisouten.mall.user.pojo.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 管理员分页查询商家返回值
 */
@Data
@NoArgsConstructor
public class AdminMerchantPageResultVO {
    private Long id;

    private String username;

    private Integer status;

    private Integer deleted;

    private String shopName;

    private String shopLogo;

    private String contactPhone;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
