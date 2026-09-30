<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
</head>

<body>

<h1>회원가입</h1>

<form action="register.do" method="post">

    아이디 :
    <input type="text" name="id">
    <br>

    비밀번호 :
    <input type="password" name="pw">
    <br>

    이름 :
    <input type="text" name="name">
    <br>

    <input type="submit" value="회원가입">
    <input type="reset" value="취소">

</form>

<a href="login.jsp">로그인</a>

</body>
</html>