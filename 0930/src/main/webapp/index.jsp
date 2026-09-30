<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>서블릿 Review</title>
<%@ include file="/head.jsp" %>
</head>
<body>
 
    <%@ include file="top.jsp" %>
<main>

    <span class="eyebrow">WELCOME TO MEMBER SPACE</span><h1 class="hero">함께하는 공간,<br>여기서 시작하세요.</h1><p>내 정보를 관리하고, 함께하는 회원을 만나보세요.</p><% if (session.getAttribute("loginUser") == null) { %><a class="button" href="register.jsp">회원가입하기</a><a class="button secondary" href="login.jsp">로그인</a><% } else { %><a class="button" href="mypage.do">내 정보 관리</a><a class="button secondary" href="users.do">회원목록 보기</a><% } %><div class="feature-row"><div><strong>간편한 가입</strong>아이디와 기본 정보로 시작</div><div><strong>나의 프로필</strong>이름과 비밀번호 관리</div><div><strong>함께하는 회원</strong>회원목록을 한눈에</div></div>

</main><footer class="footer">MEMBER SPACE · Servlet Project</footer>
</body>
</html>