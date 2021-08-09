package cn.xanderye.controller;

import cn.xanderye.base.ResultBean;
import cn.xanderye.constant.Constant;
import cn.xanderye.redis.RedisUtil;
import cn.xanderye.util.VerifyCodeUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author XanderYe
 * @date 2019/8/27
 */
@RestController
public class CaptchaController {
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 验证码
     *
     * @return com.xander.mdblog.base.ResultBean
     * @author XanderYe
     * @date 2019-07-11
     */
    @GetMapping("captcha")
    public ResultBean captcha() {
        JSONObject captcha = new JSONObject();
        String uuid = UUID.randomUUID().toString();
        String code = VerifyCodeUtil.generateVerifyCode(4);
        String image = VerifyCodeUtil.imageBase64(Constant.IMAGE_WIDTH, Constant.IMAGE_HEIGHT, code);
        redisUtil.set("captcha_" + uuid, code, 5, TimeUnit.MINUTES);
        captcha.put("uuid", uuid);
        captcha.put("image", image);
        return new ResultBean<>(captcha);
    }
}
