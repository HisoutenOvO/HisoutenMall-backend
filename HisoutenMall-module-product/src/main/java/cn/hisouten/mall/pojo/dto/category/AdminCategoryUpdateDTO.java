package cn.hisouten.mall.pojo.dto.category;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 管理端修改分类参数
 */
@Data
public class AdminCategoryUpdateDTO {

    @NotBlank(message = "分类名称不能为空")
    @Size(max = 50, message = "分类名称不能超过50字")
    private String name;

    @Min(value = 0, message = "排序值不能为负数")
    private Integer sort;

}