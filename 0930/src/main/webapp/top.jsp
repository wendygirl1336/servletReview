<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
    Object menuLoginUser = session.getAttribute("loginUser");
%>

<h1>이효정 서블릿 프로젝트</h1>

<h3>
    메뉴 :
    대학소개
    커뮤니티
    오시는길

<%
    if (menuLoginUser == null) {
%>
    <a href="login.jsp">로그인</a>
    <a href="register.jsp">회원가입</a>
<%
    } else {
%>
    <a href="logout.do">로그아웃</a>
    <a href="mypage.do">마이페이지</a>
    <a href="users.do">회원목록</a>
<%
    }
%>
</h3>
