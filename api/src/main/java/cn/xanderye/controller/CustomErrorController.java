package cn.xanderye.controller;

import cn.xanderye.base.ResultBean;
import cn.xanderye.enums.ErrorCodeEnum;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author XanderYe
 * @description:
 * @date 2021/7/31 19:43
 */
@RestController
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResultBean handleError(HttpServletRequest request){
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == 500) {
            return ResultBean.error(ErrorCodeEnum.AUTHENTICATION_EXCEPTION);
        }
        return ResultBean.error(ErrorCodeEnum.getErrorByCode(statusCode));
    }
}
