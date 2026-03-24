package Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.repository;

import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.entity.RandomBox;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface RandomBoxRepository extends JpaRepository<RandomBox, Long> {
    List<RandomBox> findAllByStore_Id(Long storeId);
    Optional<RandomBox> findByIdAndStore_Id(Long boxId, Long storeId);

    //트랜젝션 락킹을 위한 락 조회 메서드
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<RandomBox> findWithLockById(Long id);
}