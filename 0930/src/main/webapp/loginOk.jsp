<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인 성공</title>
</head>
<body>

    <%@ include file="top.jsp" %>

    <%
        String name =
            (String) application.getAttribute("loginCheck");
    %>

    <h1><%= name %>님, 로그인 성공!!!!</h1>

    <a href="index.jsp">메인으로</a>

</body>
</html>