package Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.generalStatus;

import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GeneralErrorCode implements BaseErrorCode {
    // 일반 에러 코드
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "G001", "잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "G002", "인증되지 않은 사용자입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "G003", "권한이 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "G004", "리소스를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "G005", "서버 내부 오류가 발생했습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "G006", "허용되지 않은 메서드입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "G007", "토큰을 통해 사용자를 찾을 수 없습니다.."),
    BLACKLIST_TOKEN(HttpStatus.UNAUTHORIZED, "G008", "블랙리스트에 등록된 토큰입니다."),
    RT_NOT_FOUND(HttpStatus.UNAUTHORIZED, "G008", "리프레쉬 토큰을 찾을 수 없습니다."),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "G007", "입력값 검증에 실패했습니다.");

    private final HttpStatus httpStatus;  // ex: 200 OK
    private final String code;
    private final String message;
}
