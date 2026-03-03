package Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.repository;

import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.entity.RandomBox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RandomBoxRepository extends JpaRepository<RandomBox, Long> {
    List<RandomBox> findAllByStore_Id(Long storeId);
    Optional<RandomBox> findByIdAndStore_Id(Long boxId, Long storeId);
}