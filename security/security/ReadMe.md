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