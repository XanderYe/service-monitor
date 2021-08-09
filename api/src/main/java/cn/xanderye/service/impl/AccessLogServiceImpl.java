package cn.xanderye.service.impl;

import cn.xanderye.entity.AccessLog;
import cn.xanderye.mapper.AccessLogMapper;
import cn.xanderye.service.AccessLogService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author XanderYe
 * @description:
 * @date 2021/7/11 18:40
 */
@Service
public class AccessLogServiceImpl extends ServiceImpl<AccessLogMapper, AccessLog> implements AccessLogService {
    @Autowired
    private AccessLogMapper accessLogMapper;

    @Override
    public Page<AccessLog> getAccessLogPageInfo(int pageNum, int pageSize) {
        Page<AccessLog> page = new Page<>(pageNum, pageSize);
        return accessLogMapper.getAccessLogList(page);
    }

    @Override
    public void logSystemError(String message) {
        AccessLog accessLog = new AccessLog();
        accessLog.setModuleName("系统错误");
        accessLog.setMessage(message);
        accessLog.setResult(null);
        accessLog.setCreateTime(LocalDateTime.now());
        save(accessLog);
    }
}
