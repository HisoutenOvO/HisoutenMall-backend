package cn.hisouten.mall.mapper;

import cn.hisouten.mall.pojo.bo.brand.BrandPageQueryBO;
import cn.hisouten.mall.pojo.bo.brand.BrandPageResultBO;
import cn.hisouten.mall.pojo.entity.Brand;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BrandMapper extends BaseMapper<Brand> {

    /**
     * 通过品牌id获取品牌名称
     * @param brandId 品牌id
     * @return 品牌名称
     */
    @Select("select name from brand where id = #{brandId} and deleted = 0")
    String getBrandNameByBrandId(Long brandId);

    /**
     * 品牌分页查询
     * @param page page对象
     * @param brandPageQueryBO 查询参数
     * @return 返回值
     */
    Page<BrandPageResultBO> pageQuery(Page<BrandPageQueryBO> page, @Param("dto") BrandPageQueryBO brandPageQueryBO);

    /**
     * 查找可能重复的品牌名
     * @param name 新增品牌名
     * @param brandId 排除自己的品牌id
     * @return 可能存在的品牌名
     */

    String getExistName(String name,Long brandId);

    /**
     * 查询品牌列表
     * @return 返回值
     */
    @Select("select * from brand where deleted = 0 and status = 1")
    List<Brand> selectBrandList();
}
