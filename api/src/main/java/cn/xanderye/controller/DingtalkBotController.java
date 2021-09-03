package cn.xanderye.controller;


import cn.xanderye.aop.Log;
import cn.xanderye.base.ResultBean;
import cn.xanderye.base.UserContextHolder;
import cn.xanderye.entity.DingtalkBot;
import cn.xanderye.entity.User;
import cn.xanderye.service.IDingtalkBotService;
import cn.xanderye.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Collections;
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

    @GetMapping("getList")
    public ResultBean getList() {
        List<DingtalkBot> list = dingtalkBotService.list();
        return new ResultBean(list);
    }

    @GetMapping("getById")
    public ResultBean getById(Long id) {
        DingtalkBot dingtalkBot = dingtalkBotService.getById(id, null);
        return new ResultBean(dingtalkBot);
    }

    @Log(moduleName = "机器人管理", methodName = "保存机器人信息", logResult = false)
    @PostMapping("save")
    public ResultBean save(@RequestBody DingtalkBot dingtalkBot) {
        User user = UserContextHolder.get();
        dingtalkBotService.saveBot(dingtalkBot, user);
        return new ResultBean();
    }

    @Log(moduleName = "机器人管理", methodName = "删除机器人", logResult = false)
    @PostMapping("delete")
    public ResultBean delete(Long id) {
        dingtalkBotService.deleteBot(id);
        return new ResultBean();
    }

    @Log(moduleName = "机器人管理", methodName = "测试机器人", logResult = false)
    @PostMapping("test")
    public ResultBean test(@RequestBody DingtalkBot dingtalkBot) {
        try {
            MessageService.dingTalkBotPush(dingtalkBot.getToken(), dingtalkBot.getSecret(), "测试服务监控消息推送", false, Collections.singletonList("13777004558"));
            return new ResultBean();
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return new ResultBean(1, e.getMessage());
        }
    }
}

