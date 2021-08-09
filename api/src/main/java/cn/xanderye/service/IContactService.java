package cn.xanderye.service;

import cn.xanderye.entity.Contact;
import cn.xanderye.entity.User;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yezhendong
 * @since 2021-08-06
 */
public interface IContactService extends IService<Contact> {

    Contact getById(Long id, Contact contact);

    Contact saveContact(Contact contact, User user);

    void deleteConcat(Long id);
}
