package Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.handler;

import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.ApiResponse;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.BaseErrorCode;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.generalStatus.GeneralErrorCode;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status.ErrorStatus;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.exception.GeneralException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.security.auth.message.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * GeneralException 처리 (프로젝트 공통 예외)
     */
    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneralException(GeneralException e) {
        BaseErrorCode errorCode = e.getCode();

        ApiResponse<Void> response = ApiResponse.onFailure(errorCode);

        // ErrorCode의 HttpStatus 사용
        HttpStatus status = errorCode.getHttpStatus();

        return ResponseEntity.status(status).body(response);
    }

    /**
     * Validation 예외 처리 (@Valid 검증 실패)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(
            MethodArgumentNotValidException e) {

        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ApiResponse<Map<String, String>> response = ApiResponse.onFailure(
                GeneralErrorCode.VALIDATION_ERROR,
                errors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * EntityNotFoundException 처리 (JPA 엔티티를 찾을 수 없는 경우)
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleEntityNotFoundException(EntityNotFoundException e) {
        log.error("EntityNotFoundException: {}", e.getMessage());

        ApiResponse<String> response = ApiResponse.onFailure(
                GeneralErrorCode.NOT_FOUND,
                e.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * 기타 모든 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGeneralException(Exception e) {
        ApiResponse<String> response = ApiResponse.onFailure(
                GeneralErrorCode.INTERNAL_SERVER_ERROR,
                e.getMessage()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }



    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ApiResponse<?> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        // @RequestParam YearMonth month 변환 실패 케이스
        if ("month".equals(e.getName())) {
            return ApiResponse.onFailure(ErrorStatus.UNSUPPORTED_CALENDAR_FORMAT);
        }
        // 나머지는 프로젝트 정책대로 (원하면 더 세분화 가능)
        return ApiResponse.onFailure(GeneralErrorCode.BAD_REQUEST);
    }
}
