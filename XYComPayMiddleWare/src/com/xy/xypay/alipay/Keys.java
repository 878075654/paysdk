package com.xy.xypay.alipay;

/**
 * Created by mawj on 2014/11/12.
 * 提示：如何获取安全校验码和合作身份者id
 *  1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *  2.点击“商家服务”(https://b.alipay.com/order/myorder.htm)
 *  3.点击“查询合作者身份(pid)”、“查询安全校验码(key)”
 *
 *请参考 Android平台安全支付服务(msp)应用开发接口(4.2 RSA算法签名)部分，并使用压缩包中的openssl RSA密钥生成工具，生成一套RSA公私钥。
 * 这里签名时，只需要使用生成的RSA私钥。
 *Note: 为安全起见，使用RSA私钥进行签名的操作过程，应该尽量放到商家服务器端去进行。
 */
public class Keys {
    //合作身份者id，以2088开头的16位纯数字
    public static final String DEFAULT_PARTNER = "2088801850024473";
    //收款支付宝账号;
    public static final String DEFAULT_SELLER = "2088801850024473";
    //商户私钥，自助生成
    public static final String PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKkQNip1QJ/P3Wo600Rft9SQ7uBkhNf5l+Kt8i3YruXLaAdjcgPHDhzhOruBjPUdcC0Q2aTcURqBY0jNL4cpc/7x9zJK50WPx0usBEJCF7R0N8rNKQoHTGS666Mxtn3U04N82esEpDjd/8XHoTKx1WxlxfpjtmGAEAkNbhgxYJkXAgMBAAECgYAxebAQTs4BtsgaLWrkSIq4p01w15nUxpM/YB5OcmoEj7k66ExWdKtZokQY2XnRxbKtTZAT4fHOKu1OQdXV3Ti3elzGp0l2VSdppQRhSopL6+UGJMNy1+DkArsnRb64UgRE4//8MUeBpooHjztt04XdrawEg6qGzoc+e2XvWWC8QQJBANntBflXDxNwrzT26gaoPraMbLKE9TXXrjiseAK7f//p/POnKSlfOnHlDUfljjpz+Ktav0XBLwNupdqLbmgKjSECQQDGmcGxyXsE5B2FPZtOfQdTAb3WmT+AIWjy8O+4RwHThCLIglkxHTRfv5s8nqZjmmeuYhJeuK9KDe7Iaq6Z1Gc3AkAURvuxUapzcp663Oa1q935+mL60WhHlEP/vfyEtJGabFk/CqTH1raHdnqf3/o18iiHOTBm+yy2swr3pNWnFKRBAkBPfUUomsMRXxhttw3NaX+f+qd4GFhxOW4fJs5cDJeviEi/xmRaoxzOYguJRNrGLQvooTpHDVajplX3g7OkwH4bAkEAk4uKy/WDbfnQIzhhvfJY8fbREOTUs5tgyc6Tz0iYE0lmlzE89Q8i5811rhnoD5mgqY2ItJqUhZiJU/SjFiteSw==";
    public static final String PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpEDYqdUCfz91qOtNEX7fUkO7gZITX+ZfirfIt2K7ly2gHY3IDxw4c4Tq7gYz1HXAtENmk3FEagWNIzS+HKXP+8fcySudFj8dLrARCQhe0dDfKzSkKB0xkuuujMbZ91NODfNnrBKQ43f/Fx6EysdVsZcX6Y7ZhgBAJDW4YMWCZFwIDAQAB";
}
