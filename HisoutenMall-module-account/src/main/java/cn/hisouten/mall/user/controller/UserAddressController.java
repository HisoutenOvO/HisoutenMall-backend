package cn.hisouten.mall.user.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.pojo.Result;
import cn.hisouten.mall.user.pojo.dto.UserAddressAddDTO;
import cn.hisouten.mall.user.pojo.dto.UserAddressUpdateDTO;
import cn.hisouten.mall.user.pojo.vo.UserAddressDetailVO;
import cn.hisouten.mall.user.pojo.vo.UserAddressListVO;
import cn.hisouten.mall.user.service.UserAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/address")
@Slf4j
@Tag(name = "用户端——地址接口")
@RequiredArgsConstructor
public class UserAddressController {
    private final UserAddressService userAddressService;

    /**
     * 用户地址列表查询
     * @return 返回值
     */
    @GetMapping("/list")
    @Operation(summary = "用户查询地址列表")
    public Result<List<UserAddressListVO>> listQuery(){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户：{}查询地址列表", userId);
        List<UserAddressListVO> userAddressListVOS = userAddressService.listQuery(userId);
        return Result.success(userAddressListVOS);
    }

    /**
     * 用户查询地址详情
     * @param addressId 地址id
     * @return 返回值
     */
    @GetMapping("/{addressId}")
    @Operation(summary = "用户查询地址详情")
    public Result<UserAddressDetailVO> detailQuery(@PathVariable Long addressId){
        log.info("用户:{}查询地址详情:{}",StpUtil.getLoginIdAsLong(),addressId);
        UserAddressDetailVO userAddressDetailVO = userAddressService.detailQuery(addressId);
        return Result.success(userAddressDetailVO);
    }

    /**
     * 用户新增地址
     * @param userAddressAddDTO 地址参数
     * @return 返回值
     */
    @PostMapping
    @Operation(summary = "用户新增地址")
    public Result addAddress(@RequestBody UserAddressAddDTO userAddressAddDTO){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户：{}新增地址参数",userId);
        userAddressService.addAddress(userId,userAddressAddDTO);
        return Result.success();
    }


    /**
     * 用户修改地址
     * @param addressId 地址id
     * @param userAddressUpdateDTO 修改参数
     * @return 返回值
     */
    @PutMapping("/{addressId}")
    @Operation(summary = "用户修改地址")
    public Result updateAddress(@PathVariable Long addressId,@RequestBody UserAddressUpdateDTO userAddressUpdateDTO){
        log.info("用户:{}修改地址:{}",StpUtil.getLoginIdAsLong(),addressId);
        userAddressService.updateAddress(addressId,userAddressUpdateDTO);
        return Result.success();
    }


    /**
     * 用户修改默认地址
     * @param addressId 地址id
     * @return 返回值
     */
    @PutMapping("/{addressId}/default")
    @Operation(summary = "用户修改默认地址")
    public Result changeDefault(@PathVariable Long addressId){
        log.info("用户：{}修改默认地址：{}",StpUtil.getLoginIdAsLong(),addressId);
        userAddressService.changeDefault(addressId);
        return Result.success();
    }

    /**
     * 用户删除地址
     * @param addressId 地址id
     * @return 返回值
     */
    @DeleteMapping("/{addressId}")
    @Operation(summary = "用户删除地址")
    public Result deleteAddress(@PathVariable Long addressId){
        log.info("用户：{}删除地址：{}",StpUtil.getLoginIdAsLong(),addressId);
        userAddressService.deleteAddress(addressId);
        return Result.success();
    }
}
