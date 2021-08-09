package cn.xanderye.vo;

import lombok.Data;

/**
 * Created by Xander on 2018-11-05.
 */
@Data
public class UserVO {
    private Long id;

    private String username;

    private String nickname;

    private String token;

    private String avatar;
    
    private Integer role;
}