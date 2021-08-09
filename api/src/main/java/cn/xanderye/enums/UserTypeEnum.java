package cn.xanderye.enums;

import lombok.Getter;

/**
 * Created on 2020/4/27.
 */
@Getter
public enum UserTypeEnum {

    ADMIN(0, "管理员"),
    WORKER(1, "员工"),
    NORMAL(2, "用户");


    private int value;

    private String name;

    private UserTypeEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }
}
