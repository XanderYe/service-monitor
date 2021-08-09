package cn.xanderye.service;

import cn.xanderye.entity.ServiceConfig;
import cn.xanderye.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yezhendong
 * @since 2021-08-06
 */
public interface IServiceConfigService extends IService<ServiceConfig> {

    List<ServiceConfig> getEnableServiceConfigList();

    ServiceConfig getFullServiceConfig(Long id, ServiceConfig serviceConfig);

    ServiceConfig saveServiceConfig(ServiceConfig serviceConfig, User user);
}
