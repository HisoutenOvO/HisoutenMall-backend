package cn.hisouten.mall.service;

import cn.hisouten.mall.pojo.dto.UserCartAddDTO;

public interface CartService {

    /**
     * 用户新增购物车
     * @param userCartAddDTO 新增参数
     */
    void addCart(Long userId, UserCartAddDTO userCartAddDTO);
}
