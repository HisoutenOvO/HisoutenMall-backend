package cn.hisouten.mall.pojo.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户查询物流返回值
 */
@Data
@NoArgsConstructor
public class UserLogisticsVO {

    private String company;

    private String trackingNo;

    private Integer status;

    private LocalDateTime shipTime; //即物流的createTime

    private LocalDateTime updateTime;

}
