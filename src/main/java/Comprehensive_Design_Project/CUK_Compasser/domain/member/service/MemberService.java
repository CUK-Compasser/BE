package Comprehensive_Design_Project.CUK_Compasser.domain.member.service;

import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status.ErrorStatus;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.exception.GeneralException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    @Value("aws.host")
    private String AWS_HOST;

    public byte[] generateQRCode (Long  memberId) {
        int width = 200, height = 200;

        BitMatrix encode = null;
        try {
            encode = new MultiFormatWriter().encode(AWS_HOST, BarcodeFormat.QR_CODE, width, height);
        } catch (WriterException e) {
            throw new GeneralException(ErrorStatus.QR_IMAGE_WRITE_FAILED); // 새로 에러 코드 만들기
        }

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(encode, "PNG", outputStream);
            return outputStream.toByteArray();
        } catch (IllegalArgumentException e) {
            // 가로/세로가 0 이하일 때 등
            throw new GeneralException(ErrorStatus.QR_INVALID_SIZE);
        } catch (Exception e) {
            // 기타 예상치 못한 오류
            log.error("Unexpected QR Generation Error: {}", e.getMessage());
            throw new GeneralException(ErrorStatus.QR_GENERATE_FAILED);
        }
    }
}
