package cn.xanderye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by Xander on 2018-11-05.
 */
@Data
public class User implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String password;

    private String nickname;

    private String token;

    private Integer status;

    private LocalDateTime createTime;
}
