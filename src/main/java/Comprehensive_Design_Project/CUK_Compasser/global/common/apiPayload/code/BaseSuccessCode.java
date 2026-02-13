package Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code;

import org.springframework.http.HttpStatus;

public interface BaseSuccessCode {

    HttpStatus getHttpStatus();
    String getCode();
    String getMessage();
}
