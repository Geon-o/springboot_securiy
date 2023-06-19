***
##### 23.06.15

# Spring Boot Security 설정
- mysql
- JPA
- Spring Security
- Mustache
- Spring Web
- Lombok

### 내용
- 간단한 환경설정과 mustache 라는 템플릿을 사용하여 스프링 시큐리티가 어떻게 작동하는지 확인
- 서버 실행시 콘솔창에 임의의 패스워드가 나타나고 설정한 url에 접속 시 로그인창으로 변경돼 접근을 제한
- 이때 콘솔창에 나타난 패스워드를 입력하여 접근할 수 있음

***
##### 23.06.16

# 각 계정권한에 관한 설정

### 내용
- /config/SecurityConfig
  - 스프링 필터체인에 사용자마다 접근권한을 설정
  - 설정시 WebSecurityconfigurerAdapter 상속해서 진행하여야하지만 현재 deprecated되어
  - SecurityFilterChain을 빈으로 등록해서 사용해야함

***
##### 23.06.17

# 스프링 시큐리티를 활용한 회원가입

## 내용
- 서버에 user entity를 만들고 mustache를 활용하여
- 회원가입 폼과 로그인 폼 생성 후 회원가입 진행

### 패스워드 암호화
- 회원가입을 통해 넘겨받은 패스워드로 시큐리티를 통해 로그인하려고 하면 로그인이 제대로 되지 않음
- 그렇기 때문에 패스워드를 암호화를 진행해야함


- BCryptPasswordEncoder
  - 스프링 시큐리티 프레임워크에서 제공하는 클래스
  - 비밀번호를 암호화하는데 사용할 수 있는 메서드를 가진 클래스
  - BCrypt 해싱함수를 사용


***
##### 23.06.18

# 시큐리티 로그인

## 내용
- 회원가입 후 사용자 정보를 통해 스프링 시큐리티로 로그인 하는 방법
- 스프링 시큐리티 로그인 내부 동작방식 확인
- 로그인을 진행시 완료되면 스프링 시큐리티 session이 생성되고
- session에 들어갈 수 있는 object 타입은 정해져있음 -> Authenticaion 타입 객체
- Authenticaion 안에 User 정보가 있어야 함
- User 오브젝트의 타입을 UserDetails 타입 객체에 넣고 사용
- 즉 전체적인 구성을 보았을 때 다음과 같음
  - Security Session -> Authenticaion -> UserDetails

### UserDetails
- 스프링 시큐리티에서 사용자의 정보를 담는 인터페이스
- 사용자의 정보를 불러오기 위해서 구현해야할 인터페이스로 기본 오버라이드 메소드가 존재
  - getAuthorities()
    - 계정의 권한 목록을 리턴(계정 권한)
  - getPassword()
    - 계정의 비밀번호를 리턴
  - getUsername()
    - 꼐정의 고유한 값을 리턴
  - isAccountNonExpired()
    - 계정의 만료여부
  - isAccountNonLocked()
    - 계쩡의 잠김여부
  - isCredentialsNonExpired()
    - 비밀번호 만료여부
  - isEnabled()
    - 계정의 비활성화(휴먼계정)


### UserDetailsService
- 스프링 시큐리티에서 유저의 정보를 가져오는 인터페이스
- 다음과 같은 오버라이드 메소드가 존재
  - loadUserByUsername
    - 유저의 정보를 불러와서 UserDetails로 리턴
- 이 작업이 진행이 되면 다음과 같은 구조로 형성이 됨
  - 시큐리티 session(내부에 Authentication(내부에 UserDetails))
  - 따라서 유저의 정보를 확인 할 수 있는 구조가 됨
- 또한 SecurityConfig에서 설정해준 loginProcessingUrl에 설정한 URL 요청이 오면
- 자동으로 UserDetailsService 타입으로 IoC 되어있는 loadUserByUsername 함수가 실행되는 규칙을 가지고 있음


***
##### 23.06.19

# 시큐리티 권한처리

## 내용
- user 권한말고 admin과 manager 권한 처리
- deprecated가 된 메소드들이 많아 다수의 권한을 설정해야할 경우
  - hasAnyRole() 메소드로 처리
  - 작성시 ROLE_은 이제 자동을 붙여줌으로 작성하지 않고 hasAnyRole("ADMIN", "MANAGER")
  - 위와 같은 방식으로 작성해주면 된다.

### EnableGlobalMethodSecurity 어노테이션
- 현재 deprecated 된 상태
- EnableMethodSecurity를 대신 사용


- @EnableMethodSecurity(securedEnabled = true)
  - Secured 어노테이션 활성화
    - 특정 메소드에 해당 어노테이션을 걸고 권한설정을 가능하게 함
    - @Secured("ROLE_ADMIN") 이와 같이 권한설정이 가능


- @EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
  - preAuthorize, postAuthorize 어노테이션 활성화
    - 메소드가 실행되기 직전에 실행되는 어노테이션
    - 형태는 @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    - 위와 같이 설정하여 사용가능

