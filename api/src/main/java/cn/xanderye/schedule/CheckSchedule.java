package cn.xanderye.schedule;

import cn.xanderye.entity.ServiceConfig;
import cn.xanderye.service.IServiceConfigService;
import cn.xanderye.service.ScheduleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author yezhendong
 * @description:
 * @date 2021/8/9 15:31
 */
@EnableScheduling
@Component
public class CheckSchedule {
    @Autowired
    private IServiceConfigService serviceConfigService;
    @Autowired
    private ScheduleService scheduleService;

    @Scheduled(cron = "0 */5 * * * ? ")
    public void checkService() {
        List<ServiceConfig> serviceConfigList = serviceConfigService.getEnableServiceConfigList();
        if (!serviceConfigList.isEmpty()) {
            ExecutorService executorService = Executors.newFixedThreadPool(8);
            for (ServiceConfig serviceConfig : serviceConfigList) {
                executorService.execute(() -> {
                    scheduleService.checkService(serviceConfig);
                });
            }
            executorService.shutdown();
        }
    }


}
