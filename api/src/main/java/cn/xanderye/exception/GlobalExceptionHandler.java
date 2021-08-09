package cn.xanderye.exception;

import cn.xanderye.base.ResultBean;
import cn.xanderye.enums.ErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * 全局异常处理
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception e, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ResultBean<>(status.value(), status.getReasonPhrase()), HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleRuntimeException(RuntimeException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(ResultBean.error(ErrorCodeEnum.INTERNAL_ERROR), HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleBusinessException(BusinessException e) {
        log.error("message：{}，snapshot：{}", e.getMessage(), e.getSnapshot());
        return new ResponseEntity<>(new ResultBean<>(e.getCode(), e.getMessage()), HttpStatus.OK);
    }
}
