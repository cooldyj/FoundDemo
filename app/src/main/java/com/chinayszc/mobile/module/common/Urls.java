package com.chinayszc.mobile.module.common;

/**
 * 接口url类
 * Created by jerry on 2017/4/15.
 */
public class Urls {

    /**
     * 返回成功码
     */
    public static final int SUCCESS_CODE = 0;
    /**
     * 用户未登录码
     */
    public static final int NOT_LOGGED_IN_CODE = -300;
    /**
     * 返回失败码
     */
    public static final int ERROR_CODE = 1011;
    /**
     * 设备类型
     */
    public static final String DEVICE_TYPE = "android";

    private static final String BaseUrl1 = "http://api.azhongzhi.com/api";

    /**
     * 首页
     */
    //图片
    public static final String HOME_IMG_LIST = BaseUrl1 + "/home/imageList.htm";
    //推荐产品
    public static final String HOME_PRODUCT = BaseUrl1 + "/home/product.htm";
    //其他产品列表
    public static final String HOME_PRODUCT_LIST = BaseUrl1 + "/home/productList.htm";

    /**
     * 积分商城
     */
    //我的积分
    public static final String GET_CREDIT = BaseUrl1 + "/shop/getCredit.htm";
    //轮询图片
    public static final String SHOP_IMG_LIST = BaseUrl1 + "/shop/imageList.htm";
    //首页商品列表
    public static final String SHOP_PRO_LIST = BaseUrl1 + "/shop/productList.htm";
    //我的积分列表
    public static final String SHOP_CREDIT_LIST = BaseUrl1 + "/shop/creditList.htm";
    //我的兑换记录列表
    public static final String CONVERT_LIST = BaseUrl1 + "/shop/orderList.htm";
    //我可以兑换商品列表
    public static final String MY_PRODUCT_LIST = BaseUrl1 + "/shop/myProductList.htm";
    //查看商品
    public static final String SHOP_PRODUCT = BaseUrl1 + "/shop/product.htm";
    //兑换商品
    public static final String ADD_ORDER = BaseUrl1 + "/shop/addOrder.htm";
    //获取地区
    public static final String GET_AREA = BaseUrl1 + "/shop/area.htm";
    //设置用户收货地址
    public static final String SET_ADDRESS = BaseUrl1 + "/shop/setAddress.htm";
    //获取用户收货地址
    public static final String GET_ADDRESS = BaseUrl1 + "/shop/getAddress.htm";
    //删除用户收货地址
    public static final String DELETE_ADD = BaseUrl1 + "/shop/delAddress.htm";
    //设置默认收货地址
    public static final String SET_DEFAULT_ADD = BaseUrl1 + "/shop/setDefaultAddress.htm";
    //设置默认收货地址
    public static final String BUY_ADDRESS = BaseUrl1 + "/shop/buyAddress.htm";

    /**
     * 优惠券
     */
    //未使用的优惠券
    public static final String UNUSED = BaseUrl1 + "/coupon/usable.htm";
    //已使用的优惠券
    public static final String USED = BaseUrl1 + "/coupon/used.htm";
    //已过期的优惠券
    public static final String EXPIRED = BaseUrl1 + "/coupon/expired.htm";

    /**
     * 资产模块页
     */
    //首页各数据
    public static final String GET_ASSET = BaseUrl1 + "/asset/getAsset.htm";
    //充值
    public static final String RECHARGE = BaseUrl1 + "/asset/cashIn.htm";
    //提现
    public static final String WITHDRAW = BaseUrl1 + "/asset/cashOut.htm";
    //交易记录列表
    public static final String ORDER_LIST = BaseUrl1 + "/order/orderList.htm";
    //收益记录列表
    public static final String EARNING_LIST = BaseUrl1 + "/asset/profitList.htm";
    //打开提现
    public static final String SHOW_CASH_IN = BaseUrl1 + "/asset/showCashIn.htm";
    //提现短信验证码
    public static final String SMS_CASH_OUT = BaseUrl1 + "/sms/cashOut.htm";
    //打开充值
    public static final String CASH_OUT = BaseUrl1 + "/asset/showCashOut.htm";
    //确认充值
    public static final String CASH_IN_CONFIRM = BaseUrl1 + "/goPay/cashInConfirm.htm";

    /**
     * 产品/订单模块
     */
    //产品列表
    public static final String PRODUCT_LIST = BaseUrl1 + "/product/productList.htm";
    //产品查看
    public static final String PRODUCT = BaseUrl1 + "/product/product.htm";
    //选择优惠券
    public static final String GET_COUPON = BaseUrl1 + "/order/getCoupon.htm";
    //填写购买金额
    public static final String SET_MONEY = BaseUrl1 + "/order/setMoney.htm";
    //产品支付打开
    public static final String GET_MONEY = BaseUrl1 + "/order/getMoney.htm";
    //提交订单
    public static final String SUBMIT_ORDER = BaseUrl1 + "/order/addOrder.htm";
    //确认订单
    public static final String PAY_CONFIRM = BaseUrl1 + "/goPay/payConfirm.htm";

    /**
     * 我的模块
     */
    //修改密码
    public static final String EDIT_PSW = BaseUrl1 + "/my/editPassWord.htm";
    //忘记密码
    public static final String FORGET_PSW = BaseUrl1 + "/my/forgetPassWord.htm";
    //我的银行卡
    public static final String MY_BANK_CARD = BaseUrl1 + "/my/myBank.htm";
    //吐个槽
    public static final String FEED_BACK = BaseUrl1 + "/my/feedback.htm";
    //首页
    public static final String ME_MAIN = BaseUrl1 + "/my/home.htm";
    //更换银行卡
    public static final String CHANGE_BANK_CARD = BaseUrl1 + "/my/editPassWord.htm";
    //银行列表
    public static final String BANK_CARD_LIST = BaseUrl1 + "/goPay/bankList.htm";
    //绑定银行卡第一步-新加
    public static final String BANK_CARD_SETP1 = BaseUrl1 + "/goPay/addCard1.htm";
    //绑定银行卡第二步-新加
    public static final String BANK_CARD_SETP2 = BaseUrl1 + "/goPay/addCard2.htm";
    //消息列表
    public static final String NOTICE_LIST = BaseUrl1 + "/notice/noticeList.htm";

    /**
     * 其他
     */
    //用户登录
    public static final String USER_LOGIN = BaseUrl1 + "/user/login.htm";
    //用户验证码登录
    public static final String USER_LOGIN_MOBILE = BaseUrl1 + "/user/loginMoblie.htm";
    //用户注册
    public static final String USER_REGISTER = BaseUrl1 + "/user/register.htm";
    //退出
    public static final String LOG_OUT = BaseUrl1 + "/user/logout.htm";

    /**
     * 验证码
     */
    //用户注册
    public static final String REGISTER_SMS = BaseUrl1 + "/sms/register.htm";
    //用户手机号登录
    public static final String LOGIN_SMS = BaseUrl1 + "/sms/login.htm";
    //用户找回密码
    public static final String GET_PSW_SMS = BaseUrl1 + "/sms/forget.htm";

    //积分商城产品购买成功
    public static final String POINT_MALL_SUCCESS = "http://m.azhongzhi.com/client/shopOrderSuccess.htm";

    //理财产品购买成功
    public static final String FINANCIAL_PRODUCT_SUCCESS = "http://m.azhongzhi.com/client/orderSuccess.htm";
}
