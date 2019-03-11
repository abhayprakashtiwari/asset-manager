package in.apt.assetmanager.exception;

public interface ErrorConstants {

    ExceptionData genericError = new ExceptionData("GENERIC_ERROR", "Something went wrong");


    ExceptionData FAILED_TO_READ_FILE = new ExceptionData("FAILED_TO_READ_FILE", "Something went wrong in parsing file");
    ExceptionData INVALID_ASSET_ID = new ExceptionData("INVALID_ASSET_ID", "No active asset with the passed identifier");
    ExceptionData FOUND_NON_NUMERICS = new ExceptionData("FOUND_NON_NUMERICS", "Found non numeric characters instead of digits");
    ExceptionData INVALID_STATUS = new ExceptionData("INVALID_STATUS", "Asset cannot marked into the passed status");
    ExceptionData EXCEEDS_ALLOWED_SIZE = new ExceptionData("EXCEEDS_ALLOWED_SIZE", "Asset size is higher than the allowable limit");
}
