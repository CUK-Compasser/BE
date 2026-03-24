package Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status;

import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.BaseSuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessStatus implements BaseSuccessCode {
    OK(HttpStatus.OK, "S000", "요청이 성공했습니다."),
    CREATED(HttpStatus.CREATED, "S001", "생성되었습니다."),

    // =========================
    // [Order]
    // =========================
    ORDER_CREATED(HttpStatus.CREATED, "O2001", "주문 생성에 성공했습니다."),
    ORDER_COMPLETED(HttpStatus.OK, "O2002", "결제 진행 정보 저장에 성공했습니다."),
    ORDER_STATUS_FOUND(HttpStatus.OK, "O2003", "주문 상태 조회에 성공했습니다."),
    ORDER_CANCELED(HttpStatus.OK, "ORDER2004", "주문 취소에 성공했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
