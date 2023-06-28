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


# 구글 로그인 준비

## 내용
- 구글 oauth api 키 세팅
- http://localhost:8888/login/oauth2/code/google
  - 여기서 login/oauth2/code는 고정 주소
  - 서비스에서 파라미터로 인증 정보가 주어졌을 때 인증을 성공하면 구글에서 리다이렉트할 URL


- SecurityConfig에 .oauth2Login(oauth2 -> oauth2.loginPage("/loginForm")) 설정이 핑요함
- 구글 로그인 이후 후처리가 필요함

# 구글 회원 프로필 정보 받아보기

## 내용
- 카카오 로그인 같은 경우 다음과 같은 순으로 처리가됨
1. 코드 받기(인증)
2. 엑세스토큰(권한)
3. 사용자 프로필 정보 확보
4. 정보를 토대로 회원가입을 자동으로 진행
   1. 확보한 정보 외에 필요한 정보가 필요할 경우 추가적인 정보를 입력하도록 함


- 구글 로그인이 완료
  - 코드를 받는것이 아닌 엑세스토큰과 사용자 정보를 한번에 받음
  - DefaultOAuth2UserService를 상속하여 구글로 받은 데이터에 대한 후처리를 함
    - 후처리를 해주는 메소드를 오버라이드 해야함
    - loadUser(OAuth2UserRequest userRequest)
      - 해당 함수는 sub, name, given_name, family_name, picture, email, email_verified, locale 데이터를 넘겨줌

***
##### 23.06.20


# Authentication객체가 가질수 있는 2가지타입
- getClientRegistration()에서 얻은 registrationId로 어떤 Oauth로 로그인 했는지 확인가능
- 회원프로필을 받는과정
1. 구글로그인 버튼 클릭
2. 구글로그인창
3. 로그인 완료
4. code 리턴(OAuth-Client 라이브러리)
5. AccessToken 요청
6. userRequest 정보
7. loadUser함수 호출
8. 구글로부터 회원프로필 받기


## 내용
1. Authentication 클래스
  - Authentication의 getPrincipal()은 object 타입으로 리턴
  - 그리하여 PrincipalDetails 클래스로 형변환 후 데이터 컨트롤
  - 해당 클래스 DI를 통해 User 정보를 받을 수 있음


2. @AuthenticationPrincipal 어노테이션
  - 세션유저에 접근할 수 있음
  - 따라서 User 정보를 받을 수 있음
```java
@GetMapping("/test/login")
    public @ResponseBody String loginTest(Authentication authentication, 
@AuthenticationPrincipal PrincipalDetails userDetails) { 
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        log.info("loginTest() :" +principalDetails.getUser());
        log.info("userDetails: " + userDetails.getUser());

        return "세션 정보 확인하기";
    }
```
- 결과
```shell
loginTest() :User(id=2, username=eee, password=$2a$10$fJ2M.oR1YujmSAS1SfZiBeijDSH0a0qTZBTosButWccplfk/MrSJi, email=eee@naver.com, role=ROLE_USER, provider=null, providerId=null, createDate=2023-06-18 18:06:14.517694)
userDetails: User(id=2, username=eee, password=$2a$10$fJ2M.oR1YujmSAS1SfZiBeijDSH0a0qTZBTosButWccplfk/MrSJi, email=eee@naver.com, role=ROLE_USER, provider=null, providerId=null, createDate=2023-06-18 18:06:14.517694)
```
- 위와 같은 결과가 나타남
- 위의 코드는 그냥 소셜로그인이 아닌 일반 로그인일때만 가능함
- 소셜로그인으로 진행하였을 떄 ClassCastException이 나타남
### 해결
```java
@GetMapping("/test/oauth/login")
    public @ResponseBody String testOauthLogin(Authentication authentication, @AuthenticationPrincipal OAuth2User oAuth) { // DI(의존성 주입)
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        log.info("testOauthLogin() :" +oAuth2User.getAttributes());
        log.info("oauth2User() :" +oAuth.getAttributes());


        return "oauth 세션 정보 확인하기";
    }
```
- 다음과 같이 OAuth2User 클래스를 DI 하거나 @AuthenticationPrincipal을 통해 user의 정보를 얻을 수 있음

