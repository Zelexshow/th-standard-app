package com.bytepound.thstandapp.business.config;

import com.bytepound.thstandapp.business.constant.ErrorCode;
import com.bytepound.thstandapp.business.model.CommonResult;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 处理请求参数格式错误 @RequestBody上validate失败后抛出的异常是MethodArgumentNotValidException异常。
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResult<String> handleValidationException(MethodArgumentNotValidException e) {
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        String message = allErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(";"));
        return CommonResult.getFailureResult(ErrorCode.INVALID_PARAMS.getCode()
                , ErrorCode.INVALID_PARAMS.getMsg(message));
    }

    /**
     * 处理请求参数格式错误 @RequestParam上validate失败后抛出的异常是javax.validation.ConstraintViolationException,
     * 注意此时，应该在controller类上加@Validated开启验证
     * @param e
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public CommonResult<String> handleCVValidationException(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ":" + violation.getMessage())
                .collect(Collectors.joining(";"));
        // 将验证失败的信息转换为自定义的错误信息
        return CommonResult.getFailureResult(ErrorCode.INVALID_PARAMS.getCode(), ErrorCode.INVALID_PARAMS.getMsg(message));
    }

    /**
     * 处理其他异常
     * DEBUG时建议注销
     */
    @ExceptionHandler(Exception.class)
    public CommonResult<String> handleException(Exception e) {
        e.printStackTrace();
        log.error(e.toString());
        return CommonResult.getFailureResult(ErrorCode.INVALID_PARAMS.getCode()
                , ErrorCode.INVALID_PARAMS.getMsg(e));
    }
}
