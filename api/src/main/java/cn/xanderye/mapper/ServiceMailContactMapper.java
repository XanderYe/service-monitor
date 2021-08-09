package cn.xanderye.mapper;

import cn.xanderye.entity.ServiceMailContact;
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
public interface ServiceMailContactMapper extends BaseMapper<ServiceMailContact> {

    List<Long> selectContactListByServiceId(Long serviceId);
}
