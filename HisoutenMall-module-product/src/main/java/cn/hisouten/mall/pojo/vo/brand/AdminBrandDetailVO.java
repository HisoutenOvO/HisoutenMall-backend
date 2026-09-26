package cn.hisouten.mall.pojo.vo.brand;

import lombok.Data;

/**
 * 管理员查询品牌详情返回值
 */
@Data
public class AdminBrandDetailVO {
    private String name;

    private String logo;

    private Integer sort;

    private Integer status;

    private Integer deleted;
}
