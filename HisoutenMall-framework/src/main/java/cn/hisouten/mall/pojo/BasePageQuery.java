package cn.hisouten.mall.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasePageQuery {

    private Integer page;

    private Integer pageSize;

}
