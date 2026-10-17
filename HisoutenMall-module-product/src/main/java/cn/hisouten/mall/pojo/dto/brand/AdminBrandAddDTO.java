package cn.hisouten.mall.pojo.dto.brand;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 管理端新增品牌参数
 */
@Data
public class AdminBrandAddDTO {

    @NotBlank(message = "品牌名称不能为空")
    @Size(max = 50, message = "品牌名称不能超过50字")
    private String name;

    @Size(max = 500, message = "品牌Logo链接过长")
    private String logo;

    @Min(value = 0, message = "排序值不能为负数")
    private Integer sort;

    private Integer status;
}