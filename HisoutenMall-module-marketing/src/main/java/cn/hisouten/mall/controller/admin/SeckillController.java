package cn.hisouten.mall.controller.admin;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.common.pojo.PageResult;
import cn.hisouten.mall.common.pojo.Result;
import cn.hisouten.mall.exception.BizException;
import cn.hisouten.mall.pojo.dto.activity.AdminSeckillActivityPageQueryDTO;
import cn.hisouten.mall.pojo.vo.activity.AdminSeckillActivityPageResultVO;
import cn.hisouten.mall.service.SeckillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.hisouten.mall.constant.ExceptionMessageConstant.NO_PERMISSION;
import static cn.hisouten.mall.constant.RoleConstant.ADMIN;

@RestController("adminSeckillController")
@RequestMapping("admin/seckill")
@Slf4j
@Tag(name = "管理端——秒杀接口")
@RequiredArgsConstructor
public class SeckillController {
    private final SeckillService seckillService;


    /**
     * 分页查询秒杀活动
     * @param pageQueryDTO 分页查询参数
     * @return 返回值
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询秒杀活动")
    public Result<PageResult<AdminSeckillActivityPageResultVO>> pageQuery(AdminSeckillActivityPageQueryDTO pageQueryDTO){
        if (!StpUtil.hasRole(ADMIN)) {
            throw new BizException(NO_PERMISSION);
        }
        Long adminId = StpUtil.getLoginIdAsLong();
        log.info("管理员:{} 分页查询秒杀活动",adminId);
        PageResult<AdminSeckillActivityPageResultVO> pageResult = seckillService.pageQueryAdmin(adminId,pageQueryDTO);
        return Result.success(pageResult);
    }
}
