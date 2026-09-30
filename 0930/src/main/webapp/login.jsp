<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="review.servlet.common.WebSupport,review.servlet.model.UsersDTO" %>
<!DOCTYPE html>
<html lang="ko">
<head><meta charset="UTF-8"><title>로그인</title></head>
<body>
<%@ include file="/top.jsp" %>
<h1>로그인</h1>
<% UsersDTO loginUser = (UsersDTO) session.getAttribute("loginUser");
if (loginUser == null) { %>
<% if ("1".equals(request.getParameter("registered"))) { %><p>회원가입이 완료되었습니다.</p><% } %>
<form action="<%= request.getContextPath() %>/login.do" method="post">
아이디 : <input name="id" maxlength="64" autocomplete="username" required><br>
비밀번호 : <input type="password" name="pw" maxlength="255" autocomplete="current-password" required><br>
<input type="submit" value="로그인"><input type="reset" value="취소">
</form>
<% } else { %>
<p><%= WebSupport.escape(loginUser.getName()) %>님, 환영합니다.</p>
<% } %>
</body></html>
