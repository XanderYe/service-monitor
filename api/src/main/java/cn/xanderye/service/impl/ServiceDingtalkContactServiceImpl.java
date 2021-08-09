package cn.xanderye.service.impl;

import cn.xanderye.entity.ServiceDingtalkContact;
import cn.xanderye.mapper.ServiceDingtalkContactMapper;
import cn.xanderye.service.IServiceDingtalkContactService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yezhendong
 * @since 2021-08-06
 */
@Service
public class ServiceDingtalkContactServiceImpl extends ServiceImpl<ServiceDingtalkContactMapper, ServiceDingtalkContact> implements IServiceDingtalkContactService {
    @Autowired
    private ServiceDingtalkContactMapper serviceDingtalkContactMapper;

    @Override
    public Map<String, List<Long>> getDingtalkContactMap(Long serviceId) {
        Map<String, List<Long>> map = new HashMap<>();
        List<ServiceDingtalkContact> serviceDingtalkContactList = serviceDingtalkContactMapper.selectServiceDingtalkContactListByServiceId(serviceId);
        if (!serviceDingtalkContactList.isEmpty()) {
            for (ServiceDingtalkContact serviceDingtalkContact : serviceDingtalkContactList) {
                List<Long> contactIdList = map.get(String.valueOf(serviceDingtalkContact.getDingtalkId()));
                if (contactIdList == null) {
                    contactIdList = new ArrayList<>(Collections.singletonList(serviceDingtalkContact.getContactId()));
                } else {
                    contactIdList.add(serviceDingtalkContact.getContactId());
                }
                map.put(String.valueOf(serviceDingtalkContact.getDingtalkId()), contactIdList);
            }
        }
        return map;
    }

    @Override
    public void deleteByServiceId(Long serviceId) {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("serviceId", serviceId);
        removeByMap(columnMap);
    }
}
