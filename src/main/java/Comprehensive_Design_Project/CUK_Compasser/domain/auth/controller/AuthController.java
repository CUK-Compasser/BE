package Comprehensive_Design_Project.CUK_Compasser.domain.auth.controller;

import Comprehensive_Design_Project.CUK_Compasser.domain.auth.dto.LoginReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.auth.dto.TokenRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenRespDTO> login(@Valid @RequestBody LoginReqDTO request) {
        TokenRespDTO response = authService.login(request);
        return ResponseEntity.ok(response);
    }

}
