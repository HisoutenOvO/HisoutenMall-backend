package cn.hisouten.mall.controller.user;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.pojo.Result;
import cn.hisouten.mall.service.HistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
