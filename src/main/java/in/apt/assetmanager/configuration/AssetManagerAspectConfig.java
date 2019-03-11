
package in.apt.assetmanager.configuration;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Aspect
@ConditionalOnProperty("spring.aop.logging")
public class AssetManagerAspectConfig {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.aop.log.time:false}")
    private boolean logTime;

    @Value("${spring.aop.log.method.args:false}")
    private boolean logMethodArguments;

    @Around("execution(* in.apt.assetmanager..*.*(..))")
    public Object logMethodCall(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        if (logTime) stopWatch.start();
        Object returnValue = joinPoint.proceed();
        if (logTime) stopWatch.stop();

        StringBuffer logMessage = new StringBuffer();
        appendClassMethodName(joinPoint, logMessage);
        appendMethodArgs(joinPoint, logMessage);
        appendTimeTaken(stopWatch, logMessage);
        logMessage.append(" Returning ");
        appendReturnArgs(returnValue, logMessage);
        log.info(logMessage.toString());
        return returnValue;
    }

    private void appendTimeTaken(StopWatch stopWatch, StringBuffer logMessage) {
        if (logTime){
            logMessage.append(" execution time ")
                    .append(stopWatch.getTotalTimeMillis())
                    .append(" ms");
        }
    }

    private void appendClassMethodName(ProceedingJoinPoint joinPoint, StringBuffer logMessage) {
        logMessage.append(joinPoint.getTarget().getClass().getName())
                .append(".")
                .append(joinPoint.getSignature().getName())
                .append("(");
    }

    private void appendMethodArgs(ProceedingJoinPoint joinPoint, StringBuffer logMessage) {
        if (logMethodArguments) {
            Object[] args = joinPoint.getArgs();
            for (Object arg : args) {
                logMessage.append(arg).append(",");
            }
            if (args.length>0) logMessage.deleteCharAt(logMessage.length()-1);

        }
        logMessage.append(")");
    }

    private void appendReturnArgs(Object returnValue, StringBuffer logMessage){
        if(logMethodArguments && returnValue != null){
            logMessage.append(" : (");
            logMessage.append(returnValue.toString());
            logMessage.append(" ) ");
        }
    }
}

