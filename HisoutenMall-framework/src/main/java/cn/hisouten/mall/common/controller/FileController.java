package cn.hisouten.mall.common.controller;

import cn.hisouten.mall.common.pojo.Result;
import cn.hisouten.mall.common.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
@Tag(name = "通用——文件上传")
public class FileController {

    private final FileService fileService;

    /**
     * 文件上传
     * @param file 文件
     * @return 返回值
     */
    @PostMapping("/upload")
    @Operation(summary = "图片上传")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        return Result.success(fileService.upload(file));
    }
}