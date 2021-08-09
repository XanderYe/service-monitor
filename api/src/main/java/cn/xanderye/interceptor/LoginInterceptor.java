package cn.xanderye.interceptor;

import cn.xanderye.base.UserContextHolder;
import cn.xanderye.entity.User;
import cn.xanderye.enums.ErrorCodeEnum;
import cn.xanderye.exception.BusinessException;
import cn.xanderye.redis.RedisUtil;
import cn.xanderye.service.UserService;
import cn.xanderye.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * Created by Xander on 2018-11-05.
 */
@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String remoteAddr = RequestUtil.getIpAddress(request);
        String method = request.getMethod();
        String uri = request.getRequestURI();

        //不拦截OPTION请求
        if (method.equals(RequestMethod.OPTIONS.name())) {
            return true;
        }

        String userToken = request.getHeader("token");
        log.info("remoteAddr={},  method={}, uri={}, userToken={}", remoteAddr, method, uri, userToken);

        if (userToken == null) {
            throw new BusinessException(ErrorCodeEnum.AUTHENTICATION_EXCEPTION);
        }

        // 先去缓存里查
        User user = (User) redisUtil.get("user_" + userToken);
        if (user == null) {
            // 再去数据库查
            user = userService.findByToken(userToken);
            if (user == null) {
                throw new BusinessException(ErrorCodeEnum.AUTHENTICATION_EXCEPTION, "remoteAddr={}, method={}, uri={}, userToken={}", remoteAddr, method, uri, userToken);
            }
            redisUtil.set("user_" + user.getToken(), user, 7, TimeUnit.DAYS);
        }

        UserContextHolder.set(user);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContextHolder.reset();
    }
}
