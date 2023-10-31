package com.martini.sparrow.exception.handler;

import com.martini.sparrow.exception.NotFoundException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理类
 * @author martini at 2020/12/14 8:49
 */
public class GlobalExceptionResolver implements HandlerExceptionResolver {
    /**
     * 处理异常发生时的行为
     * @param request 当前的请求对象
     * @param response 当前的响应对象
     * @param handler 当前请求的方法对象
     * @param ex 出现的异常
     * @return 出现异常时展示的视图和模型数据
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ModelAndView mav = new ModelAndView();
        if (ex instanceof NotFoundException) mav.setViewName("error/404");
        return mav;
    }
}
