# open-places

카카오 API 와 네이버 API 를 이용한 장소검색 서비스

실행하기 위한 java option
-Dopenapi.naver.client.id=cHpcZ7xP_InAHp6pxHxI
-Dopenapi.naver.client.secret=4_6CiyF0Qg
-Dopenapi.kakao.admin.key=a4b8cab008561bfc33ec2b072c8ea1ea
-Dserver.address=localhost
-Dserver.port=8080
-Dsecurity.jwt.secret=secretkey

이클립스에서 메이븐 프로젝트를 임포트하여 실행하면
schema.sql 에 정의된 테이블이 자동으로 생성된다.
이후 아래의 테스트 절차에 따라 테스트를 진행한다.

테스트
1) 테스트 절차
    - 계정 등록 > 로그인 > 키워드 조회 > 아이디별 조회 이력 조회 > hot keyword 조회
    
2) 계정등록
curl -d '{"memberId": "ggang","password": "ggang!12", "name": "shagkang", "email": "shagkang@crossent.com"}' \
-H "Accept: application/json" -H "Content-Type: application/json" \
-X POST "http://localhost:8080/account"

3) 로그인
curl -d '{"memberId": "mtest","password": "mtest!12"}' \
-H "Accept: application/json" -H "Content-Type: application/json" \
-X POST "http://localhost:8080/account/token"

    >> 실제 실행결과
ggangMacPro:~ seunghagkang$ curl -d '{"memberId": "mtest","password": "mtest!12"}' \
> -H "Accept: application/json" -H "Content-Type: application/json" \
> -X POST "http://localhost:8080/account/token"
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzIiwicm9sZXMiOlsiUk9MRV9NRU1CRVIiXSwiaWF0IjoxNjE2NjE4Mjc1LCJleHAiOjE2MTY3MDQ2NzV9.I0rNAd1vH8IZ7EX-oYsaciziJLi63Qq5ro94NT0_XxMggangMacPro:~ seunghagkang$ 
    >> 응답으로 온 문자열 복사하여 인증토큰으로 사용

4) 키워드 조회
curl -G "http://localhost:8080/place?size=10" \
--data-urlencode "keyword=정자동 BHC" \
-H "Accept: application/json" -H "Content-Type: application/json" \
-H "X-AUTH-TOKEN: {로그인결과 문자열}"

    >> 실제실행 결과
ggangMacPro:~ seunghagkang$ curl -G "http://localhost:8080/place?size=10" \
> --data-urlencode "keyword=정자동 BHC" \
> -H "Accept: application/json" -H "Content-Type: application/json" \
> -H "X-AUTH-TOKEN: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzIiwicm9sZXMiOlsiUk9MRV9NRU1CRVIiXSwiaWF0IjoxNjE2NjE4Mjc1LCJleHAiOjE2MTY3MDQ2NzV9.I0rNAd1vH8IZ7EX-oYsaciziJLi63Qq5ro94NT0_XxM"
[{"name":"BHC치킨 미금점","provider":"KAKAO"},{"name":"BHC치킨 분당정자점","provider":"KAKAO"},{"name":"BHC치킨 분당행복점","provider":"KAKAO"},{"name":"BHC치킨 서현시범단지점","provider":"KAKAO"},{"name":"창업도우미 BHC&C","provider":"KAKAO"}]

5) 조회 이력 조회
curl -G "http://localhost:8080/history/member/mtest?size=100" \
-H "Accept: application/json" -H "Content-Type: application/json" \
-H "X-AUTH-TOKEN: {로그인결과 문자열}"

6. 인기 키워드 조회
curl -G "http://localhost:8080/history/hot" \
-H "Accept: application/json" -H "Content-Type: application/json" \
-H "X-AUTH-TOKEN: {로그인결과 문자열}"
