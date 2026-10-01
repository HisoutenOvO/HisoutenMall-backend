package cn.hisouten.mall.controller.merchant;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.pojo.PageResult;
import cn.hisouten.mall.pojo.Result;
import cn.hisouten.mall.pojo.dto.MerchantOrderPageQueryDTO;
import cn.hisouten.mall.pojo.vo.MerchantOrderPageResultVO;
import cn.hisouten.mall.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("merchantOrderController")
@RequestMapping("/merchant/order")
@Slf4j
@Tag(name = "商家端——订单接口")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    /**
     * 分页查询订单
     * @param merchantOrderPageQueryDTO 查询条件
     * @return 返回值
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询订单")
    public Result<PageResult<MerchantOrderPageResultVO>> pageQuery(@RequestBody MerchantOrderPageQueryDTO  merchantOrderPageQueryDTO){
        log.info("商家：{}分页查询订单", StpUtil.getLoginIdAsLong());
        PageResult<MerchantOrderPageResultVO> pageResult = orderService.merchantPageQuery(merchantOrderPageQueryDTO);
        return Result.success(pageResult);
    }
}
