package cn.xanderye.controller;


import cn.xanderye.aop.Log;
import cn.xanderye.base.ResultBean;
import cn.xanderye.base.UserContextHolder;
import cn.xanderye.entity.Contact;
import cn.xanderye.entity.DingtalkBot;
import cn.xanderye.entity.User;
import cn.xanderye.service.IContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yezhendong
 * @since 2021-08-06
 */
@RestController
@RequestMapping("/contact")
public class ContactController {
    @Autowired
    private IContactService contactService;

    @GetMapping("getList")
    public ResultBean getList() {
        List<Contact> list = contactService.list();
        return new ResultBean(list);
    }

    @GetMapping("getById")
    public ResultBean getById(Long id) {
        Contact contact = contactService.getById(id, null);
        return new ResultBean(contact);
    }

    @Log(moduleName = "服务管理", methodName = "保存联系人信息", logResult = false)
    @PostMapping("save")
    public ResultBean save(@RequestBody Contact contact) {
        User user = UserContextHolder.get();
        contactService.saveContact(contact, user);
        return new ResultBean();
    }

    @Log(moduleName = "服务管理", methodName = "删除联系人", logResult = false)
    @PostMapping("delete")
    public ResultBean delete(Long id) {
        contactService.deleteConcat(id);
        return new ResultBean();
    }
}

