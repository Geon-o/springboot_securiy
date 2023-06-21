package com.jgo.security.auth;

// 시큐리티가 '/login' 주소 요청이 오면 낚아채서 로그인을 진행시킴
// 로그인을 진행이 완료면 시큐리티 session을 만들어준다.(Security ContextHolder)여기에 값을 저장함
// 시큐리티 세션에 들어갈 수 있는 object가 정해져있음
// -> Authentication 타입 객체
// Authentication 안에 User정보가 있어야 함.
// User 오브젝트의 타입 -> UserDetails 타입 객체
// Security Session -> Authentication -> UserDetails(PrincipalDetails)

import com.jgo.security.entity.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    //콤포지션
    private User user;
    private Map<String, Object> attributes;

    // 일반로그인
    public PrincipalDetails(User user) {
        this.user = user;
    }

    // OAuth로그인
    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    /**
     * 해당 User의 권한을 리턴하는 곳
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collection;
    }


    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }


    //이 밑으로는 계정의 상태를 설정하는 메소드
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        /**
         * false의 예
         * -> 사이트에서 1년동안 회원이 로그인을 안할 시 휴먼 계정으로 전환
         * 현재시간 - 로그인시간 -> 1년을 초과하면 false로 전환
         */
        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return null;
    }
}
