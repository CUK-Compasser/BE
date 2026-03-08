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
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private String AWS_HOST;

    public byte[] generateQRCode (Long  memberId) {
        int width = 200, height = 200;

        BitMatrix encode = null;
        try {
            encode = new MultiFormatWriter().encode(AWS_HOST, BarcodeFormat.QR_CODE, width, height);
        } catch (WriterException e) {
            throw new GeneralException(ErrorStatus.UNSUPPORTED_IMAGE_TYPE); // 새로 에러 코드 만들기
        }

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(encode, "PNG", outputStream);
            return outputStream.toByteArray();
        }
        catch (Exception e){
            log.warn("QR Code Exceptions {}", e.getMessage()); // 새로 에러 코드 만들기.
        }

        throw new GeneralException(ErrorStatus.USER_NOT_FOUND);
    }
}
