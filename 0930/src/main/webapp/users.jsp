<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="review.servlet.model.UsersDTO" %>
<%@ page import="review.servlet.common.WebSupport" %>
<% if (session.getAttribute("loginUser") == null) {
    response.sendRedirect(request.getContextPath() + "/login.jsp");
    return;
} %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>회원목록</title>
<%@ include file="head.jsp" %>
</head>
<body>
    <%@ include file="top.jsp" %>
    <main>
    <h1>회원목록</h1>
    <p>함께하고 있는 회원을 확인하세요.</p>
    <div class="table-wrap"><table>
        <tr><th>아이디</th><th>이름</th><th>권한</th></tr>
<%
    @SuppressWarnings("unchecked")
    List<UsersDTO> users = (List<UsersDTO>) request.getAttribute("users");
    if (users != null) {
        for (UsersDTO user : users) {
%>
        <tr>
            <td><%= WebSupport.escape(user.getId()) %></td>
            <td><%= WebSupport.escape(user.getName()) %></td>
            <td><%= WebSupport.escape(user.getRole()) %></td>
        </tr>
<%
        }
    }
    if (users == null || users.isEmpty()) {
%>
        <tr><td colspan="3" class="empty">표시할 회원이 없습니다. 회원목록 메뉴를 눌러 조회해주세요.</td></tr>
<% } %>
    </table></div>
    </main>
    <footer class="footer">MEMBER SPACE · Servlet Project</footer>
</body>
</html>
