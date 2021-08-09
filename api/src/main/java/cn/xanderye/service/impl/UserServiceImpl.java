package cn.xanderye.service.impl;

import cn.xanderye.base.UserContextHolder;
import cn.xanderye.constant.Constant;
import cn.xanderye.entity.User;
import cn.xanderye.enums.ErrorCodeEnum;
import cn.xanderye.exception.BusinessException;
import cn.xanderye.mapper.UserMapper;
import cn.xanderye.redis.RedisUtil;
import cn.xanderye.service.UserService;
import cn.xanderye.util.MD5Util;
import cn.xanderye.util.SecureRandomUtil;
import cn.xanderye.vo.UserVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * @author XanderYe
 * @description:
 * @date 2021/7/25 17:51:47
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    private RedisUtil redisUtil;

    private static final Pattern LETTER_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]{4,16}$");

    @Override
    public User findByToken(String token) {
        return userMapper.findByToken(token);
    }

    @Override
    public UserVO login(String username, String password) {
        UserVO userVo = new UserVO();
        if ((StringUtils.isEmpty(username)) || (StringUtils.isEmpty(password))) {
            throw new BusinessException(ErrorCodeEnum.PARAMETER_EMPTY, "username={},password={}", username, password);
        }
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new BusinessException(ErrorCodeEnum.ACCOUNT_OR_PASSWORD_ERROR, "username={},password={}", username, password);
        }
        // 是否禁用账户
        if (1 == user.getStatus()) {
            throw new BusinessException(ErrorCodeEnum.ACCOUNT_BANNED, "username={},password={}", username);
        }
        try {
            if (!MD5Util.validatePwd(user.getPassword(), Constant.SALT, password)) {
                throw new BusinessException(ErrorCodeEnum.ACCOUNT_OR_PASSWORD_ERROR, "user={}", user);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        redisUtil.set("user_" + user.getToken(), user, 7, TimeUnit.DAYS);
        userVo.setId(user.getId());
        userVo.setUsername(user.getUsername());
        userVo.setNickname(user.getNickname());
        userVo.setToken(user.getToken());
        return userVo;
    }

    @Override
    public void changePwd(String oldPwd, String newPwd) {
        User user = UserContextHolder.get();
        user = userMapper.selectById(user.getId());
        try {
            if (!MD5Util.validatePwd(user.getPassword(), Constant.SALT, oldPwd)) {
                throw new BusinessException(ErrorCodeEnum.ACCOUNT_PASSWORD_ERROR);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String newPassword = MD5Util.encryptPwd(newPwd, Constant.SALT);
        user.setPassword(newPassword);
        userMapper.updateById(user);
    }

    @Override
    public void register(User user, String uuid, String verCode) {
        String code = (String) redisUtil.get("captcha_" + uuid);
        redisUtil.delete("captcha_" + uuid);
        if (StringUtils.isEmpty(code) || !code.toLowerCase().equals(verCode.toLowerCase())) {
            throw new BusinessException(ErrorCodeEnum.CAPTCHA_ERROR, "code={},verCode={}", code, verCode);
        }
        if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())) {
            throw new BusinessException(ErrorCodeEnum.PARAMETER_EMPTY, "username={},password={}", user.getUsername(), user.getPassword());
        }
        if (!LETTER_PATTERN.matcher(user.getUsername()).matches()) {
            throw new BusinessException("用户名应只包含英文、数字，并且4-16位");
        }
        User tmp = userMapper.findByUsername(user.getUsername());
        if (tmp != null) {
            throw new BusinessException(ErrorCodeEnum.ACCOUNT_EXIST, "username={},password={}", user.getUsername(), user.getPassword());
        }
        if (user.getPassword().length() < 6) {
            throw new BusinessException(ErrorCodeEnum.UNSAFE_PASSWORD, "username={},password={}", user.getUsername(), user.getPassword());
        }
        log.info("注册用户，用户名：{}，昵称：{}，密码：{}", user.getUsername(), user.getNickname(), user.getPassword());
        user.setPassword(MD5Util.encryptPwd(user.getPassword(), Constant.SALT));
        user.setToken(SecureRandomUtil.nextHex(64));
        user.setStatus(0);
        user.setCreateTime(LocalDateTime.now());
        userMapper.insert(user);
    }

    @Override
    public void logout() {
        User user = UserContextHolder.get();
        redisUtil.delete("user_" + user.getToken());
    }

    @Override
    public User findUserByUsername(String username) {
        return userMapper.findByUsername(username);
    }
}
