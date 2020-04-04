package com.sn.theft.aop;

import com.sn.theft.annotation.AControllerAspect;
import com.sn.theft.constant.HttpStatus;
import com.sn.theft.dto.CommonDTO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * controller try...catch...拦截
 *
 * @author sn
 */
@Component
@Aspect
@Slf4j
public class ControllerAspectAop {

    @Pointcut("@annotation(com.sn.theft.annotation.AControllerAspect)")
    public void controllerAspect() {

    }

    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String description = this.getAspectLogDescription(joinPoint);
        log.info("=====start=====");
        // 打印描述信息
        log.info("annotation description: {}", description);
        // 打印请求 url
        log.info("URL: {}", request.getRequestURL().toString());
        // 打印 Http method
        log.info("HTTP Method: {}", request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        log.info("Class Method: {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        // 打印请求的 IP
        log.info("IP: {}", request.getRemoteAddr());
        // 打印请求入参
        log.info("Request Args: {}", joinPoint.getArgs());
    }

    @Around("controllerAspect()")
    public <T> CommonDTO<T> doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        CommonDTO<T> commonDTO = null;
        try {
            Object object = proceedingJoinPoint.proceed();
            commonDTO = (CommonDTO<T>) object;
            if (StringUtils.isEmpty(commonDTO.getStatus())) {
                commonDTO.setStatus(HttpStatus.HTTP_OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (commonDTO == null) {
                commonDTO = new CommonDTO<>();
            }
            commonDTO.setStatus(HttpStatus.HTTP_INTERNAL_ERROR);
            commonDTO.setMessage(e.getMessage());
        }
        return commonDTO;
    }

    @After("controllerAspect()")
    public void doAfter() throws Throwable {
        log.info("=====End=====");
    }

    private String getAspectLogDescription(JoinPoint joinPoint)
            throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        StringBuilder description = new StringBuilder();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazz = method.getParameterTypes();
                if (clazz.length == arguments.length) {
                    description.append(method.getAnnotation(AControllerAspect.class).description());
                    break;
                }
            }
        }
        return description.toString();
    }
}
