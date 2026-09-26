package cn.hisouten.mall.constant;

/**
 * 异常信息常量类
 */
public class ExceptionMessageConstant {

    public static final String NO_PERMISSION = "无权执行该操作！";
    public static final String PRODUCT_NOT_FOUND = "商品不存在！";
    public static final String SKU_NOT_FOUND = "商品款式不存在！";
    public static final String BRAND_NOT_FOUND = "品牌不存在！";
    public static final String PRODUCT_HAS_NOT_DELETED = "商品未被删除！";
    public static final String SKU_HAS_NOT_DELETED = "商品款式未被删除！";
    public static final String USER_STATUS_ERROR = "该用户状态异常，无法进行此操作！";
    public static final String USER_ALREADY_EXIST = "该用户名已存在！";
    public static final String SHOP_NAME_ALREADY_EXIST = "该店铺名已存在！";
    public static final String PRODUCT_NAME_ALREADY_EXIST = "该商品名已存在，请换个名字！";
    public static final String BRAND_NAME_ALREADY_EXIST = "该品牌名已存在，请换个名字！";
    public static final String CONTACT_PHONE_ALREADY_EXIST = "该联系电话已存在！";
    public static final String SPECS_ALREADY_EXIST = "该商品规格已存在！";
    public static final String USER_NOT_MATCH = "帐号或密码错误！";
    public static final String NO_SKU_EXIST = "请至少保留一件具体款式在当前商品下！";
    public static final String SKU_NOT_VALID = "有商品款式不存在或不属于该商品！";
    public static final String REMOVE_PRODUCT_BEFORE_DELETE = "若需要删除商品请先下架该商品！";
    public static final String PRODUCT_HAS_REMOVED = "若需要上架该款式请先上架对应商品！";
    public static final String REMOVE_SKU_BEFORE_DELETE = "若需要删除商品款式请先下架该款式！";
    public static final String LOGIC_DELETE_PRODUCT_BEFORE_PHYSICAL = "若需要彻底删除商品请先删除该商品！";
    public static final String LOGIC_DELETE_SKU_BEFORE_PHYSICAL = "若需要彻底删除商品款式请先删除该款式！";
}
