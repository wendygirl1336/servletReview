<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% if (session.getAttribute("loginUser") == null) {
    response.sendRedirect(request.getContextPath() + "/login.jsp"); return;
} %>
<%@ page import="review.servlet.common.WebSupport,review.servlet.model.UsersDTO" %>
<!DOCTYPE html>
<html lang="ko">
<head><meta charset="UTF-8"><title>로그인 성공</title><%@ include file="/head.jsp" %>
</head>
<body>
<%@ include file="/top.jsp" %>
<main>
<h1>로그인 성공</h1>
<p><%= WebSupport.escape(((UsersDTO) session.getAttribute("loginUser")).getName()) %>님, 로그인 성공!</p>
<a href="<%= request.getContextPath() %>/index.jsp">메인으로</a>
</main><footer class="footer">MEMBER SPACE · Servlet Project</footer>
</body></html>
