package cn.xanderye.controller;

import cn.xanderye.base.ResultBean;
import cn.xanderye.base.UserContextHolder;
import cn.xanderye.entity.User;
import cn.xanderye.enums.ErrorCodeEnum;
import cn.xanderye.exception.BusinessException;
import cn.xanderye.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Xander on 2018-11-05.
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("login")
    public ResultBean login(String username, String password) {
        return new ResultBean<>(this.userService.login(username, password));
    }

    @PostMapping("changePwd")
    public ResultBean changePwd(String oldPwd, String newPwd) {
        this.userService.changePwd(oldPwd, newPwd);
        return new ResultBean<>();
    }

    @PostMapping("register")
    public ResultBean register(User user, String uuid, String verCode) {
        this.userService.register(user, uuid, verCode);
        return new ResultBean<>();
    }

    @GetMapping("check")
    public ResultBean checkUser(String username) {
        if (StringUtils.isEmpty(username)) {
            throw new BusinessException(ErrorCodeEnum.PARAMETER_EMPTY);
        }
        User findUser = userService.findUserByUsername(username);
        if (findUser != null) {
            return ResultBean.error(ErrorCodeEnum.ACCOUNT_EXIST);
        }
        return new ResultBean<>();
    }

    @GetMapping("info")
    public ResultBean info() {
        User user = UserContextHolder.get();
        user.setPassword(null);
        return new ResultBean(user);
    }

    @GetMapping("logout")
    public ResultBean logout() {
        userService.logout();
        return new ResultBean();
    }
}
