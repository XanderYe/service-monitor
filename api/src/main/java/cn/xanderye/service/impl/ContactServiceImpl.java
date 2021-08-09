package cn.xanderye.service.impl;

import cn.xanderye.entity.Contact;
import cn.xanderye.entity.DingtalkBot;
import cn.xanderye.entity.User;
import cn.xanderye.mapper.ContactMapper;
import cn.xanderye.service.IContactService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yezhendong
 * @since 2021-08-06
 */
@Service
public class ContactServiceImpl extends ServiceImpl<ContactMapper, Contact> implements IContactService {

    @Cacheable(value = "contact", key = "#id")
    @Override
    public Contact getById(Long id, Contact contact) {
        if (contact != null) {
            return contact;
        }
        return getById(id);
    }

    @CachePut(value = "contact", key = "#contact.id")
    @Override
    public Contact saveContact(Contact contact, User user) {
        if (contact.getId() == null) {
            contact.setCreator(user.getNickname());
            contact.setCreateTime(LocalDateTime.now());
            save(contact);
        } else {
            contact.setUpdater(user.getNickname());
            contact.setUpdateTime(LocalDateTime.now());
            updateById(contact);
        }
        return contact;
    }

    @CacheEvict(value = "contact", key = "#id")
    @Override
    public void deleteConcat(Long id) {
        removeById(id);
    }
}
