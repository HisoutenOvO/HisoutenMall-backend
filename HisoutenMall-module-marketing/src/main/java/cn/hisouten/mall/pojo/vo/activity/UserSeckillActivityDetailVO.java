package cn.hisouten.mall.pojo.vo.activity;

import cn.hisouten.mall.pojo.vo.product.UserSeckillProductVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户端查询秒杀商品活动返回值
 */
@Data
public class UserSeckillActivityDetailVO {

    private Long id;

    private String name;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer status;

    private Long remainSeconds;   // 距开始或结束剩余秒数（前端倒计时用）

    private List<UserSeckillProductVO> products;

}
