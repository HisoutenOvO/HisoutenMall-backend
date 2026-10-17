package cn.hisouten.mall.controller.user;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.common.pojo.BasePageQueryDTO;
import cn.hisouten.mall.common.pojo.PageResult;
import cn.hisouten.mall.common.pojo.Result;
import cn.hisouten.mall.pojo.vo.history.UserHistoryPageResultVO;
import cn.hisouten.mall.service.HistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/history")
@Slf4j
@Tag(name = "用户端——浏览历史接口")
@RequiredArgsConstructor
public class HistoryController {
    private final HistoryService historyService;

    /**
     * 增加历史记录
     * @param productId 商品id
     * @return 返回值
     */
    @PostMapping("/{productId}")
    @Operation(summary = "增加浏览记录")
    public Result addHistory(@PathVariable Long productId){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户:{} 浏览商品:{} ",userId,productId);
        historyService.addHistory(userId,productId);
        return Result.success();
    }

    /**
     * 分页查询浏览历史记录
     * @param pageQueryDTO 分页参数
     * @return 返回值
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询历史")
    public Result<PageResult<UserHistoryPageResultVO>> pageQuery(@Valid BasePageQueryDTO pageQueryDTO){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户:{} 分页查询浏览记录",userId);
        PageResult<UserHistoryPageResultVO> pageResult = historyService.pageQuery(userId,pageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 删除历史记录
     * @param productId 商品id
     * @return 返回值
     */
    @DeleteMapping("/{productId}")
    @Operation(summary = "删除历史记录")
    public Result deleteHistory(@PathVariable Long productId){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户:{} 删除历史记录商品:{} ",userId,productId);
        historyService.deleteHistory(userId,productId);
        return Result.success();
    }

    /**
     * 删除所有历史记录
     * @return 返回值
     */
    @DeleteMapping
    @Operation(summary = "删除所有历史记录")
    public Result deleteAll(){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户:{} 删除所有历史记录",userId);
        historyService.deleteAll(userId);
        return Result.success();
    }
}
