package com.iprody.product.service.logging.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class BusinessOpsLoggingAspect {

    /**
     * This method is around advice for methods annotated with {@link BusinessOpsLogging}.
     * It logs the method name, class name, arguments before the method execution,
     * and the return value or exception after the method execution.
     *
     * @param joinPoint     the proceeding join point
     * @param businessOpsLogging the {@link BusinessOpsLogging} annotation
     * @return the result of the method execution
     * @throws Throwable if an error occurs during method execution
     */
    @Around("@annotation(businessOpsLogging)")
    public Object aroundCallForAddLogging(ProceedingJoinPoint joinPoint,
                                          BusinessOpsLogging businessOpsLogging) throws Throwable {
        try {
            log.atLevel(businessOpsLogging.level())
                    .setMessage(
                            String.format("Initiating business operation on the %s: %s, with input Arguments: %s.",
                                    businessOpsLogging.domain(),
                                    joinPoint.getSignature().getName(),
                                    Arrays.toString(joinPoint.getArgs())
                            )
                    ).log();
            final Object result = joinPoint.proceed();
            log.atLevel(businessOpsLogging.level())
                    .setMessage(
                            String.format("Completion of business operation on the %s: %s, Resulting value: %s.",
                                    businessOpsLogging.domain(),
                                    joinPoint.getSignature().getName(),
                                    result
                            )
                    ).log();
            return result;
        } catch (Throwable ex) {
            log.atLevel(businessOpsLogging.level())
                    .setMessage(
                            String.format("Exception encountered during business operation on the %s: %s, Message: %s",
                                    businessOpsLogging.domain(),
                                    joinPoint.getSignature().getName(),
                                    ex.getMessage()
                            )
                    ).log();
            throw ex;
        }
    }

}
