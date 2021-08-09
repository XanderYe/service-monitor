package cn.xanderye.service;

import cn.xanderye.util.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

/**
 * @author yezhendong
 * @description:
 * @date 2021/8/9 13:06
 */
@Slf4j
@Service
public class MessageService {

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSender javaMailSender;

    private static final String DB_BOT_URL = "https://oapi.dingtalk.com/robot/send?access_token=${token}";


    public void sendEmail(String title, String content, List<String> toEmailList, List<String> carbonCopyList) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(from);
        mailMessage.setTo(toEmailList.toArray(new String[0]));
        if (carbonCopyList != null && !carbonCopyList.isEmpty()) {
            mailMessage.setCc(carbonCopyList.toArray(new String[0]));
        }
        mailMessage.setSubject(title);
        mailMessage.setText(content);
        log.info("发送电子邮件，标题: [{}], 内容: [{}], 收件人: [{}], 抄送人: [{}]", title, content, toEmailList, carbonCopyList);
        javaMailSender.send(mailMessage);
    }

    /**
     * 钉钉机器人推送
     *
     * @see <a href="https://developers.dingtalk.com/document/app/custom-robot-access">钉钉机器人开发文档</a>
     * @param token
     * @param secret
     * @param content
     * @param isAtAll      是否@全体
     * @param atMobileList @的手机号列表
     * @return java.lang.String
     * @author XanderYe
     * @date 2021/1/23
     */
    public static String dingTalkBotPush(String token, String secret, String content, boolean isAtAll, List<String> atMobileList) throws IOException, InvalidKeyException, NoSuchAlgorithmException {
        String webhook = DB_BOT_URL.replace("${token}", token);
        long timestamp = System.currentTimeMillis();
        String sign = getSign(timestamp, secret);
        webhook = webhook + "&timestamp=" + timestamp + "&sign=" + sign;
        JSONObject atJson = new JSONObject();
        // 是否通知所有人
        atJson.put("isAtAll", isAtAll);
        // 通知人列表
        if (!isAtAll) {
            atJson.put("atMobiles", atMobileList);
        }
        // 消息内容
        JSONObject contentJson = new JSONObject();
        contentJson.put("content", content);
        // 请求体
        JSONObject params = new JSONObject();
        params.put("msgtype", "text");
        params.put("text", contentJson);
        params.put("at", atJson);
        String res = HttpUtil.doPostJSON(webhook, params.toJSONString()).getResponse();
        log.info("发送钉钉消息，token: [{}], secret: [{}], 内容: [{}], @手机号: [{}], 结果: [{}]", token, secret, content, atMobileList, res);
        return res;
    }


    /**
     * 钉钉机器人签名
     *
     * @param timestamp
     * @param secret
     * @return java.lang.String
     * @author XanderYe
     * @date 2021/1/23
     */
    private static String getSign(long timestamp, String secret) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        String stringToSign = timestamp + "\n" + secret;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
        return URLEncoder.encode(new String(Base64.getEncoder().encode(signData)), "UTF-8");
    }
}
