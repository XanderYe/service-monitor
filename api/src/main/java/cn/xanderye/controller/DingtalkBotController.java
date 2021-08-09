package cn.xanderye.controller;


import cn.xanderye.aop.Log;
import cn.xanderye.base.ResultBean;
import cn.xanderye.base.UserContextHolder;
import cn.xanderye.entity.DingtalkBot;
import cn.xanderye.entity.User;
import cn.xanderye.service.IDingtalkBotService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/dingtalkBot")
public class DingtalkBotController {
    @Autowired
    private IDingtalkBotService dingtalkBotService;

    @Log(moduleName = "机器人管理", methodName = "获取机器人列表")
    @GetMapping("getList")
    public ResultBean getList() {
        List<DingtalkBot> list = dingtalkBotService.list();
        return new ResultBean(list);
    }

    @Log(moduleName = "机器人管理", methodName = "根据id获取机器人")
    @GetMapping("getById")
    public ResultBean getById(Long id) {
        DingtalkBot dingtalkBot = dingtalkBotService.getById(id, null);
        return new ResultBean(dingtalkBot);
    }

    @Log(moduleName = "机器人管理", methodName = "保存机器人信息")
    @PostMapping("save")
    public ResultBean save(@RequestBody DingtalkBot dingtalkBot) {
        User user = UserContextHolder.get();
        dingtalkBotService.saveBot(dingtalkBot, user);
        return new ResultBean();
    }

    @Log(moduleName = "机器人管理", methodName = "删除机器人")
    @PostMapping("delete")
    public ResultBean delete(Long id) {
        dingtalkBotService.deleteBot(id);
        return new ResultBean();
    }
}

