package cn.xanderye.service.impl;

import cn.xanderye.entity.ServiceMailContact;
import cn.xanderye.mapper.ServiceMailContactMapper;
import cn.xanderye.service.IServiceMailContactService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
public class ServiceMailContactServiceImpl extends ServiceImpl<ServiceMailContactMapper, ServiceMailContact> implements IServiceMailContactService {

    @Override
    public void deleteByServiceId(Long serviceId) {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("service_id", serviceId);
        removeByMap(columnMap);
    }
}
