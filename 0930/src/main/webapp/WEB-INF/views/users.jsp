<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="review.servlet.common.WebSupport,review.servlet.model.UsersDTO" %>
<!DOCTYPE html>
<html lang="ko">
<head><meta charset="UTF-8"><title>회원목록</title></head>
<body>
<%@ include file="/top.jsp" %>
<h1>회원목록</h1>
<%@ page import="java.util.List" %>
<table border="1">
<tr><th>아이디</th><th>이름</th><th>권한</th></tr>
<% List<UsersDTO> users = (List<UsersDTO>) request.getAttribute("users");
for (UsersDTO user : users) { %>
<tr><td><%= WebSupport.escape(user.getId()) %></td>
<td><%= WebSupport.escape(user.getName()) %></td>
<td><%= WebSupport.escape(user.getRole()) %></td></tr>
<% } if (users.isEmpty()) { %><tr><td colspan="3">등록된 회원이 없습니다.</td></tr><% } %>
</table>
</body></html>
