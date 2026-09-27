package cn.hisouten.mall.user.pojo.dto;

import cn.hisouten.mall.pojo.BasePageQuery;
import lombok.Data;

/**
 * 管理员用户分页查询参数
 */
@Data
public class AdminUserPageQueryDTO extends BasePageQuery {
    private String keyword;

    private Integer gender;

    private Integer status;

    private Integer deleted;
}
