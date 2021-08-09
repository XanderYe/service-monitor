package cn.xanderye.mapper;

import cn.xanderye.entity.ServiceConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yezhendong
 * @since 2021-08-06
 */
public interface ServiceConfigMapper extends BaseMapper<ServiceConfig> {

    ServiceConfig getFullServiceConfig(Long id);
}
