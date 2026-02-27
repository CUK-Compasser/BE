package Comprehensive_Design_Project.CUK_Compasser.domain.store.controller;

import Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.StoreResponse;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.StoreUpdateRequest;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.service.StoreService;
import Comprehensive_Design_Project.CUK_Compasser.global.security.userDetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores")
public class StoreController {

    private final StoreService storeService;

    /**
     * 점장 가게 수정 (운영시간 raw JSON 포함)
     * PATCH /stores/{storeId}
     */
    @PatchMapping("/{storeId}")
    public StoreResponse updateStore(
            @PathVariable Long storeId,
            @RequestBody StoreUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMember().getId();
        return storeService.updateStore(storeId, memberId, request);
    }

    /**
     * (선택) 내 가게 조회 - 운영시간 확인용
     * GET /stores/{storeId}
     * {
     *   "businessHours": {
     *     "timezone": "Asia/Seoul",
     *     "weekly": {
     *       "MON": { "bussiness-time" : {"open": "20:00", "close": "22:00"}, "break-time": {"start": "20:00", "end": "22:00"}},
     *       "TUE": { "open": "09:00", "close": "21:00", "closed": false, "breaks-start": "19:00", "breaks-end": "21:00", "breaks": true },
     *       "WED": { "open": "09:00", "close": "21:00", "closed": false, "breaks-start": "19:00", "breaks-end": "21:00", "breaks": true },
     *       "THU": { "open": "09:00", "close": "21:00", "closed": false, "breaks-start": "19:00", "breaks-end": "21:00", "breaks": true },
     *       "FRI": { "open": "09:00", "close": "22:00", "closed": false, "breaks-start": "19:00", "breaks-end": "21:00", "breaks": true },
     *       "SAT": { "open": "10:00", "close": "22:00", "closed": false, "breaks-start": "19:00", "breaks-end": "21:00", "breaks": true },
     *       "SUN": { "open": null, "close": null, "closed": true, "breaks-start": "null", "breaks-end": "null", "break-time": null }
     *     }
     *   }
     * }
     */
    @GetMapping("/{storeId}")
    public StoreResponse getMyStore(
            @PathVariable Long storeId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMember().getId();
        return storeService.getMyStore(storeId, memberId);
    }
}