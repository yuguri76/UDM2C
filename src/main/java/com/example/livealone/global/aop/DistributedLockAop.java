package com.example.livealone.global.aop;

import com.example.livealone.global.config.RedissonConfig;
import com.example.livealone.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Locale;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DistributedLockAop {

    private static final String REDISSON_LOCK_PREFIX = "LOCK:";

    private final RedissonClient redissonClient;
    private final AopForTransaction aopForTransaction;
    private final MessageSource messageSource;

    @Around("@annotation(com.example.livealone.global.aop.DistributedLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable{
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        String key = REDISSON_LOCK_PREFIX + CustomSpringELParser.getDynamicValue(signature.getParameterNames(), joinPoint.getArgs(), distributedLock.key());
        RLock rLock = redissonClient.getLock(key);

        try {
            boolean isLocked = rLock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.timeUnit());
            if(isLocked){
                try{
                    return aopForTransaction.proceed(joinPoint);
                }finally {
                    rLock.unlock();
                }
            }
        } catch (InterruptedException e) {
            throw new InterruptedException();
        }

        throw new CustomException(messageSource.getMessage(
                "can.not.get.lock.key",
                null,
                CustomException.DEFAULT_ERROR_MESSAGE,
                Locale.getDefault()
        ), HttpStatus.NOT_FOUND);

    }
}

