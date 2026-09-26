package cn.hisouten.mall.service;

import cn.hisouten.mall.pojo.PageResult;
import cn.hisouten.mall.pojo.dto.brand.AdminBrandAddDTO;
import cn.hisouten.mall.pojo.dto.brand.AdminBrandPageQueryDTO;
import cn.hisouten.mall.pojo.dto.brand.AdminBrandUpdateDTO;
import cn.hisouten.mall.pojo.vo.brand.AdminBrandDetailVO;
import cn.hisouten.mall.pojo.vo.brand.AdminBrandPageResultVO;
import cn.hisouten.mall.pojo.vo.brand.BrandListVO;

import java.util.List;

public interface BrandService {

    /**
     * 通过品牌id获取品牌名称
     * @param brandId 品牌id
     * @return 品牌名称
     */
    String getBrandNameByBrandId(Long brandId);

    /**
     * 获取品牌列表
     * @return 返回值
     */
    List<BrandListVO> listQuery();

    /**
     * 品牌分页查询
     * @param adminBrandPageQueryDTO 分页查询参数
     * @return 返回值
     */
    PageResult<AdminBrandPageResultVO> pageQuery(AdminBrandPageQueryDTO adminBrandPageQueryDTO);

    /**
     * 查询品牌详情
     * @param brandId 品牌id
     * @return 返回值
     */
    AdminBrandDetailVO detailQuery(Long brandId);

    /**
     * 新增品牌
     * @param adminBrandAddDTO 新增品牌参数
     */
    void addBrand(AdminBrandAddDTO adminBrandAddDTO);

    /**
     * 修改品牌
     * @param adminBrandUpdateDTO 修改参数
     */
    void updateBrand(Long brandId, AdminBrandUpdateDTO adminBrandUpdateDTO);

    /**
     * 修改品牌上下架状态
     * @param brandId 品牌id
     * @param status 状态
     */
    void changeStatus(Long brandId,Integer status);

    /**
     * 逻辑删除品牌
     * @param brandId 品牌id
     */
    void logicDelete(Long brandId);
}
