<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="review.servlet.common.WebSupport,review.servlet.model.UsersDTO" %>
<!DOCTYPE html>
<html lang="ko">
<head><meta charset="UTF-8"><title>회원가입</title><%@ include file="/head.jsp" %>
</head>
<body>
<%@ include file="/top.jsp" %>
<main>
<h1>회원가입</h1>
<% if (request.getAttribute("error") != null) { %>
<p class="notice" role="alert"><%= WebSupport.escape((String) request.getAttribute("error")) %></p>
<% } %>
<form action="<%= request.getContextPath() %>/register.do" method="post">
<label for="id">아이디</label><input id="id" name="id" maxlength="64" autocomplete="username" required
value="<%= WebSupport.escape(request.getParameter("id")) %>">
<label for="pw">비밀번호</label><input id="pw" type="password" name="pw" maxlength="255" autocomplete="new-password" required>
<label for="name">이름</label><input id="name" name="name" maxlength="100" required
value="<%= WebSupport.escape(request.getParameter("name")) %>">
<input type="submit" value="회원가입"><input type="reset" value="취소">
</form>
</main><footer class="footer">MEMBER SPACE · Servlet Project</footer>
</body></html>
