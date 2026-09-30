<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="review.servlet.model.UsersDTO" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>마이페이지</title>
</head>
<body>

    <%@ include file="top.jsp" %>

    <%
        UsersDTO user = (UsersDTO) request.getAttribute("user");
    %>

    <h1>마이페이지</h1>

    <form action="mypage.do" method="post">

        아이디 :
        <input type="text" name="id" value="<%= user.getId() %>" readonly>
        <br>

        비밀번호 :
        <input type="password" name="pw" value="<%= user.getPassword() %>">
        <br>

        이름 :
        <input type="text" name="name" value="<%= user.getName() %>">
        <br>

        <input type="submit" value="수정">
        <input type="reset" value="취소">

    </form>

    <form action="delete.do" method="post">
        <input type="submit" value="회원탈퇴">
    </form>

</body>
</html>
