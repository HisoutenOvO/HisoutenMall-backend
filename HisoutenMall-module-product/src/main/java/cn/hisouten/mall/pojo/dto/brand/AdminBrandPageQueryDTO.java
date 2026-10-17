package cn.hisouten.mall.pojo.dto.brand;

import cn.hisouten.mall.common.pojo.BasePageQueryDTO;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 管理员分页查询品牌参数
 */
@Data
public class AdminBrandPageQueryDTO extends BasePageQueryDTO {

    @Size(max = 50, message = "搜索关键词过长")
    private String keyword;

    private Integer status;

    private Integer deleted;
}