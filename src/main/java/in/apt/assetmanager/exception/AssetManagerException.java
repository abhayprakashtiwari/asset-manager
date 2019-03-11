package in.apt.assetmanager.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class AssetManagerException extends Exception {

    @NonNull
    private ExceptionData exceptionData;

    private HttpStatus status = HttpStatus.BAD_REQUEST;


}
