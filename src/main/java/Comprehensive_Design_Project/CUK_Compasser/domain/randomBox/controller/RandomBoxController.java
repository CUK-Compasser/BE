package Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.controller;

import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.dto.RandomBoxCreateReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.dto.RandomBoxRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.dto.RandomBoxUpdateReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.service.RandomBoxService;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.ApiResponse;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status.SuccessStatus;
import Comprehensive_Design_Project.CUK_Compasser.global.security.userDetails.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores/{storeId}/random-box")
@Tag(name = "RandomBox", description = "랜덤박스 관리 API")
public class RandomBoxController {

    private final RandomBoxService randomBoxService;

    @Operation(
            summary = "랜덤박스 등록",
            description = "로그인한 점장이 자신의 매장에 랜덤박스를 등록합니다."
    )
    @PostMapping
    public ApiResponse<RandomBoxRespDTO> create(
            @RequestBody RandomBoxCreateReqDTO req,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ApiResponse.onSuccess(
                SuccessStatus.OK,
                randomBoxService.create(userDetails.getMember().getId(), req)
        );
    }

    @Operation(
            summary = "랜덤박스 목록 조회",
            description = "로그인한 점장의 매장에 등록된 랜덤박스 목록을 조회합니다."
    )
    @GetMapping
    public ApiResponse<List<RandomBoxRespDTO>> list(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ApiResponse.onSuccess(
                SuccessStatus.OK,
                randomBoxService.list(userDetails.getMember().getId())
        );
    }

    @Operation(
            summary = "랜덤박스 수정",
            description = "로그인한 점장이 자신의 매장 랜덤박스 정보를 수정합니다.<br>" +
                    "픽업시간 형식은 00:00 ~ 23:59 입니다!<br>" +
                    "예시: {\\\"timezone\\\":\\\"Asia/Seoul\\\",\\\"pickupTime\\\":{\\\"start\\\":\\\"18:00\\\",\\\"end\\\":\\\"21:00\\\"}}"

    )
    @PatchMapping("/{boxId}")
    public ApiResponse<RandomBoxRespDTO> update(
            @PathVariable Long boxId,
            @RequestBody RandomBoxUpdateReqDTO req,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ApiResponse.onSuccess(
                SuccessStatus.OK,
                randomBoxService.update(boxId, userDetails.getMember().getId(), req)
        );
    }

    @Operation(
            summary = "랜덤박스 삭제",
            description = "로그인한 점장이 자신의 매장 랜덤박스를 삭제합니다."
    )
    @DeleteMapping("/{boxId}")
    public ApiResponse<Void> delete(
            @PathVariable Long boxId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMember().getId();
        randomBoxService.delete(boxId, memberId);
        return ApiResponse.onSuccess(SuccessStatus.OK, null);
    }
}