## 정리
- 스프링 시큐리티
  - 서버 세션안에 시큐리티가 관리하는 세션이 존재함
  - 시큐리티 세션에 들어갈 수 있는 타입은 Authentication 객체뿐임(무조건)
  - 사용하려면 컨트롤러에서 Authentication을 DI 해야함
    - Authentication 안에 들어갈 수 있는 타입
      1. UserDetails 타입
         - 일반적인 로그인을 할때
      2. OAuth2User 타입
         - 소셜로그인 할 때 
  - 하지만 컨트롤러에서 두가지를 처리하기에는 너무 복잡해짐
  - 그렇기에 UserDetails와 Oauth2User를 implements를 하여 유연하게 처리하도록 설정
  - 이미 PrincipalDetails에는 Oauth2User가 implements되어 있기에 OAuth2User도 implements 해주면 됨

***
##### 23.06.21

# 구글로그인 및 자동 회원가입 진행 완료

## 내용
- 회원가입을 하려면 User 객체가 필요
  - 하지만 시큐리티 세션에는 OAuth2User와 userDetails 객체만 존재
  - 그리하여 PrincipalDetails 클래스를 만든거임


```java
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    
    @Autowired
    private UserRepository userRepository;

    // 구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("getClientRegistration(): " + userRequest.getClientRegistration());
        log.info("getAccessToken().getTokenValue(): " + userRequest.getAccessToken().getTokenValue());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("getAttributes(): " + oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getClientId(); // google
        String providerId = oAuth2User.getAttribute("sub");
        String username = provider+"_"+providerId;
        String password = bCryptPasswordEncoder.encode("겟인데어");
        String email = oAuth2User.getAttribute("email");
        String role = "ROLE_USER";

        User user = userRepository.findByUsername(username);

        if (user == null) {
            user = user.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();

            userRepository.save(user);
        }

        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }
}
```
- 다음과 같이 PrincipalOauth2UserService 클래스에 다음과 같이 설정해두면 oauth로 로그인시
자동 회원가입을 시키며 로그인이 된다.
- 따라서 이전시간에 일반로그인 컨트롤러와 oauth 로그인 컨트롤러를 나눌필요 없이
다음과 같이 설정할 수 있다.
```java
@GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        log.info("principalDetails: " + principalDetails.getUser());
        return "user";
    }
```
- 이와 같이 하나의 클래스를 통해 일반로그인와 oauth 로그인 둘다 처리가 가능해진다.
- 또한 PrincipalDetailsService와 PrincipalOauth2UserService는 만들지 않아도 알아서 내부 메소드가 호출되어 로그인이 됨.


- 근데 굳이 왜 만들었냐?
  - PrincipalDetails 타입을 리턴하기 위해 만들었음.
  - 리턴과 동시에 Authentication에 저장됨.


- 또 @AuthenticationPrincipal 어노테이션은 각 PrincipalDetailsService와 PrincipalOauth2UserService의
함수가 종료될 시 만들어진다.

***
##### 23.06.14

# 페이스북 로그인 완료

