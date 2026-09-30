<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="review.servlet.model.UsersDTO" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원목록</title>
</head>
<body>

    <%@ include file="top.jsp" %>

    <h1>회원목록</h1>

    <table border="1">
        <tr>
            <th>아이디</th>
            <th>이름</th>
            <th>권한</th>
        </tr>

<%
    List<UsersDTO> users = (List<UsersDTO>) request.getAttribute("users");

    for (UsersDTO user : users) {
%>
        <tr>
            <td><%= user.getId() %></td>
            <td><%= user.getName() %></td>
            <td><%= user.getRole() %></td>
        </tr>
<%
    }
%>
    </table>

</body>
</html>
