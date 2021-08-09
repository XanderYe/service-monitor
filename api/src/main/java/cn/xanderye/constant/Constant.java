package cn.xanderye.constant;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author XanderYe
 * @description:
 * @date 2021/7/25 17:57:42
 */
public class Constant {
    public static final String SALT = "xander";

    public static final String EHCACHE_CAPTCHA_NAME = "captcha";

    public static final String EHCACHE_USER_NAME = "user";

    public static final int IMAGE_WIDTH = 100;

    public static final int IMAGE_HEIGHT = 30;

    /**
     * QQ正则
     */
    public static final Pattern QQ_PATTERN = Pattern.compile("\\d{0,10}");

    /**
     * 群正则
     */
    public static final Pattern GROUP_PATTERN = Pattern.compile("\\d{0,9}");

    /**
     * 最大QQ号
     */
    public static final int MAX_QQ_NUM = 1500000000;

    /**
     * 群组最大群号
     */
    public static final int MAX_GROUP_NUM = 100219998;

    /**
     * 群信息最大群号
     */
    public static final int MAX_QUNINFO_NUM = 109999899;

    /**
     * 图片格式对应的base64 map
     */
    public static final Map<String, String> IMAGE_BASE64_MAP = new HashMap<String, String>(){{
        put("jpg", "image/jpeg");
        put("jpeg", "image/jpeg");
        put("png", "image/png");
    }};
}
