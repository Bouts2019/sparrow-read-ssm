package com.martini.sparrow.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.martini.sparrow.vo.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

/**
 * 因为控制器基本都是要统一响应的，因此抽离出来统一的操作
 * @author martini at 2020/12/15 8:29
 */
@Component
@Aspect
@Slf4j
public class ControllerAspect {
    @Pointcut(value = "execution(* com.martini.sparrow.controller.restful.*.*(..))")
    public void controllerPointcut() {}

    @Around("controllerPointcut()")
    public Object wrapResponse(ProceedingJoinPoint joinPoint) {
        CommonResponse commonResponse = new CommonResponse();
        try {
            Object[] args = joinPoint.getArgs();
            Object res = joinPoint.proceed(args);
            commonResponse.setStatus(true);
            commonResponse.setMessage("ok");
            commonResponse.setData(res);
        } catch (Throwable e) {
            commonResponse.setStatus(false);
            commonResponse.setMessage(e.getMessage());
            commonResponse.setData(null);
            e.printStackTrace();
        }
        return commonResponse;
    }
}
