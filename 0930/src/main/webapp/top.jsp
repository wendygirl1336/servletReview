<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<header class="header"><nav class="nav" aria-label="주 메뉴">
<a class="brand" href="<%= request.getContextPath() %>/index.jsp"><small>MEMBER SPACE</small>이효정 서블릿 프로젝트</a>
<div class="nav-links">
<a href="<%= request.getContextPath() %>/index.jsp">홈</a>
<% if (session.getAttribute("loginUser") == null) { %>
<a href="<%= request.getContextPath() %>/login.jsp">로그인</a>
<a href="<%= request.getContextPath() %>/register.jsp">회원가입</a>
<% } else { %>
<a href="<%= request.getContextPath() %>/logout.do">로그아웃</a>
<a href="<%= request.getContextPath() %>/mypage.do">마이페이지</a>
<a href="<%= request.getContextPath() %>/users.do">회원목록</a>
<% } %>
</div></nav></header>
