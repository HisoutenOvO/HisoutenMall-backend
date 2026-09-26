package cn.hisouten.mall.pojo.dto.brand;

import lombok.Data;

/**
 * 管理端修改品牌参数
 */
@Data
public class AdminBrandUpdateDTO {
    private String name;

    private String logo;

    private Integer sort;
}
