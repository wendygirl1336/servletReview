<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="review.servlet.common.WebSupport,review.servlet.model.UsersDTO" %>
<!DOCTYPE html>
<html lang="ko">
<head><meta charset="UTF-8"><title>처리 안내</title><%@ include file="/head.jsp" %>
</head>
<body>
<%@ include file="/top.jsp" %>
<main>
<h1>처리 안내</h1>
<% if (request.getAttribute("error") != null) { %>
<p class="notice" role="alert"><%= WebSupport.escape((String) request.getAttribute("error")) %></p>
<% } %>
<a href="<%= request.getContextPath() %>/index.jsp">메인으로</a>
</main><footer class="footer">MEMBER SPACE · Servlet Project</footer>
</body></html>
