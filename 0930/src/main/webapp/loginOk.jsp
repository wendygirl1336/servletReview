<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="review.servlet.model.UsersDTO" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인 성공</title>
</head>
<body>

    <%@ include file="top.jsp" %>

    <%
        UsersDTO loginUser = (UsersDTO) session.getAttribute("loginUser");

        if (loginUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }
    %>

    <h1><%= loginUser.getName() %>님, 로그인 성공!!!!</h1>

    <a href="index.jsp">메인으로</a>

</body>
</html>
