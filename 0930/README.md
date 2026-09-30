# 6주차 Servlet 회원 관리

디자인은 추가하지 않았습니다. Java 21 / Tomcat 10.1 / MySQL 8.x를 사용합니다.

## 실행 설정 (중요)

1. Eclipse에서 **0930 폴더**를 Existing Projects into Workspace로 가져옵니다.
2. MySQL에서 `database/schema.sql`을 실행합니다. 기존 테이블/회원은 삭제하지 않습니다.
3. `db.properties.example`을 `src/main/java/db.properties`로 복사하고 **실제 MySQL 포트·계정·비밀번호**를 입력합니다.
   이 파일은 Git에서 제외됩니다. 비밀번호를 커밋하지 마세요.
4. Project → Clean 후 Tomcat 서버에서 Clean/Publish하고 재시작합니다.
5. 서버에 지정된 context root의 `index.jsp`로 접속합니다.

설정 우선순위는 JVM 옵션(`-Ddb.url`, `-Ddb.user`, `-Ddb.password`) →
환경 변수(`DB_URL`, `DB_USER`, `DB_PASSWORD`) → 클래스패스의 `db.properties`입니다.
별도 설정이 없으면 localhost:3306 / springdb / root / 빈 비밀번호입니다.
DB가 실제로 다른 포트에서 실행 중이면 해당 포트를 지정해야 합니다.

이번 확인 당시 PC는 3306에서 MySQL이 실행 중이었고 기존 코드의 3307은 연결되지 않았습니다.
기존 코드에 적힌 비밀번호도 3306 서버에서 거부되었습니다.
이는 실제 접속 설정으로 해결해야 하며, 잘못된 설정에서도 이제 null 정리 예외 대신 안내 화면을 보여줍니다.

기존 users 테이블은 id / password / name / role 열과 id의 PRIMARY KEY 또는 UNIQUE 제약이 필요합니다.
새 테이블 기준 길이는 64 / 255 / 100 / 20입니다. 기존 테이블이 더 짧으면 해당 길이에 맞는 값을 입력하거나
기존 데이터를 백업한 뒤 스키마를 별도로 조정하세요. 스크립트가 기존 스키마를 자동 변경하지는 않습니다.

## 구현

- 비로그인: 로그인 / 회원가입 메뉴
- 로그인: 로그아웃 / 마이페이지 / 회원목록 메뉴
- `GET /mypage.do`: 본인 회원 정보 표시
- `POST /mypage.do`: 이름·비밀번호 수정. 비밀번호 공란은 기존 값 유지
- `POST /delete.do`: 본인 탈퇴 후 세션 종료. GET 요청으로는 탈퇴 불가
- `GET /users.do`: 로그인한 회원만 목록 조회

아이디와 role은 요청 파라미터로 변경할 수 없습니다. role은 권한 정보이므로 회원 프로필 수정 대상에서 제외했습니다.
세션과 화면에 저장된 비밀번호를 넣지 않습니다. 기존 수업 DB의 비밀번호 비교 방식은 유지합니다.
JSP 출력은 HTML 이스케이프하며, 실제 조회 JSP는 WEB-INF 내부에 둡니다.
기존 mypage.jsp/users.jsp 주소도 컨트롤러로 이동하므로 직접 접근 시 null 오류가 나지 않습니다.

## 오류 수정

- JDBC 연결 실패 시 null 반환을 없애고 원인 예외를 보존합니다.
- DAO는 try-with-resources로 자원을 정리합니다. 기존 close 메서드도 null을 허용합니다.
- 중복 아이디: 409 안내, 빈 값/길이 오류: 400 안내
- DB 접속/쿼리 실패: 503 안내 및 서버 로그. 실패를 성공이나 빈 목록으로 표시하지 않습니다.
- 수정 후 세션 정보 갱신, 탈퇴 후 세션 무효화, 삭제된 회원의 다른 세션 접근 차단
- 추적되던 오래된 build/classes 파일을 제거하여 소스로 다시 빌드하도록 했습니다.

## 검증

MySQL 8.4.6의 별도 테스트 인스턴스와 Tomcat 10.1.48에서 **44개 실제 HTTP/MySQL 검증**을 통과했습니다.
Java 컴파일과 모든 JSP 사전 컴파일도 통과했습니다.

테스트는 `tests/IntegrationTest.java`에 있습니다. 테스트용 MySQL 서버에 새 임시 스키마만 만들고 종료 시 제거합니다.
실제 수업 DB의 데이터에는 접근하지 않습니다. DB 연결 실패 및 테이블 누락도 재현합니다.

재실행: Java 21, Node.js, 별도 MySQL 테스트 서버를 준비한 뒤 PowerShell에서:

```powershell
# 비밀번호가 필요한 테스트 서버라면 TEST_MYSQL_PASSWORD 환경 변수를 먼저 설정합니다.
./tests/run-tests.ps1 -MySqlUrl "jdbc:mysql://127.0.0.1:13307" -MySqlUser root
```

테스트 스크립트는 Maven Central에서 검증용 Tomcat/ECJ 라이브러리를 target에 내려받습니다.
수업용 Tomcat을 중지하거나 기존 MySQL 서비스를 변경하지 않습니다.
