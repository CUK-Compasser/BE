package Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.controller;

import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.dto.RandomBoxCreateReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.dto.RandomBoxRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.dto.RandomBoxUpdateReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.service.RandomBoxService;
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
    public RandomBoxRespDTO create(
            @PathVariable Long storeId,
            @RequestBody RandomBoxCreateReqDTO req,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMember().getId();
        return randomBoxService.create(storeId, memberId, req);
    }

    @GetMapping
    public List<RandomBoxRespDTO> list(
            @PathVariable Long storeId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMember().getId();
        return randomBoxService.list(storeId, memberId);
    }

    @PatchMapping("/{boxId}")
    public RandomBoxRespDTO update(
            @PathVariable Long storeId,
            @PathVariable Long boxId,
            @RequestBody RandomBoxUpdateReqDTO req,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMember().getId();
        return randomBoxService.update(storeId, boxId, memberId, req);
    }
}