package Comprehensive_Design_Project.CUK_Compasser.domain.auth.repository;

import Comprehensive_Design_Project.CUK_Compasser.domain.auth.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
