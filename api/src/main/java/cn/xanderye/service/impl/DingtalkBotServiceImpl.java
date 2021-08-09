package cn.xanderye.service.impl;

import cn.xanderye.entity.DingtalkBot;
import cn.xanderye.entity.User;
import cn.xanderye.mapper.DingtalkBotMapper;
import cn.xanderye.service.IDingtalkBotService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yezhendong
 * @since 2021-08-06
 */
@Slf4j
@Service
public class DingtalkBotServiceImpl extends ServiceImpl<DingtalkBotMapper, DingtalkBot> implements IDingtalkBotService {

    @Cacheable(value = "bot", key = "#id")
    @Override
    public DingtalkBot getById(Long id, DingtalkBot dingtalkBot) {
        if (dingtalkBot != null) {
            return dingtalkBot;
        }
        return getById(id);
    }

    @CachePut(value = "bot", key = "#dingtalkBot.id")
    @Override
    public DingtalkBot saveBot(DingtalkBot dingtalkBot, User user) {
        if (dingtalkBot.getId() == null) {
            dingtalkBot.setCreator(user.getNickname());
            dingtalkBot.setCreateTime(LocalDateTime.now());
            save(dingtalkBot);
        } else {
            dingtalkBot.setUpdater(user.getNickname());
            dingtalkBot.setUpdateTime(LocalDateTime.now());
            updateById(dingtalkBot);
        }
        return dingtalkBot;
    }

    @CacheEvict(value = "bot", key = "#id")
    @Override
    public void deleteBot(Long id) {
        removeById(id);
    }
}
