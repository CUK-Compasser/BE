package Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.controller;

import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.dto.RandomBoxCreateReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.dto.RandomBoxRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.dto.RandomBoxUpdateReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.service.RandomBoxService;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.ApiResponse;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status.SuccessStatus;
import Comprehensive_Design_Project.CUK_Compasser.global.security.userDetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores/{storeId}/random-box")
public class RandomBoxController {

    private final RandomBoxService randomBoxService;

    @PostMapping
    public ApiResponse<RandomBoxRespDTO> create(
            @RequestBody RandomBoxCreateReqDTO req,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ApiResponse.onSuccess(SuccessStatus.OK, randomBoxService.create(userDetails.getMember().getId(), req));
    }

    @GetMapping
    public ApiResponse<List<RandomBoxRespDTO>> list(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ApiResponse.onSuccess(SuccessStatus.OK, randomBoxService.list(userDetails.getMember().getId()));
    }

    @PatchMapping("/{boxId}")
    public ApiResponse<RandomBoxRespDTO> update(
            @PathVariable Long boxId,
            @RequestBody RandomBoxUpdateReqDTO req,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ApiResponse.onSuccess(SuccessStatus.OK, randomBoxService.update(boxId, userDetails.getMember().getId(), req));
    }

    @DeleteMapping("/{boxId}")
    public ApiResponse<Void> delete(
            @PathVariable Long boxId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ApiResponse.onSuccess(SuccessStatus.OK, null);
    }
}