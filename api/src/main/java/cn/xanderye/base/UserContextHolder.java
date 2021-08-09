package cn.xanderye.base;

import cn.xanderye.entity.User;
import org.springframework.core.NamedThreadLocal;

/**
 * Created by Xander on 2018-11-05.
 */
public class UserContextHolder {

    private static final ThreadLocal<User> HOLDER = new NamedThreadLocal<User>("User");

    public static void reset() {
        HOLDER.remove();
    }

    public static void set(User value) {
        HOLDER.set(value);
    }

    public static User get() {
        return (User) HOLDER.get();
    }
}