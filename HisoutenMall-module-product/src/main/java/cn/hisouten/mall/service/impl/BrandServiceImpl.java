package cn.hisouten.mall.service.impl;

import cn.hisouten.mall.exception.businessexception.BrandNameAlreadyExist;
import cn.hisouten.mall.exception.businessexception.BrandNotFoundException;
import cn.hisouten.mall.exception.businessexception.BrandRelatedProductException;
import cn.hisouten.mall.mapper.BrandMapper;
import cn.hisouten.mall.pojo.PageResult;
import cn.hisouten.mall.pojo.bo.brand.BrandPageQueryBO;
import cn.hisouten.mall.pojo.bo.brand.BrandPageResultBO;
import cn.hisouten.mall.pojo.dto.brand.AdminBrandAddDTO;
import cn.hisouten.mall.pojo.dto.brand.AdminBrandPageQueryDTO;
import cn.hisouten.mall.pojo.dto.brand.AdminBrandUpdateDTO;
import cn.hisouten.mall.pojo.entity.Brand;
import cn.hisouten.mall.pojo.vo.brand.AdminBrandDetailVO;
import cn.hisouten.mall.pojo.vo.brand.AdminBrandPageResultVO;
import cn.hisouten.mall.pojo.vo.brand.BrandListVO;
import cn.hisouten.mall.service.BrandService;
import cn.hisouten.mall.service.ProductService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static cn.hisouten.mall.constant.ExceptionMessageConstant.*;
import static cn.hisouten.mall.constant.StatusConstant.DISABLED;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandMapper brandMapper;
    private final ProductService productService;

    /**
     * 通过品牌id获取品牌名称
     * @param brandId 品牌id
     * @return 品牌名称
     */
    @Override
    public String getBrandNameByBrandId(Long brandId) {
        return brandMapper.getBrandNameByBrandId(brandId);
    }

    /**
     * 获取品牌列表
     * @return 返回值
     */
    @Override
    public List<BrandListVO> listQuery() {
        List<Brand> brandList = brandMapper.selectList(null);
        List<BrandListVO> brandVOList = new ArrayList<>();
        for (Brand brand : brandList) {
            BrandListVO brandListVO = new BrandListVO();
            brandListVO.setId(brand.getId());
            brandListVO.setName(brand.getName());
            brandListVO.setLogo(brand.getLogo());
            brandListVO.setSort(brand.getSort());
            brandVOList.add(brandListVO);
        }
        return brandVOList;
    }

    /**
     * 管理端分页查询品牌
     * @param adminBrandPageQueryDTO 分页查询参数
     * @return 返回值
     */
    @Override
    public PageResult<AdminBrandPageResultVO> pageQuery(AdminBrandPageQueryDTO adminBrandPageQueryDTO) {
        Page<BrandPageQueryBO> page = new Page<>(adminBrandPageQueryDTO.getPage(), adminBrandPageQueryDTO.getPageSize());
        BrandPageQueryBO brandPageQueryBO = new BrandPageQueryBO();
        BeanUtils.copyProperties(adminBrandPageQueryDTO,brandPageQueryBO);
        // 如果前端没传deleted条件，默认查未删除的
        if (brandPageQueryBO.getDeleted() == null) {
            brandPageQueryBO.setDeleted(DISABLED);
        }
        Page<BrandPageResultBO> result = brandMapper.pageQuery(page,brandPageQueryBO);
        long total = result.getTotal();
        List<AdminBrandPageResultVO> records = new ArrayList<>();
        for (BrandPageResultBO bo : result.getRecords()) {
            AdminBrandPageResultVO adminBrandPageResultVO = AdminBrandPageResultVO.builder()
                    .id(bo.getId())
                    .name(bo.getName())
                    .logo(bo.getLogo())
                    .sort(bo.getSort())
                    .status(bo.getStatus())
                    .createTime(bo.getCreateTime())
                    .updateTime(bo.getUpdateTime())
                    .deleted(bo.getDeleted())
                    .build();
            records.add(adminBrandPageResultVO);
        }
        return new PageResult<>(total,records);
    }

    /**
     * 查询品牌详情
     * @param brandId 品牌id
     * @return 返回值
     */
    @Override
    public AdminBrandDetailVO detailQuery(Long brandId) {
        Brand brand = brandMapper.selectById(brandId);
        if(brand == null){
            throw new BrandNotFoundException(BRAND_NOT_FOUND);
        }
        AdminBrandDetailVO adminBrandDetailVO = new AdminBrandDetailVO();
        BeanUtils.copyProperties(brand,adminBrandDetailVO);
        return adminBrandDetailVO;
    }

    /**
     * 新增品牌
     * @param adminBrandAddDTO 新增品牌参数
     */
    @Override
    public void addBrand(AdminBrandAddDTO adminBrandAddDTO) {
        Brand brand = new Brand();
        String existedName = brandMapper.getExistName(adminBrandAddDTO.getName(),null);
        if(existedName != null){
            throw new BrandNameAlreadyExist(BRAND_NAME_ALREADY_EXIST);
        }
        BeanUtils.copyProperties(adminBrandAddDTO,brand);
        brandMapper.insert(brand);
    }

    /**
     * 修改品牌
     * @param adminBrandUpdateDTO 修改参数
     */
    @Override
    public void updateBrand(Long brandId, AdminBrandUpdateDTO adminBrandUpdateDTO) {
        Brand brand = brandMapper.selectById(brandId);
        if(brand == null){
            throw new BrandNotFoundException(BRAND_NOT_FOUND);
        }
        String existedName = brandMapper.getExistName(adminBrandUpdateDTO.getName(),brandId);
        if(existedName != null){
            throw new BrandNameAlreadyExist(BRAND_NAME_ALREADY_EXIST);
        }
        BeanUtils.copyProperties(adminBrandUpdateDTO,brand);
        brandMapper.updateById(brand);
    }

    /**
     * 修改品牌上下架状态
     * @param brandId 品牌id
     * @param status 状态
     */
    @Override
    public void changeStatus(Long brandId,Integer status) {
        Brand brand = brandMapper.selectById(brandId);
        if(brand == null){
            throw new BrandNotFoundException(BRAND_NOT_FOUND);
        }
        brand.setStatus(status);
        brandMapper.updateById(brand);
    }

    /**
     * 逻辑删除品牌
     * @param brandId 品牌id
     */
    @Override
    public void logicDelete(Long brandId) {
        Brand brand = brandMapper.selectById(brandId);
        if(brand == null){
            throw new BrandNotFoundException(BRAND_NOT_FOUND);
        }
        //若有相关产品则不可删除
        Long count = productService.getProductCountByBrandId(brandId);
        if(count > 0){
            throw new BrandRelatedProductException(BRAND_RELATED_PRODUCT);
        }
        brandMapper.deleteById(brandId);
    }


}
