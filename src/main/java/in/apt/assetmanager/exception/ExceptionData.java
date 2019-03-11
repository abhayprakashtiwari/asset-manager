package in.apt.assetmanager.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ExceptionData {

    private String errorKey;

    private String errorMessage;
}
