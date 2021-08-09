package cn.xanderye.controller;


import cn.xanderye.base.ResultBean;
import cn.xanderye.base.UserContextHolder;
import cn.xanderye.entity.ServiceConfig;
import cn.xanderye.entity.User;
import cn.xanderye.redis.RedisUtil;
import cn.xanderye.service.IServiceConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yezhendong
 * @since 2021-08-06
 */
@RestController
@RequestMapping("/serviceConfig")
public class ServiceConfigController {
    @Autowired
    private IServiceConfigService serviceConfigService;
    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("getList")
    public ResultBean getList() {
        List<ServiceConfig> list = serviceConfigService.list();
        return new ResultBean(list);
    }

    @GetMapping("getById")
    public ResultBean getById(Long id) {
        ServiceConfig serviceConfig = serviceConfigService.getFullServiceConfig(id, null);
        return new ResultBean(serviceConfig);
    }

    @PostMapping("save")
    public ResultBean save(@RequestBody ServiceConfig serviceConfig) {
        User user = UserContextHolder.get();
        serviceConfigService.saveServiceConfig(serviceConfig, user);
        return new ResultBean();
    }

    @PostMapping("delete")
    public ResultBean delete(String id) {
        serviceConfigService.removeById(id);
        return new ResultBean();
    }

    @GetMapping("getPublish")
    public ResultBean getPublish() {
        Boolean isPublish = (Boolean) redisUtil.get("isPublish");
        if (isPublish == null) {
            isPublish = false;
        }
        return new ResultBean(isPublish);
    }

    @PostMapping("setPublish")
    public ResultBean setPublish() {
        redisUtil.set("isPublish", true, 30, TimeUnit.MINUTES);
        return new ResultBean();
    }

    @PostMapping("resetPublish")
    public ResultBean resetPublish() {
        redisUtil.delete("isPublish");
        return new ResultBean();
    }
}

