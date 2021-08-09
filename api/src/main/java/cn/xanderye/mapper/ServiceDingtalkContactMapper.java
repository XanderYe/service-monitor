package cn.xanderye.mapper;

import cn.xanderye.entity.ServiceDingtalkContact;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yezhendong
 * @since 2021-08-06
 */
public interface ServiceDingtalkContactMapper extends BaseMapper<ServiceDingtalkContact> {


    List<ServiceDingtalkContact> selectServiceDingtalkContactListByServiceId(Long serviceId);
}
