package cn.hisouten.mall.mapper;

import cn.hisouten.mall.pojo.entity.Brand;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BrandMapper extends BaseMapper<Brand> {

    /**
     * 通过品牌id获取品牌名称
     * @param brandId 品牌id
     * @return 品牌名称
     */
    @Select("select name from brand where id = #{brandId} and deleted = 0")
    String getBrandNameByBrandId(Long brandId);
}
