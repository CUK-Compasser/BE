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

    // =========================
    // [Store]
    // =========================
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "S001", "가게를 찾을 수 없습니다."),
    STORE_FORBIDDEN(HttpStatus.FORBIDDEN, "S002", "가게 수정/조회 권한이 없습니다."),
    INVALID_LOCATION(HttpStatus.BAD_REQUEST, "S003", "위치 정보(위도/경도)가 올바르지 않습니다."),
    STORE_BUSINESS_HOURS_SERIALIZE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S020", "영업시간 JSON 직렬화에 실패했습니다."),
    STORE_BUSINESS_HOURS_PARSE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S021", "영업시간 JSON 파싱에 실패했습니다."),

    // =========================
    // [Store Location]
    // =========================
    STORE_ADDRESS_NOT_FOUND(HttpStatus.BAD_REQUEST, "SL001", "유효한 주소를 찾을 수 없습니다."),
    STORE_LOCATION_CONVERT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S003", "주소 좌표 변환에 실패했습니다."),

    // =========================
    // [Store Image]
    // =========================
    STORE_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "SI001", "가게 이미지를 찾을 수 없습니다."),

    // =========================
    // [StoreManager]
    // =========================
    STORE_MANAGER_NOT_FOUND(HttpStatus.FORBIDDEN, "SM001", "점장 계정이 아닙니다."),

    // =========================
    // [BusinessHours]
    // =========================
    BUSINESS_HOURS_INVALID(HttpStatus.BAD_REQUEST, "BH001", "영업시간 형식이 올바르지 않습니다."),

    // =========================
    // [RandomBox]
    // =========================
    INVALID_RANDOM_BOX_NAME(HttpStatus.BAD_REQUEST, "RB001", "랜덤박스 이름이 올바르지 않습니다."),
    INVALID_RANDOM_BOX_STOCK(HttpStatus.BAD_REQUEST, "RB002", "랜덤박스 수량이 올바르지 않습니다."),
    INVALID_RANDOM_BOX_PRICE(HttpStatus.BAD_REQUEST, "RB003", "랜덤박스 가격이 올바르지 않습니다."),
    INVALID_RANDOM_BOX_BUY_LIMIT(HttpStatus.BAD_REQUEST, "RB004", "구매 제한 개수가 올바르지 않습니다."),
    INVALID_RANDOM_BOX_SALE_STATUS(HttpStatus.BAD_REQUEST, "RB005", "랜덤박스 판매 상태가 올바르지 않습니다."),
    RANDOM_BOX_NOT_FOUND(HttpStatus.NOT_FOUND, "RB006", "랜덤박스를 찾을 수 없습니다."),
    RANDOM_BOX_NOT_ON_SALE(HttpStatus.BAD_REQUEST, "RB007", "판매 중인 랜덤박스가 아닙니다."),
    RANDOM_BOX_STOCK_NOT_ENOUGH(HttpStatus.BAD_REQUEST, "RB008", "랜덤박스 재고가 부족합니다."),
    RANDOM_BOX_BUY_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "RB009", "구매 가능 수량을 초과했습니다."),

    // =========================
    // [QRCode]
    // =========================
    QR_IMAGE_WRITE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"RW001","이미지 파일로 변환하는 중 오류가 발생했습니다."),
    QR_INVALID_SIZE(HttpStatus.INTERNAL_SERVER_ERROR, "RW003","QR 코드의 크기(가로/세로) 설정이 잘못되었습니다."),
    QR_GENERATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"RW004","QR 코드 생성 중 알 수 없는 오류가 발생했습니다."),
    QR_EXPIRED(HttpStatus.BAD_REQUEST,"RW005","만료된 QR 코드 입니다."),

    // =========================
    // [Reward]
    // =========================
    REWARD_NOT_FOUND(HttpStatus.BAD_REQUEST,"RW001","적립을 찾을 수 없습니다"),

    // =========================
    // [BusinessNum]
    // =========================
    BUSINESS_LICENSE_REQUIRED(HttpStatus.BAD_REQUEST, "O001", "사업자등록번호를 입력해주세요."),
    BUSINESS_LICENSE_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "O002", "사업자등록번호 형식이 올바르지 않습니다."),
    BUSINESS_LICENSE_NOT_REGISTERED(HttpStatus.BAD_REQUEST, "O003", "사업자등록번호가 등록되지 않았습니다."),
    BUSINESS_LICENSE_NOT_VERIFIED(HttpStatus.BAD_REQUEST, "O004", "사업자등록번호 검증이 완료되지 않았습니다."),
    BUSINESS_LICENSE_VERIFY_FAILED(HttpStatus.BAD_REQUEST, "O005", "사업자등록번호 검증에 실패했습니다."),

    // =========================
    // [Reservation]
    // =========================
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "R001", "예약을 찾을 수 없습니다."),
    RESERVATION_ALREADY_PROCESSED(HttpStatus.BAD_REQUEST, "R002", "이미 처리된 예약입니다."),
    REJECT_REASON_REQUIRED(HttpStatus.BAD_REQUEST, "R003", "거절 사유는 필수입니다."),
    RESERVATION_STATUS_REQUIRED(HttpStatus.BAD_REQUEST, "R004", "예약 상태값은 필수입니다."),
    INVALID_RESERVATION_STATUS(HttpStatus.BAD_REQUEST, "R005", "변경할 수 없는 예약 상태입니다."),
    RESERVATION_ALREADY_APPROVED(HttpStatus.BAD_REQUEST, "R006", "이미 승인된 예약입니다."),
    RESERVATION_ALREADY_REJECTED(HttpStatus.BAD_REQUEST, "R007", "이미 거절된 예약입니다."),


    // =========================
    // [Order]
    // =========================
    INVALID_ORDER_QUANTITY(HttpStatus.BAD_REQUEST, "O001", "주문 수량이 올바르지 않습니다."),
    ORDER_ALREADY_PAID(HttpStatus.BAD_REQUEST, "O002", "이미 송금 완료 처리된 주문입니다."),
    ORDER_CANCEL_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "O003", "현재 상태의 주문은 취소할 수 없습니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}