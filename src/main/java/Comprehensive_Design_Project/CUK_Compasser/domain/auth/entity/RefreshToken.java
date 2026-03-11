package Comprehensive_Design_Project.CUK_Compasser.domain.auth.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@RedisHash(value = "refreshToken", timeToLive = 1000 * 60 * 60)
public class RefreshToken {

    @Id
    private String userId;

    @Indexed
    private String refreshToken;
}
