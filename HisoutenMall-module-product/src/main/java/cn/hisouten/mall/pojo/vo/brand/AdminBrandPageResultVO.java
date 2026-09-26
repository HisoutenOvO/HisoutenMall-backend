package cn.hisouten.mall.pojo.vo.brand;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

/**
 * 管理员分页查询品牌返回值
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminBrandPageResultVO {
    private Long id;

    private String name;

    private String logo;

    private Integer sort;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer deleted;
}
