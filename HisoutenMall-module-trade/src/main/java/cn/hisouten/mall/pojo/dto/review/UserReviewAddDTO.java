package cn.hisouten.mall.pojo.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 用户新增评价参数
 */
@Data
public class UserReviewAddDTO {

    @NotNull(message = "订单项ID不能为空")
    private Long orderItemId;

    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分最低为1星")
    @Max(value = 5, message = "评分最高为5星")
    private Integer rating;

    @Size(max = 1000, message = "评价内容不能超过1000字")
    private String content;

    @Size(max = 1000, message = "图片地址过长")
    private String images;

}