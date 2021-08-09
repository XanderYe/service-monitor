package cn.xanderye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class ServiceMailContact implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Integer serviceId;

    private Integer contactId;


}
