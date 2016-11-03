package com.quxin.freshfun.service.aop;

import com.alibaba.fastjson.JSON;
import com.quxin.freshfun.utils.BusinessException;
import com.quxin.freshfun.utils.ResultUtil;
import org.apache.http.HttpResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 记录请求日志的切面,记录请求参数、返回结果以及处理耗时
 * Created by baoluo on 15/12/7.
 */
@Aspect
@Service
public class RequestLogAspect {

    private static Logger logger = LoggerFactory.getLogger(RequestLogAspect.class);

   @Around("execution(public * com.quxin.freshfun.controller.*.*.*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object[] args = joinPoint.getArgs();

        String resultString = null;

        try{
            preLog(joinPoint.toString(),args);
            Object ret = joinPoint.proceed();
            return ret;
        } catch (BusinessException busE) {
            logger.error("业务异常" ,busE.getMessage());
            return ResultUtil.fail(4004,busE.getMessage());
        } catch (Throwable t){
            logger.error("系统异常" ,t);
            return ResultUtil.fail(4004,"系统异常");
        } finally {
            long cost = System.currentTimeMillis() - start;
            afterLog(joinPoint.toString(), resultString, cost);
        }
    }


    private void  preLog(String method,Object[] args){
        StringBuilder paramString = new StringBuilder();
        if (args != null) {
            for (Object obj : args) {
                if (obj instanceof HttpServletRequest || obj instanceof HttpServletResponse ) {
                    obj = "";
                }
                paramString.append(JSON.toJSONString(obj)).append(",");
            }
        }


        StringBuilder sb = new StringBuilder(200);
        sb.append("method:").append(method);
        sb.append(" ").append("parameter:").append(paramString);
        sb.append(" ").append("start");
        logger.info(sb.toString());
    }

    private void  afterLog(String method, String result, long cost){

        StringBuilder sb = new StringBuilder(200);
        sb.append("method:").append(method);
        sb.append(" ").append("result:").append(result);
        sb.append(" ").append("cost:").append(cost).append("ms");
        sb.append(" ").append("end");
        logger.info(sb.toString());
    }
}
