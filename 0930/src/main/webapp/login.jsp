<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="review.servlet.model.UsersDTO" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>
</head>
<body>

    <%@ include file="top.jsp" %>

<%
    UsersDTO loginUser = (UsersDTO) session.getAttribute("loginUser");

    if (loginUser == null) {
%>

    <h1>로그인</h1>

    <form action="login.do" method="post">

        아이디 :
        <input type="text" name="id">
        <br>

        암호 :
        <input type="password" name="pw">
        <br>

        <input type="submit" value="로그인">
        <input type="reset" value="취소">

    </form>

    <a href="register.jsp">회원가입</a>

<%
    } else {
%>

    <h1><%= loginUser.getName() %>님, 환영합니다.</h1>

    <form action="logout.do" method="get">
        <input type="submit" value="로그아웃">
    </form>

<%
    }
%>

</body>
</html>
