package cn.hisouten.mall.pojo.dto.brand;

import lombok.Data;

/**
 * 管理端新增品牌参数
 */
@Data
public class AdminBrandAddDTO {
    private String name;

    private String logo;

    private Integer sort;

    private Integer status;
}
