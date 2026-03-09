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
            @RequestBody RandomBoxCreateReqDTO req,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return randomBoxService.create(userDetails.getMember().getId(), req);
    }

    @GetMapping
    public List<RandomBoxRespDTO> list(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return randomBoxService.list(userDetails.getMember().getId());
    }

    @PatchMapping("/{boxId}")
    public RandomBoxRespDTO update(
            @PathVariable Long boxId,
            @RequestBody RandomBoxUpdateReqDTO req,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return randomBoxService.update(boxId, userDetails.getMember().getId(), req);
    }

    @DeleteMapping("/{boxId}")
    public void delete(
            @PathVariable Long boxId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        randomBoxService.delete(boxId, userDetails.getMember().getId());
    }
}