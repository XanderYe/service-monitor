package cn.xanderye.mapper;

import cn.xanderye.entity.AccessLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @author XanderYe
 * @description:
 * @date 2021/7/11 18:39
 */
public interface AccessLogMapper extends BaseMapper<AccessLog> {

    Page<AccessLog> getAccessLogList(Page<AccessLog> page);
}
