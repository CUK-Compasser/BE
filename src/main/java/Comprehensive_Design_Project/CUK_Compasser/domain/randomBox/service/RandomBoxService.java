package Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.service;

import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.converter.RandomBoxConverter;
import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.dto.RandomBoxCreateReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.dto.RandomBoxRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.dto.RandomBoxUpdateReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.entity.RandomBox;
import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.entity.SaleStatus;
import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.repository.RandomBoxRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Store;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.repository.StoreRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.repository.StoreManagerRepository;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status.ErrorStatus;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.exception.GeneralException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RandomBoxService {

    private final StoreRepository storeRepository;
    private final RandomBoxRepository randomBoxRepository;
    private final StoreManagerRepository storeManagerRepository;
    private final RandomBoxConverter randomBoxConverter;

    /**
     * 랜덤박스 생성
     * - 점장 본인의 스토어에 랜덤박스를 등록한다.
     * - 픽업 시간 JSON은 JsonNode로 받은 뒤 문자열로 변환해 저장한다.
     */
    @Transactional
    public RandomBoxRespDTO create(Long memberId, RandomBoxCreateReqDTO req) {
        validateCreateReq(req);
        Store store = getMyStoreEntity(memberId);

        RandomBox box = randomBoxRepository.save(RandomBox.builder()
                .store(store)
                .boxName(req.getBoxName())
                .content(req.getContent())
                .stock(req.getStock())
                .price(req.getPrice())
                .buyLimit(req.getBuyLimit())
                .saleStatus(SaleStatus.READY)
                .pickupTimeInfo(req.getPickupTimeInfo().toString())
                .build());

        return randomBoxConverter.toResp(box);
    }

    /**
     * 랜덤박스 수정
     * - 점장 본인 스토어의 랜덤박스만 수정 가능하다.
     * - 요청에 들어온 값만 부분 수정한다.
     */
    @Transactional
    public RandomBoxRespDTO update(Long boxId, Long memberId, RandomBoxUpdateReqDTO req) {
        Store store = getMyStoreEntity(memberId);

        RandomBox box = randomBoxRepository.findByIdAndStore_Id(boxId, store.getId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.RANDOM_BOX_NOT_FOUND));

        validateUpdateReq(req, box);

        return randomBoxConverter.toResp(box);
    }

    /**
     * 랜덤박스 삭제
     * - 점장 본인 스토어의 랜덤박스만 삭제 가능하다.
     */
    @Transactional
    public void delete(Long boxId, Long memberId) {
        Store store = getMyStoreEntity(memberId);

        RandomBox box = randomBoxRepository.findByIdAndStore_Id(boxId, store.getId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.RANDOM_BOX_NOT_FOUND));

        randomBoxRepository.delete(box);
    }

    /**
     * 랜덤박스 목록 조회
     * - 점장 본인 스토어에 등록된 랜덤박스 전체를 조회한다.
     */
    @Transactional(readOnly = true)
    public List<RandomBoxRespDTO> list(Long memberId) {
        Store store = getMyStoreEntity(memberId);

        return randomBoxRepository.findAllByStore_Id(store.getId())
                .stream()
                .map(randomBoxConverter::toResp)
                .toList();
    }

    /**
     * 점장 본인 스토어 엔티티 조회
     * - 점장 회원 여부 확인 후 본인 스토어를 조회한다.
     */
    private Store getMyStoreEntity(Long memberId) {
        if (!storeManagerRepository.existsById(memberId)) {
            throw new GeneralException(ErrorStatus.STORE_MANAGER_NOT_FOUND);
        }

        return storeRepository.findByStoreManager_MemberId(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.STORE_NOT_FOUND));
    }

    /**
     * 생성 요청값 검증
     * - 필수값, 수량/가격 제한, 픽업시간 JSON 형식을 검증한다.
     */
    private void validateCreateReq(RandomBoxCreateReqDTO req) {
        if (req.getBoxName() == null || req.getBoxName().isBlank()) {
            throw new GeneralException(ErrorStatus.INVALID_RANDOM_BOX_NAME);
        }

        if (req.getStock() == null || req.getStock() < 1) {
            throw new GeneralException(ErrorStatus.INVALID_RANDOM_BOX_STOCK);
        }

        if (req.getPrice() == null || req.getPrice() < 1) {
            throw new GeneralException(ErrorStatus.INVALID_RANDOM_BOX_PRICE);
        }

        if (req.getBuyLimit() == null || req.getBuyLimit() < 1) {
            throw new GeneralException(ErrorStatus.INVALID_RANDOM_BOX_BUY_LIMIT);
        }

        validatePickupTimeInfo(req.getPickupTimeInfo());
    }

    /**
     * 수정 요청값 검증 및 반영
     * - null이 아닌 값만 엔티티에 반영한다.
     * - pickupTimeInfo가 들어오면 JSON 형식 검증 후 문자열로 저장한다.
     */
    private void validateUpdateReq(RandomBoxUpdateReqDTO req, RandomBox box) {
        if (req.getBoxName() != null) {
            if (req.getBoxName().isBlank()) {
                throw new GeneralException(ErrorStatus.INVALID_RANDOM_BOX_NAME);
            }
            box.setBoxName(req.getBoxName());
        }

        if (req.getContent() != null) {
            box.setContent(req.getContent());
        }

        if (req.getStock() != null) {
            if (req.getStock() < 1) {
                throw new GeneralException(ErrorStatus.INVALID_RANDOM_BOX_STOCK);
            }
            box.setStock(req.getStock());
        }

        if (req.getPrice() != null) {
            if (req.getPrice() < 1) {
                throw new GeneralException(ErrorStatus.INVALID_RANDOM_BOX_PRICE);
            }
            box.setPrice(req.getPrice());
        }

        if (req.getBuyLimit() != null) {
            if (req.getBuyLimit() < 1) {
                throw new GeneralException(ErrorStatus.INVALID_RANDOM_BOX_BUY_LIMIT);
            }
            box.setBuyLimit(req.getBuyLimit());
        }

        if (req.getSaleStatus() != null) {
            box.setSaleStatus(req.getSaleStatus());
        }

        if (req.getPickupTimeInfo() != null) {
            validatePickupTimeInfo(req.getPickupTimeInfo());
            box.setPickupTimeInfo(req.getPickupTimeInfo().toString());
        }
    }

    /**
     * 픽업시간 JSON 검증
     * 기대 형식:
     * {
     *   "timezone": "Asia/Seoul",
     *   "pickupTime": {
     *     "start": "09:00",
     *     "end": "18:00"
     *   }
     * }
     */
    private void validatePickupTimeInfo(JsonNode pickupTimeInfo) {
        if (pickupTimeInfo == null || pickupTimeInfo.isNull()) {
            throw new GeneralException(ErrorStatus.INVALID_PICKUP_TIME);
        }

        JsonNode timezoneNode = pickupTimeInfo.get("timezone");
        JsonNode pickupTimeNode = pickupTimeInfo.get("pickupTime");

        if (timezoneNode == null || timezoneNode.asText().isBlank()) {
            throw new GeneralException(ErrorStatus.INVALID_PICKUP_TIME);
        }

        if (pickupTimeNode == null || pickupTimeNode.isNull()) {
            throw new GeneralException(ErrorStatus.INVALID_PICKUP_TIME);
        }

        JsonNode startNode = pickupTimeNode.get("start");
        JsonNode endNode = pickupTimeNode.get("end");

        if (startNode == null || startNode.asText().isBlank()) {
            throw new GeneralException(ErrorStatus.INVALID_PICKUP_TIME);
        }

        if (endNode == null || endNode.asText().isBlank()) {
            throw new GeneralException(ErrorStatus.INVALID_PICKUP_TIME);
        }

        try {
            LocalTime startTime = LocalTime.parse(startNode.asText());
            LocalTime endTime = LocalTime.parse(endNode.asText());
            boolean isOvernightEnd = endTime.equals(LocalTime.MIDNIGHT);
            if (!isOvernightEnd && !startTime.isBefore(endTime)) {
                throw new GeneralException(ErrorStatus.INVALID_PICKUP_TIME);
            }
        } catch (DateTimeParseException e) {
            throw new GeneralException(ErrorStatus.INVALID_PICKUP_TIME);
        }
    }
}