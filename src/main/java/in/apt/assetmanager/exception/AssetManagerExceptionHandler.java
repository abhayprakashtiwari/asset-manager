package in.apt.assetmanager.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class AssetManagerExceptionHandler {

    @ExceptionHandler(value = AssetManagerException.class)
    @ResponseBody
    public ResponseEntity<?> handleAssetManagerException(AssetManagerException exception){
        return ResponseEntity.status(exception.getStatus()).body(exception.getExceptionData());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity<?> handleGenericException(Exception exception){
        return ResponseEntity.badRequest().body(ErrorConstants.genericError);
    }
}
