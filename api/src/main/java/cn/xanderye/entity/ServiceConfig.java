package cn.xanderye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

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
public class ServiceConfig implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    private String url;

    private Integer method;

    private String header;

    private String param;

    private Integer status;

    private String creator;

    private LocalDateTime createTime;

    private String updater;

    private LocalDateTime updateTime;

    @TableField(exist = false)
    private List<Long> mailContactList;

    @TableField(exist = false)
    private Map<String, List<Long>> serviceDingtalkContactMap;

    @TableField(exist = false)
    private Boolean isOnline;
}
