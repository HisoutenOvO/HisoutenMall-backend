package cn.hisouten.mall.service;

import cn.hisouten.mall.pojo.dto.UserCartAddDTO;
import cn.hisouten.mall.pojo.vo.UserCartItemListVO;

import java.util.List;

public interface CartService {

    /**
     * 用户新增购物车
     * @param userCartAddDTO 新增参数
     */
    void addCart(Long userId, UserCartAddDTO userCartAddDTO);

    /**
     * 用户查询购物车列表
     * @param userId 用户id
     * @return 返回值
     */
    List<UserCartItemListVO> listQuery(Long userId);
}