## 내용
- 페이스북 api 설정
- yaml설정 시 구글로그인 경우 scope를 email과 profile을 받도록 설정
- 달리 페이스북 로그인시 email과 public_profile로 scope 설정
  - 그 이유는 페이스북 api 문서(https://developers.facebook.com/docs/facebook-login/web)
  에서 확인해보면 호출 샘플을 확인할 수 있고 위와 같이 설명한 형태로 데이터가 넘어오기 때문에 설정


- 기존에 구글로그인 시에는 oauth2user.getAttributes()를 통해 넘어오는 데이터가 
sub, name, email이 있었지만 facebook으로 넘어오는 데이터는 id, name, email 이여서
db 저장시 providerid 가 null이 돼버리는 현상 발생
- 이에 구분지어서 데이터를 받기위해 interface를 만들고 각 class를 지정해줌

***
##### 23.06.26

# 네이버 로그인

## 내용
* 보충설명
   - Oauth2-client 라이브러리
     - provider 존재
       - 페이스북, 구글, 트위터 등 기본제공
       - 네이버, 카카오는 존재하지 않음
         - 이유는 getAttribute 값이 모두 다르기 때문에


- Oauth2 방식(4가지 존재)
  - Code
    - 코드를 부여받는 방식(네이버로그인)
  - client credentials grant type
    - react or js로 구성

```shell
naver:
 client-id:
 client-secret:
 scope:
  - name
  - email
 client-name: naver
 authorization-grant-type: authorization_code
 redirect-uri: http://localhost:8888/login/oauth2/code/naver
```

##### 위와 같이 yaml 파일을 설정해줘야함
- 이유는 기본 provider로 지정되어 있지않기 때문
- 하지만 등록후 저장시 터미널에서 다음과 같은 에러를 띄움
```shell
threw exception with message: Provider ID must be specified for client registration 'naver'
```
##### 이는 oauth2의 기본 provider에 naver가 등록되지 않기 떄문에 나타나는 이슈
- 해결하기 위해 다음과 같이 설정
```shell
provider:
          naver:
           authorization-uri: https://nid.naver.com/oauth2.0/authorize
           token-uri: https://nid.naber.com/oaut2.0/token
           user-info-uri: https://openapi.naver.com/v1/nid/me
           user-name-attribute: response #회원정보를 json으로 받는데 response라는 키값으로 네이버가 리턴해줌.
```
##### 각 내용들은 https://developers.naver.com/docs/login/devguide/devguide.md#2-2-1-%EC%86%8C%EC%85%9C-%EB%A1%9C%EA%B7%B8%EC%9D%B8 여기서 확인
- 3.4.2 ~ 3.4.5 확인

### 여기까지 spring boot 기본 로그인 + OAuth2.0 로그인 통합구현

***
##### 23.06.27

# JWT(1)
- JSON WEB TOKEN
- 왜 사용하고, 어디에 쓰이는지(중요)
- 세션의 고질적인 문제를 해결하기 위함

## 세션
- 로그인 요청(인증)
  1. 클라이언트가 최초 request
  2. 서버 -> session 저장소에 session id 생성 (세션 저장소는 사이즈가 큼)
  3. 각 세션id에 저장소가 생김
  4. 응답 시 header에 세션id를 돌려줌
  5. 클라이언트(web)에 세션id가 저장됨
  6. 로그인 요청
  7. db에 값 확인
  8. 해당 세션id 저장소에 DB에 저장된 사용자 정보를 저장
  9. 메인페이지로 이동
  10. 유저정보 요청(세션id를 들고 서버로 요청)
  11. 서버에서는 해당 세션id가 존재하는지 확인 후 해당 저장소에 접근
  12. DB에서 해당 데이터를 응답받고
  13. 전달받은 데이터를 클라이언트로 전달


- 세션id
  1. 최초요청
  2. 서버 -> 목록에 식별자를 만들고 식별자를 돌려줌
     1. html를 return 할때 header에 식별자를 담음
  3. web의 쿠키에 해당 식별자를 저장해놓음
  4. 두번째 요청시 web에서 서버로 식별자를 들고 요청
  5. 서버는 식별자가 기존에 만든 식별자인지 확인
  - 세션id가 사라지는 시점
    1. session의 값을 서버쪽에서 삭제
    2. 사용자가 브라우저를 종료
       - web 쿠키게 저장된 세션id는 사라짐 
    3. 서버에 남겨져있는 의미없는 식별자는 기본적으로 30분후에 삭제됨

### 요약
  - 사용자 인증
  - 민감한 정보를 접근할 때


### 단점
  - ex)
    - 클라이언트의 수가 많아지면 서버에서 처리해줄 수 있는 양은 한정적
    - 따라서 서버에 부하가 걸리고 처리속도가 낮아짐
    - 이를 해결하기 위해 서버의 개수를 적절하게 늘림
      - 로드밸런싱
        - 서버의 부하를 분산


  - 위와 같이 여러 서버가 분산되어있음 최초로 한번 요청했던 서버에는 세션정보가 남아있지만 
