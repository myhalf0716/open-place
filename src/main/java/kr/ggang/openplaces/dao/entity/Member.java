package kr.ggang.openplaces.dao.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CollectionTable;
import javax.persistence.JoinColumn;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonProperty;

import kr.ggang.openplaces.model.JoinReq;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbm_member")
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Post Entity에서 User와의 관계를 Json으로 변환시 오류 방지를 위한 코드
@Proxy(lazy = false)
public class Member implements UserDetails {
    @Id // pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberKey;
    @Column(nullable = false, unique = true, length = 80)
    private String memberId;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(length = 200)
    private String password;
    @Column(nullable = false, length = 200)
    private String name;
    @Column(length = 200)
    private String email;
    @Column
    private LocalDateTime createdAt;

    @ElementCollection
    @CollectionTable(
        name = "tbm_role",
        joinColumns = @JoinColumn(name = "memberKey")
    )
    @Column(name = "role")
    private List<String> roles = new ArrayList<>();
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public String getUsername() {
        return this.memberId;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isEnabled() {
        return true;
    }

    public static Member fromJoinReq(JoinReq joinReq) {
        return Member.builder()
            .memberId(joinReq.getMemberId())
            .name(joinReq.getName())
            .email(joinReq.getEmail())
            .build();
    }
}
