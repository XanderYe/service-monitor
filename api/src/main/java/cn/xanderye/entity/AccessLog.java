package cn.xanderye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author XanderYe
 * @description:
 * @date 2021/7/11 18:37
 */
@Data
public class AccessLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String ip;

    private String moduleName;

    private String message;

    private String result;

    private LocalDateTime createTime;

    @TableField(exist = false)
    private User user;
}
