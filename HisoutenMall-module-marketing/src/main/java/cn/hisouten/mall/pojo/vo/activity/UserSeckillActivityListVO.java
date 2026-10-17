package cn.hisouten.mall.pojo.vo.activity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户端列表查询秒杀活动返回值
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSeckillActivityListVO {

    private Long id;

    private String name;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer status;

    private Integer productCount;

}
