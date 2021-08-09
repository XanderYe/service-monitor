package cn.xanderye.service;

import cn.xanderye.entity.ServiceDingtalkContact;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yezhendong
 * @since 2021-08-06
 */
public interface IServiceDingtalkContactService extends IService<ServiceDingtalkContact> {

    Map<String, List<Long>> getDingtalkContactMap(Long serviceId);

    void deleteByServiceId(Long serviceId);
}
