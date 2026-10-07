package cn.hisouten.mall.constant;

/**
 * 用于记录redis相关的常量类
 */
public class RedisConstant {

    //商品详情键前缀
    public static final String CACHE_PRODUCT_DETAIL_PREFIX = "cache:product:detail:";

    //商品详情锁前缀
    public static final String LOCK_PRODUCT_DETAIL_PREFIX = "lock:product:detail:";

    //商品详情缓存过期时间
    public static final long CACHE_PRODUCT_DETAIL_TTL = 30L;

    //用户详情键前缀
    public static final String CACHE_USER_DETAIL_PREFIX = "cache:user:detail:";

    //用户详情缓存过期时间
    public static final Long CACHE_USER_DETAIL_TTL = 30L;

    //地址详情键前缀
    public static final String CACHE_ADDRESS_DETAIL_PREFIX = "cache:address:detail:";

    //地址详情缓存过期时间
    public static final Long CACHE_ADDRESS_DETAIL_TTL = 30L;

    //分类键前缀
    public static final String CACHE_CATEGORY_PREFIX = "cache:category:";

    //分类名键前缀
    public static final String CACHE_CATEGORY_NAME_PREFIX = "cache:category:name:";

    //品牌键前缀
    public static final String CACHE_BRAND_PREFIX = "cache:brand:";

    //品牌名键前缀
    public static final String CACHE_BRAND_NAME_PREFIX = "cache:brand:name:";

    //商家名键前缀
    public static final String CACHE_MERCHANT_NAME_PREFIX = "cache:merchant:name:";

    //用户名键前缀
    public static final String CACHE_USER_NICKNAME_PREFIX = "cache:user:nickname:";


    //设置空缓存的过期时间——单位是分钟
    public static final long CACHE_NULL_TTL = 2L;

}
