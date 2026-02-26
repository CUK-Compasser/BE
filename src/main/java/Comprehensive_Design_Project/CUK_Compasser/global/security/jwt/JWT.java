package Comprehensive_Design_Project.CUK_Compasser.global.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JWT {
    private String grantType;
    private String accessToken;
    private String refreshToken;
}
