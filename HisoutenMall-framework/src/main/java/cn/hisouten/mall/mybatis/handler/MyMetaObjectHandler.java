package cn.hisouten.mall.mybatis.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 借助Mybatis-Plus注册自己的公共字段填充类
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    //定义当前时间
    LocalDateTime now = LocalDateTime.now();

    @Override
    public void insertFill(MetaObject metaObject){
        strictInsertFill(metaObject,"createTime", LocalDateTime.class,now);
        strictInsertFill(metaObject,"updateTime", LocalDateTime.class,now);
    }

    @Override
    public void updateFill(MetaObject metaObject){
        strictUpdateFill(metaObject,"updateTime", LocalDateTime.class,now);
    }
}
