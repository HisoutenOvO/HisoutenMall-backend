package cn.hisouten.mall.constant;

/**
 * 异常信息常量类
 */
public class ExceptionMessageConstant {

    public static final String NO_PERMISSION = "无权执行该操作！";
    public static final String SYSTEM_ERROR = "系统错误！";
    public static final String PRODUCT_NOT_FOUND = "商品不存在！";
    public static final String CATEGORY_NOT_FOUND = "分类不存在！";
    public static final String USER_NOT_FOUND = "用户不存在！";
    public static final String ADDRESS_NOT_FOUND = "地址不存在！";
    public static final String CART_NOT_FOUND = "购物车项不存在！";
    public static final String ORDER_NOT_FOUND = "订单不存在！";
    public static final String MERCHANT_NOT_FOUND = "商家不存在！";
    public static final String PARENT_CATEGORY_NOT_FOUND = "父级分类不存在！";
    public static final String LEVEL_OVERFLOW = "分类层级溢出，最多三级，请修改子分类！";
    public static final String SKU_NOT_FOUND = "商品款式不存在！";
    public static final String BRAND_NOT_FOUND = "品牌不存在！";
    public static final String PRODUCT_HAS_NOT_DELETED = "商品未被删除！";
    public static final String SKU_HAS_NOT_DELETED = "商品款式未被删除！";
    public static final String USER_STATUS_ERROR = "该用户状态异常，无法进行此操作！";
    public static final String ORDER_STATUS_ERROR = "订单状态异常，无法进行此操作！";
    public static final String USER_ALREADY_EXIST = "该用户名已存在！";
    public static final String SHOP_NAME_ALREADY_EXIST = "该店铺名已存在！";
    public static final String CATEGORY_NAME_ALREADY_EXIST = "该分类名已存在！";
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
    public static final String BRAND_RELATED_PRODUCT = "该品牌关联着商品，暂不可删除";
    public static final String CATEGORY_RELATED_CHILDREN = "该分类下有子分类，需要删除请先删除子分类";
    public static final String CATEGORY_RELATED_PRODUCT = "该分类下关联着商品，不可删除";
    public static final String BRAND_INVALID = "该品牌已无效，请更换品牌再进行操作！(被下架，被删除或不存在)";
    public static final String CATEGORY_INVALID = "该分类已无效，请更换分类再进行操作！(被下架，被删除或不存在)";
    public static final String PASSWORD_ERROR = "原密码错误！";
    public static final String SAME_PASSWORD = "新密码不能和原密码相同！";
    public static final String CHANGE_DEFAULT_ADDRESS_FIRST = "删除默认地址前，请先将其他地址设为默认！";
    public static final String CHECK_PRODUCT_FIRST = "请先勾选商品再进行结算！";
    public static final String ROLE_NOT_MERCHANT = "该用户角色不是商家，请确认";
    public static final String ROLE_NOT_USER = "该用户角色不是顾客，请确认";
    public static final String SKU_HAS_DISABLED = "该款式已被下架！";
    public static final String PRODUCT_HAS_DISABLED = "该商品已被下架！";
    public static final String OUT_OF_STOCK = "库存不足！";
    public static final String OUT_OF_CART = "购物车已满！";
}
