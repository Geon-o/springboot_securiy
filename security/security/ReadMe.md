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
<<<<<<< HEAD
  ![image](https://github.com/Geon-o/springboot_securiy/assets/91590599/581bab23-b3a5-464c-8092-4e983bc85edd)

=======

***
##### 23.06.16

# 각 계정권한에 관한 설정

### 내용
- /config/SecurityConfig
  - 스프링 필터체인에 사용자마다 접근권한을 설정
  - 설정시 WebSecurityconfigurerAdapter 상속해서 진행하여야하지만 현재 deprecated되어
  - SecurityFilterChain을 빈으로 등록해서 사용해야함
>>>>>>> f1bcc6e ([security] 계정 권한설정)
