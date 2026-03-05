package Comprehensive_Design_Project.CUK_Compasser.domain.member.entity;

import Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.entity.StoreManager;
import Comprehensive_Design_Project.CUK_Compasser.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "members",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_members_email", columnNames = {"email"}),
                @UniqueConstraint(name = "uk_members_provider_pid", columnNames = {"provider", "provider_id"})
        },
        indexes = {
                @Index(name = "idx_members_role", columnList = "role"),
                @Index(name = "idx_members_status", columnList = "status")
        }
)
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    private String email;

    @Column(length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Login provider; // LOCAL, KAKAO

    @Column(name = "provider_id", length = 128)
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MemberRole role; // NORMAL, STORE_MANAGER

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Role status; // ACTIVE, BLOCKED, DELETED

    @Column(name = "member_name", length = 50)
    private String memberName;

    @Column(length = 20)
    private String phone;

    /** 점장 승격 시 생성되는 1:1 */
    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private StoreManager storeManager;

    public static Member createNewMemberByKakao(String email, String nickname) {
        // phone은 어떻게 할까?
        return Member.builder().email(email).status(Role.ACTIVE).role(MemberRole.NULL).provider(Login.KAKAO).providerId("id").memberName(nickname).build();
    }

    // 편의 메서드(선택)
    public void promoteToStoreManager(StoreManager manager) {
        this.role = MemberRole.STORE_MANAGER;
        this.storeManager = manager;
        manager.setMember(this);
    }
}
