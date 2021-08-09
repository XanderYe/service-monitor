package cn.xanderye.aop;

import cn.xanderye.base.UserContextHolder;
import cn.xanderye.entity.AccessLog;
import cn.xanderye.entity.User;
import cn.xanderye.redis.RedisUtil;
import cn.xanderye.service.AccessLogService;
import cn.xanderye.util.RequestUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author XanderYe
 * @description:
 * @date 2021/7/10 22:00:35
 */
@Slf4j
@Aspect
@Component
public class LogAspect {
    @Autowired
    private AccessLogService accessLogService;

    private static final List<String> NO_SAVE_LIST = new ArrayList<String>(){{

    }};

    @Pointcut("execution(* cn.xanderye.controller..*(..))")
    private void pointcut(){}

    @Pointcut("@annotation(cn.xanderye.aop.Log)")
    private void logAnnotation(){}

    @Around(value = "logAnnotation()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object obj;
        obj = pjp.proceed();
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        if (method.isAnnotationPresent(Log.class)) {
            Log aLog = method.getAnnotation(Log.class);
            String methodName = pjp.getSignature().getName();
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = servletRequestAttributes.getRequest();
            Object[] args = pjp.getArgs();
            recordAccessLog(methodName, args, aLog, obj, request);
        }
        return obj;
    }

    private void recordAccessLog(String methodName, Object[] args, Log aLog, Object result, HttpServletRequest request) {
        String ip = RequestUtil.getIpAddress(request);
        AccessLog accessLog = new AccessLog();
        User user = UserContextHolder.get();
        accessLog.setUserId(user.getId());
        if (StringUtils.isNotEmpty(aLog.methodName())) {
            methodName = aLog.methodName();
        }
        String str = MessageFormat.format("{0}请求[{1}]模块，[{2}]方法，参数：{3}，用户：{4}", ip, aLog.moduleName(), methodName, Arrays.toString(args), user.getNickname());
        log.info(str);
        boolean save = true;
        String resultStr = null;
        if (aLog.logResult() && result != null) {
            try {
                resultStr = JSON.toJSONString(result);
            } catch (Exception e) {
                resultStr = result.toString();
            }
            // 不保存的日志
            if (NO_SAVE_LIST.contains(resultStr)) {
                save = false;
            }
        }
        accessLog.setIp(ip);
        accessLog.setModuleName(aLog.moduleName());
        accessLog.setMessage(str);
        accessLog.setResult(resultStr);
        accessLog.setCreateTime(LocalDateTime.now());
        if (save) {
            accessLogService.save(accessLog);
        }
    }
}
