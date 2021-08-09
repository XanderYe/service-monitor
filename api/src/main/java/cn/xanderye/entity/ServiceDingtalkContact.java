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
public class ServiceDingtalkContact implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long serviceId;

    private Long dingtalkId;

    private Long contactId;

    public ServiceDingtalkContact() {

    }

    public ServiceDingtalkContact(Long serviceId, Long dingtalkId, Long contactId) {
        this.serviceId = serviceId;
        this.dingtalkId = dingtalkId;
        this.contactId = contactId;
    }
}
