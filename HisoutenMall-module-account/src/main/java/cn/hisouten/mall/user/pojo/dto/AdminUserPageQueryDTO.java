package cn.hisouten.mall.user.pojo.dto;

import cn.hisouten.mall.common.pojo.BasePageQueryDTO;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 管理员用户分页查询参数
 */
@Data
public class AdminUserPageQueryDTO extends BasePageQueryDTO {

    @Size(max = 50, message = "搜索关键词过长")
    private String keyword;

    private Integer gender;

    private Integer status;

    private Integer deleted;
}