package com.wuzhulin.common.aop;

import com.alibaba.fastjson.JSON;
import com.wuzhulin.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;

@Aspect
@Component
@Slf4j
public class CacheAspect {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Pointcut("@annotation(com.wuzhulin.common.aop.Cache)")
    private void pt(){}

    @Around("pt()")
    private Object around(ProceedingJoinPoint point) {
        try {
            Signature signature = point.getSignature();
            //获取类名
            String className = point.getTarget().getClass().getSimpleName();
            //获取方法名
            String methodName = signature.getName();

            Class[] parameterTypes = new Class[point.getArgs().length];
            Object[] args = point.getArgs();
            String params = "";
            for (int i = 0; i < args.length; i++) {
                if(args[i] != null) {
                    params += JSON.toJSONString(args[i]);
                    parameterTypes[i] = args[i].getClass();
                }else {
                    args[i] = null;
                }
            }
            if(StringUtils.isNoneBlank(params)) {
                params = DigestUtils.md5Hex(params);
            }
            Method method = point.getSignature().getDeclaringType().getMethod(methodName,parameterTypes);
            Cache annotation = method.getAnnotation(Cache.class);
            long expire = annotation.expire();
            String name = annotation.name();
            //生成rediskey
            String redisKey = name + "::" + className + "::" + methodName + "::" + params;

            //判断是否有缓存
            String redisValue = redisTemplate.opsForValue().get(redisKey);
            if(StringUtils.isNotBlank(redisValue)) {
                log.info("走了缓存~~ {},{}",className,methodName);
                return JSON.parseObject(redisValue, Result.class);
            }
            //如果没有缓存，先执行方法，在加入redis缓存中
            Object proceed = point.proceed();
            redisTemplate.opsForValue().set(redisKey,JSON.toJSONString(proceed), Duration.ofMillis(expire));
            log.info("存入缓存~~ {},{}",className,methodName);
            return proceed;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return Result.fail(-999,"系统错误");
    }
}
