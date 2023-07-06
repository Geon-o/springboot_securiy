***
##### 23.07.04

# JWT 서버 프로젝트 설정
- Lombok
- Spring security
- Spring Web
- Spring JPA
- MySQL
- Java JWT

***
##### 23.07.06

# jwt를 위한 security 설정
```java
httpSecurity.csrf(csrf -> csrf.disable());
        httpSecurity.sessionManagement(sm -> sm
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))//세션을 사용하지 않겠다는 로직
                .formLogin(fl -> fl.disable())
```
- jwt 사용시 기본적인 로직형태
- 로그인 폼을 사용하지 않아 페이지 접근시 로그인을 하지 않도록 함
- 또한 세션을 사용하지 않음
- @CrossOrigin
  - 인증이 필요 없을 시 사용하는 어노테이션
  - 하지만 인증이 필요하다면 사용하지 않고 필터에 등록하여 사용


# jwt Bearer 인증방식


1. header에 데이터를 담아 전송
   - authorization에 인증이 가능한 정보(id, pw)를 담아 전송
   - 이와 같은 방식이 http basic 방식
   - 하지만 중간에 해당 데이터가 노출될 가능성이 높음 -> 그리하여 https를 사용

2. header에 토큰을 담아 전송
   - authorization에 토큰을 담아 전송
   - 토큰도 마찬가지로 노출이 되면 안되지만 basic 방식처럼 민감한 정보가 그대로 노출되는 것보단 나음
   - 이 방식이 Bearer 방식
   - 해당 토큰생성 방식을 jwt를 사용
```java
.httpBasic(hb -> hb.disable())
```

- 필터 생성
  - 여러가지 필터를 설정
  - security filter에서 사용자 커스텀 필터를 사용하려면 다음과 같이 설정
```java
httpSecurity.addFilterAfter(new MyFilter3(), BasicAuthenticationFilter.class);
```
- 필터 설정 후 동작 순서는 무조건 security filter가 먼저 시작
- 만약 spring security의 filter 보다 먼저 시작하고 싶으면 다음과 같이 설정
```java
httpSecurity.addFilterBefore(new MyFilter3(), SecurityContextPersistenceFilter.class);
```
- spring security filter의 순서 참고
  - https://velog.velcdn.com/images/on5949/post/0a013ae9-0e13-4c99-aefd-cb5de4966831/image.png


# jwt 임시 토큰 만들고 테스트
***