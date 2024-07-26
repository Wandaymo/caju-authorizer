package br.com.wandaymo.caju.authorizer.log;

import java.lang.reflect.Method;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogService.class);

    @Around(value = "execution(public * br.com.wandaymo.caju.authorizer.service..*(..))")
    public Object logMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Logged logged = method.getAnnotation(Logged.class);

        if (logged == null || logged.ignore()) {
            return joinPoint.proceed();
        }

        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = method.getName();
        Object[] args = joinPoint.getArgs();

        LOGGER.debug("Executing method {} from class {} with parameters: {}.", methodName, className, args);

        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - start;

        LOGGER.debug("Method {} from class {} took {} ms and returned: {}.", methodName, className, duration, result);
        LOGGER.info("Method {} from class {} finished successfully and took {} ms.", methodName, className, duration);

        return result;
    }

    @AfterThrowing(
            pointcut = "execution(public * br.com.wandaymo.caju.authorizer.service..*(..))",
            throwing = "exception"
    )
    public void logExceptions(JoinPoint joinPoint, Exception exception) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String[] parameterNames = signature.getParameterNames();
        Logged logged = method.getAnnotation(Logged.class);

        if (logged != null && !logged.ignore()) {
            String className = joinPoint.getTarget().getClass().getSimpleName();
            String methodName = joinPoint.getSignature().getName();
            Object[] args = joinPoint.getArgs();

            for (int i = 0; i < parameterNames.length; i++) {
                args[i] = parameterNames[i] + "=" + args[i];
            }

            LOGGER.error("An error {} occurred while executing method {} from class {} with parameters: {}",
                    exception, methodName, className, args);
        }
    }
}
