package cn.xanderye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author yezhendong
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DingtalkBot implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    private String token;

    private String secret;

    private String creator;

    private LocalDateTime createTime;

    private String updater;

    private LocalDateTime updateTime;


}
