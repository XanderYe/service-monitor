package cn.xanderye.controller;

import cn.xanderye.base.ResultBean;
import cn.xanderye.entity.AccessLog;
import cn.xanderye.service.AccessLogService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author XanderYe
 * @description:
 * @date 2021/7/29 18:49
 */
@RestController
@RequestMapping("system")
public class SystemController {
    @Autowired
    private AccessLogService accessLogService;

    /**
     * 分页获取日志
     * @param pageNum
     * @param pageSize
     * @return cn.xanderye.base.ResultBean<com.github.pagehelper.PageInfo>
     * @author XanderYe
     * @date 2021/7/29
     */
    @GetMapping("getAccessLogList")
    public ResultBean<Page<AccessLog>> getAccessLogList(Integer pageNum, Integer pageSize) {
        pageNum = pageNum == null || pageNum < 0 ? 0 : pageNum;
        pageSize = pageSize == null || pageSize < 0 ? 12 : pageSize;
        return new ResultBean<>(accessLogService.getAccessLogPageInfo(pageNum, pageSize));
    }
}
