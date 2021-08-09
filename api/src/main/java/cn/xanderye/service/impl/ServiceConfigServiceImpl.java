package cn.xanderye.service.impl;

import cn.xanderye.entity.*;
import cn.xanderye.mapper.ServiceConfigMapper;
import cn.xanderye.redis.RedisUtil;
import cn.xanderye.service.*;
import cn.xanderye.util.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
public class ServiceConfigServiceImpl extends ServiceImpl<ServiceConfigMapper, ServiceConfig> implements IServiceConfigService {
    @Autowired
    private ServiceConfigMapper serviceConfigMapper;
    @Autowired
    private IServiceDingtalkContactService serviceDingtalkContactService;
    @Autowired
    private IServiceMailContactService serviceMailContactService;

    @Override
    public List<ServiceConfig> getEnableServiceConfigList() {
        LambdaQueryWrapper<ServiceConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ServiceConfig::getStatus, 0);
        return list(queryWrapper);
    }

    @Cacheable(value = "serviceConfig", key = "#id")
    @Override
    public ServiceConfig getFullServiceConfig(Long id, ServiceConfig serviceConfig) {
        if (serviceConfig != null) {
            return serviceConfig;
        }
        serviceConfig = serviceConfigMapper.getFullServiceConfig(id);
        Map<String, List<Long>> dingtalkContactMap = serviceDingtalkContactService.getDingtalkContactMap(id);
        serviceConfig.setServiceDingtalkContactMap(dingtalkContactMap);
        return serviceConfig;
    }

    @CachePut(value = "serviceConfig", key = "#serviceConfig.id")
    @Override
    public ServiceConfig saveServiceConfig(ServiceConfig serviceConfig, User user) {
        boolean update = false;
        if (serviceConfig.getId() == null) {
            serviceConfig.setCreator(user.getNickname());
            serviceConfig.setCreateTime(LocalDateTime.now());
            save(serviceConfig);
        } else {
            update = true;
            serviceConfig.setUpdater(user.getNickname());
            serviceConfig.setUpdateTime(LocalDateTime.now());
            updateById(serviceConfig);
        }
        Long serviceId = serviceConfig.getId();
        if (update) {
            serviceMailContactService.deleteByServiceId(serviceConfig.getId());
            serviceDingtalkContactService.deleteByServiceId(serviceId);
        }
        // 重新绑定邮箱联系人
        List<Long> contactIdList = serviceConfig.getMailContactList();
        List<ServiceMailContact> mailContactList = contactIdList.stream()
                .map(id -> new ServiceMailContact(serviceId, id)).collect(Collectors.toList());
        serviceMailContactService.saveBatch(mailContactList);

        // 重新绑定钉钉联系人
        List<ServiceDingtalkContact> dingtalkContactList = new ArrayList<>();
        Map<String, List<Long>> dingtalkContactMap = serviceConfig.getServiceDingtalkContactMap();
        for (String key : dingtalkContactMap.keySet()) {
            List<Long> contactList = dingtalkContactMap.get(key);
            List<ServiceDingtalkContact> list = contactList.stream()
                    .map(id -> new ServiceDingtalkContact(serviceId, Long.valueOf(key), id)).collect(Collectors.toList());
            dingtalkContactList.addAll(list);
        }
        serviceDingtalkContactService.saveBatch(dingtalkContactList);
        return serviceConfig;
    }
}
