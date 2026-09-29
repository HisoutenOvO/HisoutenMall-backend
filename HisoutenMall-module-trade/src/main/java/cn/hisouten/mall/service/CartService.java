package cn.hisouten.mall.service;

import cn.hisouten.mall.pojo.dto.UserCartAddDTO;
import cn.hisouten.mall.pojo.dto.UserCartItemDeleteDTO;
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

    /**
     * 购物车修改数量
     * @param cartItemId 购物车单品id
     * @param quantity 最终数量
     */
    void updateQuantity(Long userId, Long cartItemId, Integer quantity);

    /**
     * 用户单条勾选购物车记录
     * @param cartItemId 购物车项id
     * @param checked 勾选状态
     */
    void updateChecked(Long userId, Long cartItemId, Integer checked);

    /**
     * 全选或全不选
     * @param checked 勾选状态
     */
    void checkAll(Integer checked);

    /**
     * 用户批量删除购物车数据
     * @param userCartItemDeleteDTO 删除的id
     * @param userId 用户id
     */
    void deleteBatch(UserCartItemDeleteDTO userCartItemDeleteDTO,Long userId);
}
