package Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.service;

import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.converter.RandomBoxConverter;
import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.dto.RandomBoxCreateReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.dto.RandomBoxRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.dto.RandomBoxUpdateReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.entity.RandomBox;
import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.repository.RandomBoxRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Store;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.repository.StoreRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.repository.StoreManagerRepository;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status.ErrorStatus;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RandomBoxService {

    private final StoreRepository storeRepository;
    private final RandomBoxRepository randomBoxRepository;
    private final StoreManagerRepository storeManagerRepository;
    private final RandomBoxConverter randomBoxConverter;

    @Transactional
    public RandomBoxRespDTO create(Long storeId, Long memberId, RandomBoxCreateReqDTO req) {
        Store store = assertOwner(storeId, memberId);

        RandomBox box = randomBoxRepository.save(RandomBox.builder()
                .store(store)
                .boxName(req.getBoxName())
                .content(req.getContent())
                .stock(req.getStock())
                .beforePrice(req.getBeforePrice())
                .afterPrice(req.getAfterPrice())
                .saleStatus(req.getSaleStatus()) // ✅ enum 그대로
                .build());

        return randomBoxConverter.toResp(box);
    }

    @Transactional(readOnly = true)
    public List<RandomBoxRespDTO> list(Long storeId, Long memberId) {
        assertOwner(storeId, memberId);
        return randomBoxRepository.findAllByStore_Id(storeId)
                .stream().map(randomBoxConverter::toResp).toList();
    }

    @Transactional
    public RandomBoxRespDTO update(Long storeId, Long boxId, Long memberId, RandomBoxUpdateReqDTO req) {
        assertOwner(storeId, memberId);

        RandomBox box = randomBoxRepository.findByIdAndStore_Id(boxId, storeId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RANDOM_BOX_NOT_FOUND));

        if (req.getBoxName() != null) box.setBoxName(req.getBoxName());
        if (req.getContent() != null) box.setContent(req.getContent());
        if (req.getStock() != null) box.setStock(req.getStock());
        if (req.getBeforePrice() != null) box.setBeforePrice(req.getBeforePrice());
        if (req.getAfterPrice() != null) box.setAfterPrice(req.getAfterPrice());
        if (req.getSaleStatus() != null) box.setSaleStatus(req.getSaleStatus()); // ✅ enum

        return randomBoxConverter.toResp(box);
    }

    private Store assertOwner(Long storeId, Long memberId) {
        if (!storeManagerRepository.existsById(memberId)) {
            throw new GeneralException(ErrorStatus.STORE_MANAGER_NOT_FOUND);
        }
        return storeRepository.findByIdAndStoreManager_MemberId(storeId, memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.STORE_FORBIDDEN));
    }
}