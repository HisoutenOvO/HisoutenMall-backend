package cn.hisouten.mall.mapper;

import cn.hisouten.mall.pojo.dto.AdminOrderPageQueryDTO;
import cn.hisouten.mall.pojo.dto.MerchantOrderPageQueryDTO;
import cn.hisouten.mall.pojo.dto.UserOrderPageQueryDTO;
import cn.hisouten.mall.pojo.entity.Order;
import cn.hisouten.mall.pojo.vo.AdminOrderPageResultVO;
import cn.hisouten.mall.pojo.vo.MerchantOrderPageResultVO;
import cn.hisouten.mall.pojo.vo.UserOrderPageResultVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
}
