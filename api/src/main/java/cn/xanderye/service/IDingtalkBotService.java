package cn.xanderye.service;

import cn.xanderye.entity.DingtalkBot;
import cn.xanderye.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yezhendong
 * @since 2021-08-06
 */
public interface IDingtalkBotService extends IService<DingtalkBot> {

    DingtalkBot getById(Long id, DingtalkBot dingtalkBot);

    DingtalkBot saveBot(DingtalkBot dingtalkBot, User user);

    void deleteBot(Long id);
}
