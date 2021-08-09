package cn.xanderye.mapper;

import cn.xanderye.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author XanderYe
 * @description:
 * @date 2021/7/25 17:49:02
 */
public interface UserMapper extends BaseMapper<User> {

    User findByUsername(String username);

    User findByToken(String token);
}
