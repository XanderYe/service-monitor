package cn.xanderye.service.impl;

import cn.xanderye.entity.ServiceConfig;
import cn.xanderye.mapper.ServiceConfigMapper;
import cn.xanderye.service.IServiceConfigService;
import cn.xanderye.service.IServiceDingtalkContactService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yezhendong
 * @since 2021-08-06
 */
@Service
public class ServiceConfigServiceImpl extends ServiceImpl<ServiceConfigMapper, ServiceConfig> implements IServiceConfigService {
    @Autowired
    private ServiceConfigMapper serviceConfigMapper;
    @Autowired
    private IServiceDingtalkContactService serviceDingtalkContactService;

    @Override
    public ServiceConfig getFullServiceConfig(Long id) {
        ServiceConfig serviceConfig = serviceConfigMapper.getFullServiceConfig(id);
        Map<String, List<Long>> dingtalkContactMap = serviceDingtalkContactService.getDingtalkContactMap(id);
        serviceConfig.setServiceDingtalkContactMap(dingtalkContactMap);
        return serviceConfig;
    }
}
