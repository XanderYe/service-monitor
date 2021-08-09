package cn.xanderye.base;

import cn.xanderye.enums.ErrorCodeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 消息返回类
 */
@Data
@Accessors(chain = true)
public class ResultBean<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private String msg = "success";

    private int code = 0;
    private T data;

    public ResultBean() {
    }

    public ResultBean(int code, String message) {
        this.code = code;
        this.msg = message;
    }

    public ResultBean(T data) {
        this.data = data;
    }

    /**
     * 成功
     */
    public static <T> ResultBean success(T data) {
        return new ResultBean<T>().setData(data);
    }

    /**
     * 失败
     * @param errorCode
     */
    public static ResultBean error(ErrorCodeEnum errorCode) {
        return new ResultBean(errorCode.getCode(), errorCode.getMessage());
    }
}