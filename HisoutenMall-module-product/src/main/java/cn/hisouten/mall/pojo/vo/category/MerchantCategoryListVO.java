package cn.hisouten.mall.pojo.vo.category;

import lombok.Data;

@Data
public class MerchantCategoryListVO {

    private Long id;

    private Long parentId;

    private String name;

    private Integer level;

    private Integer sort;

    private Integer status;

    private Integer deleted;

}
