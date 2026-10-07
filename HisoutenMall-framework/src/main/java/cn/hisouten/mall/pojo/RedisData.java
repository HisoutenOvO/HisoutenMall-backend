package cn.hisouten.mall.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 统一封装带有逻辑过期时间的缓存数据
 * @param <T> 对象类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedisData<T>{
    private LocalDateTime expire; //逻辑过期时间，额外字段
    private T data; //用于储存各种实体类
}

