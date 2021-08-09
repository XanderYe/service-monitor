package cn.xanderye.service;

import cn.xanderye.entity.ServiceMailContact;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yezhendong
 * @since 2021-08-06
 */
public interface IServiceMailContactService extends IService<ServiceMailContact> {

    void deleteByServiceId(Long serviceId);
}
