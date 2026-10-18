package cn.hisouten.mall.mapper;

import cn.hisouten.mall.pojo.dto.order.AdminOrderPageQueryDTO;
import cn.hisouten.mall.pojo.dto.order.MerchantOrderPageQueryDTO;
import cn.hisouten.mall.pojo.dto.order.UserOrderPageQueryDTO;
import cn.hisouten.mall.pojo.entity.Order;
import cn.hisouten.mall.pojo.vo.order.AdminOrderPageResultVO;
import cn.hisouten.mall.pojo.vo.order.DailySalesVO;
import cn.hisouten.mall.pojo.vo.order.MerchantOrderPageResultVO;
import cn.hisouten.mall.pojo.vo.order.UserOrderPageResultVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    /**
     * 根据订单号查找订单
     * @param orderNo 订单号
     * @return 返回值
     */
    @Select("select * from `order` where order_no = #{orderNo}")
    Order selectByOrderNo(String orderNo);

    /**
     * 根据订单创建时间搜索订单
     * @param deadLineTime 订单创建时间——截止时间
     * @return 返回值
     */
    @Select("select * from `order` where status = 0 and deleted = 0 and create_time <= #{deadLineTime}")
    List<Order> selectByCreateTime(LocalDateTime deadLineTime);

    /**
     * 用户分页查询订单
     * @param page 分页条件
     * @param userOrderPageQueryDTO 条件参数
     * @return 返回值
     */
    Page<UserOrderPageResultVO> pageQuery(Page<UserOrderPageResultVO> page,@Param("dto") UserOrderPageQueryDTO userOrderPageQueryDTO);

    /**
     * 商家分页查询订单
     * @param page 分页条件
     * @param merchantOrderPageQueryDTO 查询条件
     * @return 返回值
     */
    Page<MerchantOrderPageResultVO> MerchantPageQuery(Page<MerchantOrderPageResultVO> page, @Param("dto") MerchantOrderPageQueryDTO merchantOrderPageQueryDTO,Long merchantId);

    /**
     * 管理员分页查询订单
     * @param page 分页参数
     * @param adminOrderPageQueryDTO 条件参数
     * @return 返回值
     */
    Page<AdminOrderPageResultVO> AdminPageQuery(Page<AdminOrderPageResultVO> page,@Param("dto") AdminOrderPageQueryDTO adminOrderPageQueryDTO);

    /**
     * 获取商家今日营业额
     * @param merchantId 商家id
     * @return 返回值
     */
    BigDecimal selectTodaySales(Long merchantId);


    /**
     * 获取商家今日订单数
     * @param merchantId 商家id
     * @return 返回值
     */
    Long selectTodayOrderCount(Long merchantId);

    /**
     * 获取商家累计销售额
     * @param merchantId 商家id
     * @return 返回值
     */
    BigDecimal selectTotalSales(Long merchantId);

    /**
     * 获取商家待发货数量
     * @param merchantId 商家id
     * @return 返回值
     */
    @Select("select count(*) from `order` where merchant_id = #{merchantId} and status = 1")
    Long selectPendingShipCount(Long merchantId);

    /**
     * 获取今日平台总销售额
     * @return 返回值
     */
    @Select("select ifnull(sum(pay_amount), 0) from `order` where status in (1,2,3) and date(create_time) = curdate()")
    BigDecimal selectTodayPlatformSales();

    /**
     * 获取今日平台总订单数
     * @return 返回值
     */
    @Select("select count(*) from `order` where date(create_time) = curdate()")
    Long selectTodayPlatformOrderCount();

    /**
     * 获取近7天销售趋势
     * @return 返回值
     */
    List<DailySalesVO> getSalesTrend();

}
