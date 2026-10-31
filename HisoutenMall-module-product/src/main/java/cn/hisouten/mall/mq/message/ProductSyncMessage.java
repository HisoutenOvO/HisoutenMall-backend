package cn.hisouten.mall.mq.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * es修改文档内容消息体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSyncMessage implements Serializable {
    private Long productId;

    /**
     * 操作类型：
     * SAVE - 新增或全量更新（如修改基本信息、上下架）
     * DELETE - 删除（逻辑删除或物理删除）
     * SALES - 仅更新销量（下单支付后触发）
     */
    private String type;
}