package Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status;

import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // =========================
    // [User / Profile]
    // =========================
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "유저 없음"),
    NICKNAME_EMPTY(HttpStatus.BAD_REQUEST, "U002", "닉네임은 필수입니다."),
    NICKNAME_TOO_LONG(HttpStatus.BAD_REQUEST, "U003", "닉네임은 50자 이하여야 합니다."),
    SELF_FOLLOW_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "U004", "자기 자신을 팔로우/언팔로우할 수 없습니다."),

    // =========================
    // [File / Upload]
    // =========================
    FILE_REQUIRED(HttpStatus.BAD_REQUEST, "F001", "파일이 필요합니다."),
    FILE_TOO_LARGE(HttpStatus.PAYLOAD_TOO_LARGE, "F002", "파일 용량이 너무 큽니다."),
    UNSUPPORTED_IMAGE_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "F003", "지원하는 파일 양식이 아닙니다."),
    FILE_UPLOAD_FAILED(HttpStatus.EXPECTATION_FAILED, "F004", "파일업로드에 실패했습니다."),

    // Store
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "S001", "가게를 찾을 수 없습니다."),
    STORE_FORBIDDEN(HttpStatus.FORBIDDEN, "S002", "가게 수정/조회 권한이 없습니다."),

    // StoreManager
    STORE_MANAGER_NOT_FOUND(HttpStatus.FORBIDDEN, "SM001", "점장 계정이 아닙니다."),

    // BusinessHours
    BUSINESS_HOURS_INVALID(HttpStatus.BAD_REQUEST, "BH001", "영업시간 형식이 올바르지 않습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
