package cn.hisouten.mall.service;

import cn.hisouten.mall.pojo.PageResult;
import cn.hisouten.mall.pojo.dto.UserOrderCreateDTO;
import cn.hisouten.mall.pojo.dto.UserOrderPageQueryDTO;
import cn.hisouten.mall.pojo.dto.UserOrderPayDTO;
import cn.hisouten.mall.pojo.vo.UserOrderCreateVO;
import cn.hisouten.mall.pojo.vo.UserOrderPageResultVO;

public interface OrderService {

    /**
     * 用户从购物车里结算下单
     * @param addressId 地址id
     * @return 返回值
     */
    UserOrderCreateVO createFromCart(Long userId, Long addressId);

    /**
     * 用户立即购买下单
     * @param userOrderCreateDTO 下单参数
     * @return 返回值
     */
    UserOrderCreateVO createDirect(Long userId, UserOrderCreateDTO userOrderCreateDTO);

    /**
     * 用户支付订单
     * @param userOrderPayDTO 支付订单参数
     */
    void pay(Long userId, UserOrderPayDTO userOrderPayDTO);

    /**
     * 订单取消支付
     * @param orderNo 订单编号
     */
    void cancelPay(Long userId, String orderNo);

    /**
     * 订单分页查询
     * @param userOrderPageQueryDTO 分页查询参数
     * @return 返回值
     */
    PageResult<UserOrderPageResultVO> pageQuery(UserOrderPageQueryDTO userOrderPageQueryDTO);
}
