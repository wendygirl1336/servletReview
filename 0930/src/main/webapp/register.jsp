<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="review.servlet.common.WebSupport,review.servlet.model.UsersDTO" %>
<!DOCTYPE html>
<html lang="ko">
<head><meta charset="UTF-8"><title>회원가입</title></head>
<body>
<%@ include file="/top.jsp" %>
<h1>회원가입</h1>
<% if (request.getAttribute("error") != null) { %>
<p><%= WebSupport.escape((String) request.getAttribute("error")) %></p>
<% } %>
<form action="<%= request.getContextPath() %>/register.do" method="post">
아이디 : <input name="id" maxlength="64" autocomplete="username" required
value="<%= WebSupport.escape(request.getParameter("id")) %>"><br>
비밀번호 : <input type="password" name="pw" maxlength="255" autocomplete="new-password" required><br>
이름 : <input name="name" maxlength="100" required
value="<%= WebSupport.escape(request.getParameter("name")) %>"><br>
<input type="submit" value="회원가입"><input type="reset" value="취소">
</form>
</body></html>
