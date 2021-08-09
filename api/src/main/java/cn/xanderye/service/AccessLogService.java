package cn.xanderye.service;

import cn.xanderye.entity.AccessLog;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author XanderYe
 * @description:
 * @date 2021/7/11 18:40
 */
public interface AccessLogService extends IService<AccessLog> {

    /**
     * 分页获取日志
     * @param pageNum
     * @param pageSize
     * @return com.github.pagehelper.PageInfo<cn.xanderye.entity.AccessLog>
     * @author XanderYe
     * @date 2021/7/29
     */
    Page<AccessLog> getAccessLogPageInfo(int pageNum, int pageSize);

    /**
     * 记录错误日志
     * @param message
     * @return void
     * @author yezhendong
     * @date 2021/8/9
     */
    void logSystemError(String message);
}
