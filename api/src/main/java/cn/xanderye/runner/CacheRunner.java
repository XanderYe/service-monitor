package cn.xanderye.runner;

import cn.xanderye.entity.Contact;
import cn.xanderye.entity.DingtalkBot;
import cn.xanderye.redis.RedisUtil;
import cn.xanderye.service.IContactService;
import cn.xanderye.service.IDingtalkBotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * @author yezhendong
 * @description:
 * @date 2021/7/7 8:51
 */
@Slf4j
@Component
public class CacheRunner implements ApplicationRunner {
    @Autowired
    private IDingtalkBotService dingtalkBotService;
    @Autowired
    private IContactService contactService;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void run(ApplicationArguments args) {
        try {
            List<DingtalkBot> botList = dingtalkBotService.list();
            log.info("缓存钉钉机器人，{}条", botList.size());
            for (DingtalkBot dingtalkBot : botList) {
                dingtalkBotService.getById(dingtalkBot.getId(), dingtalkBot);
            }
            List<Contact> contactList = contactService.list();
            log.info("缓存联系人，{}条", contactList.size());
            for (Contact contact : contactList) {
                contactService.getById(contact.getId(), contact);
            }
            // 删除redis服务状态
            Set<String> statusKeys = redisUtil.keys("crashTime::*");
            if (statusKeys != null && !statusKeys.isEmpty()) {
                redisUtil.delete(statusKeys);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
