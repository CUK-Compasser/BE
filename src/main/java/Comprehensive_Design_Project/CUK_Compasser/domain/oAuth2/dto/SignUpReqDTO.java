package Comprehensive_Design_Project.CUK_Compasser.domain.oAuth2.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SignUpReqDTO {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class JoinReqDTO {
        @NotBlank(message = "이름은 필수 입력 항목입니다.")
        private String memberName;

        @NotBlank(message = "닉네임은 필수 입력 항목입니다.")
        private String nickname;

        @NotBlank(message = "이메일은 필수 입력 항목입니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        private String email;

        @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
        // @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
        private String password;

        @NotBlank(message = "비밀번호 확인은 필수 입력 항목입니다.")
        private String passwordConfirm;
    }
}