만약 그 세션정보가 담긴 서버가 혼잡해서 다른 서버로 요청이 갔을 경우 해당 서버에는 
현재 사용자의 세션정보가 없기때문에 새로운 세션을 만들어버리는 경우가 발생
    1. 이를 해결하기 위해선 모든 서버에 세션정보를 복사해버리던가
    2. 또 최초의 요청을 했던 서버로 무조건 요청이 가도록 설정
      - 하지만 두 방법다 너무 복잡하고 번거로움
      - 이를 해결하기 위해선 DB에 세션값을 저장하여 모든 서버가 참조하도록 설정
      - 하지만 하드디스크에 접근하기 때문에 성능이 저하됨(풀스캔)
        - 이를 해결하기 위해 메모리 공유 서버를 사용함
        - 대표적으로 redis가 존재

***
##### 23.06.28
# JWT(2)

## TCP
- 신뢰성이 높은 통신
- OSI 7계층을 통해 나온 데이터를 전달하는 방식 중 하나
  1. TCP
     - ack 통신을 통해 확인 후 데이터를 전송
     - 신뢰성이 높은 통신
     - WEB에서는 TCP로 통신함
  2. UDP
     - 상대가 데이터를 잘 받았는지 확인하지 않고 보냄
     - 그렇기에 중간에 데이터 유실이 일어남
     - 영상, 음성통화 등 데이터를 받을 상대가 사람일경우 사용되는 방식


## CIA
- 기밀성
  - 허가되지 않는 사용자 혹은 대상이 접근이 가능한 경우 기밀성이


- 무결성
  - 특정 정보에 대해서 허가되지 않는 사용자나 대상이 데이터의 변경 또는 삭제가 될 경우 무결성이 깨짐


- 가용성
  - 원하는 시간, 환경, 서비스시에 특정 정보를 사용할 수 있고, 만약 사용이 불가능하다면 해당 정보에 대한 가용성이 깨짐

### 문제점
1. 기밀성을 유지하기 위해 암호화를 하고 복호화가 가능한 열쇠를 만들면 기밀성은 유지가 되지만
   전달 받은쪽에서 해당 열쇠가 없다면 가용성이 깨진다. 그렇기에 열쇠를 같이 보내야하지만 열쇠 또한 탈취가
   될 수 있다.

2. 데이터를 요청한 대상으로부터 전달한 대상에게 잘 받았다는 응답메시지를 전달할 때 
누가 보냈는지 인증 문제가 발생 (이유는 잘 받았다는 응답 자체도 중간에 탈취할 수 있기때문)

## RSA (암호화)
- 시멘트리 키(대칭 키)
  - 키 하나로 암호화, 복호화 가능


- public key
  - 공개 키
  - 개인 키로 복호화 가능
  - 보통 암호화 할때 사용


- private key
  - 개인 키 
  - 공개 키로 복호화 가능
  - 전자문서 등 서명, 즉 인증에 사용

#### ex.1)
- a에서 b로 데이터를 전달할 때 데이터 암호화를 b의 공개키로 암호화하면
중간에 탈취가 되더라도 확인이 불가능함(복호화를 하려면 개인키가 필요하기 때문)
- 따라서 문제없이 전달 받고 b는 개인키로 해당 데이터를 복호화 할 수 있음 -> 첫번째 문제점 해결

#### ex.2)
- a가 b로 어떠한 데이터를 전달 할 때 a의 개인키로 암호화 하여 전달
- 물론 중간에 데이터가 탈취되어 다른 대상이 확인 할 수 있음 하지만 이 데이터의 전달 목적은 인증이 목적임
- 이에 b는 a가 보낸 데이터를 전달받고 a의 공개 키로 복호화가 가능
  - 이는 즉 a의 개인 키로 암호화가 되었다는 뜻이며 해당 개인 키는 해당 대상만 가지고 있을 수 있으므로
누가 보냈는지 확인이 가능함

#### ex.3)
- a에서 b로 데이터를 전송할 때 b의 공개키로 전달, 중간에 탈취하여 열어볼 순 없지만 삭제할 순있음
- 이에 a의 기본키로 한번더 암호화를 진행
- 이렇게 되면 b에서는 a의 공개 키로 복호화가 가능해짐
- 위와 같이 복호화가 가능하다면 인증이 된것, 반면 복호화가 불가능하다면 중간에 탈취되었다는 의미로
인증이 되지 않은 것
- 따라서 인증이 되었다면 b의 개인키로 열어볼 수 있으고 데이터 확인을 할 수 있음
- 이러한 방식이 RSA 암호화 방식


### RFC 7519 공식 문서 참고
***
