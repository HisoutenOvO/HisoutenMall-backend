package cn.hisouten.mall.pojo.bo.brand;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用于中转brand分页查询VO
 */
@Data
public class BrandPageResultBO {
    private Long id;

    private String name;

    private String logo;

    private Integer sort;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer deleted;
}
