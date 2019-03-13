package in.apt.assetmanager.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;

@ControllerAdvice
public class AssetManagerExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(AssetManagerExceptionHandler.class);

    @ExceptionHandler(value = AssetManagerException.class)
    @ResponseBody
    public ResponseEntity<?> handleAssetManagerException(AssetManagerException exception){
        log.error(buildStackTraceForLogging(exception.getStackTrace()));
        return ResponseEntity.status(exception.getStatus()).body(exception.getExceptionData());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity<?> handleGenericException(Exception exception){
        log.error(buildStackTraceForLogging(exception.getStackTrace()));
        return ResponseEntity.badRequest().body(ErrorConstants.genericError);
    }

    private String buildStackTraceForLogging(StackTraceElement[] elements){
        StringBuilder stringBuilder = new StringBuilder();
        Arrays.stream(elements).forEach(stackTraceElement -> stringBuilder.append(stackTraceElement.toString()));
        return stringBuilder.toString();
    }
}
