package cn.xanderye.service;

import cn.xanderye.entity.Contact;
import cn.xanderye.entity.DingtalkBot;
import cn.xanderye.entity.ServiceConfig;
import cn.xanderye.redis.RedisUtil;
import cn.xanderye.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author yezhendong
 * @description:
 * @date 2021/8/9 15:44
 */
@Slf4j
@Service
public class ScheduleService {
    @Autowired
    private IContactService contactService;
    @Autowired
    private IDingtalkBotService dingtalkBotService;
    @Autowired
    private IServiceConfigService serviceConfigService;
    @Autowired
    private AccessLogService accessLogService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private RedisUtil redisUtil;

    private static final String CRASH_TIME = "crashTime::${id}";

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void checkService(ServiceConfig serviceConfig) {
        Boolean isPublish = (Boolean) redisUtil.get("isPublish");
        if (isPublish != null && isPublish) {
            log.info("开启发版模式，不监控服务状态");
            return;
        }
        ServiceConfig fullServiceConfig = serviceConfigService.getFullServiceConfig(serviceConfig.getId(), null);
        String key = CRASH_TIME.replace("${id}", String.valueOf(serviceConfig.getId()));
        Long crashTime = (Long) redisUtil.get(key);
        try {
            HttpUtil.doGet(fullServiceConfig.getUrl(), null);
            handleSuccess(fullServiceConfig, key, crashTime);
        } catch (IOException e) {
            // 重试一次
            try {
                HttpUtil.doGet(fullServiceConfig.getUrl(), null);
                handleSuccess(fullServiceConfig, key, crashTime);
            } catch (IOException e2) {
                if (crashTime == null) {
                    // 正常 -> 异常
                    handleError(fullServiceConfig, key, e2);
                }
            }
        }
    }

    private void handleSuccess(ServiceConfig serviceConfig, String key, Long crashTime) {
        if (crashTime != null) {
            long lastTime = (System.currentTimeMillis() - crashTime) / 1000;
            // 异常 -> 正常
            accessLogService.logSystemError(serviceConfig.getName() + "已从异常状态恢复");
            String title = serviceConfig.getName() + "已恢复";
            String content = MessageFormat.format("地址: {0}, 持续时间: {1}秒", serviceConfig.getUrl(), lastTime);
            sendMessage(serviceConfig, title, content);
            redisUtil.delete(key);
            log.info("{} 已从异常状态恢复", serviceConfig.getName());
        } else {
            log.info("{} 正常", serviceConfig.getName());
        }
    }

    private void handleError(ServiceConfig serviceConfig, String key, IOException e) {
        // 正常 -> 异常
        accessLogService.logSystemError(serviceConfig.getName() + "访问失败，异常：" + e.getMessage());
        String title = serviceConfig.getName() + "访问失败";
        String content = MessageFormat.format("地址: {0}, 发生时间: {1}, 详情原因请看日志", serviceConfig.getUrl(), LocalDateTime.now().format(DATE_TIME_FORMATTER));
        sendMessage(serviceConfig, title, content);
        redisUtil.set(key, System.currentTimeMillis());
        log.info("{} 访问失败", serviceConfig.getName());
    }

    public void sendMessage(ServiceConfig serviceConfig, String title, String content) {
        // 发送邮件
        List<Long> contactList = serviceConfig.getMailContactList();
        if (contactList != null && !contactList.isEmpty()) {
            List<String> to = new ArrayList<>();
            for (Long contactId : contactList) {
                Contact contact = contactService.getById(contactId, null);
                to.add(contact.getEmail());
            }
            try {
                messageService.sendEmail(title, content, to, null);
            } catch (Exception e) {
                log.error("邮件发送失败，原因：{}", e.getMessage());
            }
        }

        // 发送钉钉消息
        Map<String, List<Long>> dingtalkContactMap = serviceConfig.getServiceDingtalkContactMap();
        if (dingtalkContactMap != null) {
            String ctx = title + "\n" + content;
            for (String key : dingtalkContactMap.keySet()) {
                Long botId = Long.valueOf(key);
                DingtalkBot dingtalkBot = dingtalkBotService.getById(botId, null);
                List<Long> newContactList = dingtalkContactMap.get(key);
                List<String> atList = new ArrayList<>();
                for (Long contactId : newContactList) {
                    Contact contact = contactService.getById(contactId, null);
                    atList.add(contact.getPhone());
                }
                try {
                    MessageService.dingTalkBotPush(dingtalkBot.getToken(), dingtalkBot.getSecret(), ctx, false, atList);
                } catch (IOException | InvalidKeyException | NoSuchAlgorithmException e) {
                    log.error("钉钉消息发送失败，原因：{}", e.getMessage());
                }
            }
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
