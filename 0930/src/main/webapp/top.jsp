<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<h1>이효정 서블릿 프로젝트</h1>
<h3>메뉴 : 대학소개 커뮤니티 오시는길
<% if (session.getAttribute("loginUser") == null) { %>
<a href="<%= request.getContextPath() %>/login.jsp">로그인</a>
<a href="<%= request.getContextPath() %>/register.jsp">회원가입</a>
<% } else { %>
<a href="<%= request.getContextPath() %>/logout.do">로그아웃</a>
<a href="<%= request.getContextPath() %>/mypage.do">마이페이지</a>
<a href="<%= request.getContextPath() %>/users.do">회원목록</a>
<% } %>
</h3>
