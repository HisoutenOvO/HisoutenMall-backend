package cn.hisouten.mall.controller.Admin;

import cn.hisouten.mall.pojo.PageResult;
import cn.hisouten.mall.pojo.Result;
import cn.hisouten.mall.pojo.dto.AdminOrderPageQueryDTO;
import cn.hisouten.mall.pojo.vo.AdminOrderPageResultVO;
import cn.hisouten.mall.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Slf4j
@Tag(name = "管理端——订单接口")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    /**
     * 分页查询订单
     * @param adminOrderPageResultDTO 分页参数
     * @return 返回值
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询订单")
    public Result<PageResult<AdminOrderPageResultVO>> pageQuery(AdminOrderPageQueryDTO adminOrderPageResultDTO){
        log.info("管理员分页查询订单");
        PageResult<AdminOrderPageResultVO> pageResult = orderService.adminPageQuery(adminOrderPageResultDTO);
        return Result.success(pageResult);
    }
}
