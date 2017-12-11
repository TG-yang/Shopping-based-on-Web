package cn.polar.search.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理器
 */

public class GlobalExceptionResolver implements HandlerExceptionResolver {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionResolver.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        // 写出错误日志
        logger.error("系统发生异常", e);

        // 错误页面展示
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message", "系统发生异常，请稍后再试");
        modelAndView.setViewName("error/exception");
        return modelAndView;
    }
}
