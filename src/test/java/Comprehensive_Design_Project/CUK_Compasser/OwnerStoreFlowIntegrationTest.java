package Comprehensive_Design_Project.CUK_Compasser;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.Login;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.Member;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.MemberRole;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.Role;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.repository.MemberRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.entity.SaleStatus;
import Comprehensive_Design_Project.CUK_Compasser.global.security.userDetails.CustomUserDetails;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // ✅ JWT/필터 비활성화
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class OwnerStoreFlowIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired MemberRepository memberRepository;

    private Long memberId;

    @BeforeEach
    void setUp() {
        // ✅ 로그인 없이도 테스트 할 수 있도록 "멤버"를 DB에 미리 생성
        Member m = Member.builder()
                .email("owner@test.com")
                .password("encoded")
                .memberName("점장테스터")
                .provider(Login.NORMAL)   // ✅ 이거 추가
                .role(MemberRole.NORMAL)
                .status(Role.ACTIVE)       // (status도 not null이면 같이)
                .build();
        memberRepository.save(m);
        memberId = m.getId();
    }

    /** 요청마다 @AuthenticationPrincipal CustomUserDetails를 주입하기 위한 PostProcessor */
    private RequestPostProcessor withMemberPrincipal(Long memberId) {
        return request -> {
            Member member = memberRepository.findById(memberId).orElseThrow();
            CustomUserDetails principal = new CustomUserDetails(member);

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(auth);
            return request;
        };
    }

    @Test
    void owner_full_flow_test() throws Exception {

        // 1) 점장 승격 (자동 생성)
        String upgradeResp = mockMvc.perform(
                        patch("/owners/upgrade")
                                .with(withMemberPrincipal(memberId))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(memberId))
                .andExpect(jsonPath("$.role").value("STORE_MANAGER"))
                .andExpect(jsonPath("$.storeId").isNumber())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        JsonNode upgradeJson = objectMapper.readTree(upgradeResp);
        Long storeId = upgradeJson.get("storeId").asLong();

        // 2) 가게 PATCH (영업시간 raw JSON 포함)
        String patchBody = """
                {
                  "storeName": "컴패서 카페",
                  "storeDetails": "랜덤박스 판매합니다",
                  "beforePrice": 12000,
                  "afterPrice": 5900,
                  "businessHours": {
                    "timezone": "Asia/Seoul",
                    "weekly": {
                      "MON": { "open": "09:00", "close": "21:00", "closed": false, "breaks": [] },
                      "SUN": { "open": null, "close": null, "closed": true, "breaks": [] }
                    }
                  }
                }
                """;

        mockMvc.perform(
                        patch("/stores/{storeId}", storeId)
                                .with(withMemberPrincipal(memberId))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(patchBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storeId").value(storeId))
                .andExpect(jsonPath("$.storeName").value("컴패서 카페"))
                .andExpect(jsonPath("$.beforePrice").value(12000))
                .andExpect(jsonPath("$.businessHours.weekly.MON.open").value("09:00"));

        // 3) 위치 PATCH
        String locBody = """
                { "locationType":"CUSTOM", "latitude": 37.4851234, "longitude": 126.9876543 }
                """;

        mockMvc.perform(
                        patch("/stores/{storeId}/location", storeId)
                                .with(withMemberPrincipal(memberId))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(locBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.latitude").value(37.4851234))
                .andExpect(jsonPath("$.longitude").value(126.9876543));

        // 4) 기본 이미지 존재 확인 (점장 승격 시 기본 이미지 생성했다면 1개 이상)
        mockMvc.perform(
                        get("/stores/{storeId}/images", storeId)
                                .with(withMemberPrincipal(memberId))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storeId").value(storeId))
                .andExpect(jsonPath("$.images").isArray());

        // 5) (선택) 이미지 업로드 테스트 - multipart
        // 실제 서비스에서 URL을 "uploaded://filename" 같은 방식으로 저장하도록 해두었다면 아래로 검증 가능
        MockMultipartFile file = new MockMultipartFile(
                "storeImage", "test.png", "image/png", "fake".getBytes(StandardCharsets.UTF_8)
        );

        mockMvc.perform(
                        multipart("/stores/{storeId}/images", storeId)
                                .file(file)
                                .with(withMemberPrincipal(memberId))
                                .with(req -> { req.setMethod("PATCH"); return req; }) // multipart PATCH 트릭
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.images").isArray());

        // 6) 랜덤박스 생성
        String boxCreateBody = """
                {
                  "boxName": "랜덤박스 A",
                  "content": "오늘 남은 빵 랜덤 구성",
                  "stock": 10,
                  "beforePrice": 12000,
                  "afterPrice": 5900,
                  "saleStatus": "READY"
                }
                """;

        String createBoxResp = mockMvc.perform(
                        post("/stores/{storeId}/random-box", storeId)
                                .with(withMemberPrincipal(memberId))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(boxCreateBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storeId").value(storeId))
                .andExpect(jsonPath("$.saleStatus").value(SaleStatus.READY.name()))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        Long boxId = objectMapper.readTree(createBoxResp).get("boxId").asLong();
        assertThat(boxId).isNotNull();

        // 7) 랜덤박스 수정 (saleStatus 변경)
        String boxUpdateBody = """
                { "stock": 0, "saleStatus": "SOLD_OUT" }
                """;

        mockMvc.perform(
                        patch("/stores/{storeId}/random-box/{boxId}", storeId, boxId)
                                .with(withMemberPrincipal(memberId))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(boxUpdateBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.boxId").value(boxId))
                .andExpect(jsonPath("$.stock").value(0))
                .andExpect(jsonPath("$.saleStatus").value(SaleStatus.SOLD_OUT.name()));

        // 8) 랜덤박스 리스트 조회
        mockMvc.perform(
                        get("/stores/{storeId}/random-box", storeId)
                                .with(withMemberPrincipal(memberId))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].boxId").value(boxId));
    }
}