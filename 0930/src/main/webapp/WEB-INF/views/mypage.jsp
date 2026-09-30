<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="review.servlet.common.WebSupport,review.servlet.model.UsersDTO" %>
<!DOCTYPE html>
<html lang="ko">
<head><meta charset="UTF-8"><title>마이페이지</title><%@ include file="/head.jsp" %>
</head>
<body>
<%@ include file="/top.jsp" %>
<main>
<h1>마이페이지</h1>
<% if (request.getAttribute("error") != null) { %>
<p class="notice" role="alert"><%= WebSupport.escape((String) request.getAttribute("error")) %></p>
<% } %>
<% UsersDTO user = (UsersDTO) request.getAttribute("user"); %>
<% if ("1".equals(request.getParameter("updated"))) { %><p>회원 정보가 수정되었습니다.</p><% } %>
<form action="<%= request.getContextPath() %>/mypage.do" method="post">
<label for="id">아이디</label><input id="id" value="<%= WebSupport.escape(user.getId()) %>" readonly>
<label for="name">이름</label><input id="name" name="name" value="<%= WebSupport.escape(user.getName()) %>" maxlength="100" required>
<label for="pw">새 비밀번호</label><input id="pw" type="password" name="pw" maxlength="255" autocomplete="new-password">
<p class="hint">비밀번호를 비워두면 기존 비밀번호를 유지합니다.</p>
<input type="submit" value="수정"><input type="reset" value="취소">
</form>
<form class="danger" action="<%= request.getContextPath() %>/delete.do" method="post"
onsubmit="return confirm('정말 회원탈퇴하시겠습니까?');">
<button type="submit">회원탈퇴</button>
</form>
</main><footer class="footer">MEMBER SPACE · Servlet Project</footer>
</body></html>
