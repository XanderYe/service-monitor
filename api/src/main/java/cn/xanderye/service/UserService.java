package cn.xanderye.service;

import cn.xanderye.entity.User;
import cn.xanderye.vo.UserVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author XanderYe
 * @description:
 * @date 2021/7/25 17:49:24
 */
public interface UserService extends IService<User> {
    /**
     * 根据token查询用户
     * @param token
     * @return cn.xanderye.tbautosign.entity.User
     * @author XanderYe
     * @date 2020/8/27
     */
    User findByToken(String token);

    /**
     * 登录
     * @param username
     * @param password
     * @return cn.xanderye.tbautosign.VO.UserVo
     * @author XanderYe
     * @date 2020/8/27
     */
    UserVO login(String username, String password);

    /**
     * 修改密码
     * @param newPwd
     * @param newPwd
     * @return void
     * @author XanderYe
     * @date 2021/7/26
     */
    void changePwd(String oldPwd, String newPwd);

    /**
     * 注册
     * @param user
     * @param uuid
     * @param verCode
     * @return void
     * @author XanderYe
     * @date 2020/8/27
     */
    void register(User user, String uuid, String verCode);

    /**
     * 注销方法
     * @param
     * @return void
     * @author XanderYe
     * @date 2021/7/27
     */
    void logout();

    /**
     * 根据用户名查找
     * @param username
     * @return cn.xanderye.tbautosign.entity.User
     * @author XanderYe
     * @date 2020/8/31
     */
    User findUserByUsername(String username);
}
