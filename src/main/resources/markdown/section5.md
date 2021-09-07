## 실전프로젝트 - 인가 프로세스 DB 연동 웹 계층 구현

### 스프링 시큐리티 인가 개요

- DB와 연동하여 자원 및 권한을 설정하고 제어함으로 동적 권한 관리가 가능하도록 한다
    - 설정 클래스 소스에서 권한 관련 코드 모두 제거
        - ex) antMatcher("/user").hasRole("USER")
- 관리자 시스템 구축
    - 회원 관리 - 권한 부여
    - 권한 관리 - 권한 생성, 삭제
    - 자원 관리 - 자원 생성, 삭제, 수정, 권한 매핑
- 권한 계층 구현
    - URL - URL 요청 시 인가 처리
    - Method - 메소드 호출 시 인가처리
        - Method
        - Pointcut
        
### 주요 아키텍처 이해
![authorization](../static/images/authorization.png)
![authorization_architecture](../static/images/authorization_architecture.png)
- 스프링 시큐리티의 인가처리
  - http.antMatchers("/user").access("hasRole('USER')")
    - 사용자가 /user 자원에 접근하기 위해서는 ROLE_USER 권한이 필요하다