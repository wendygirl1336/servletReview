package review.servlet.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import review.servlet.model.UsersDAO;
import review.servlet.model.UsersDTO;

@WebServlet("/login.do")
public class LoginController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // login.jsp에서 입력한 값 받기
        String id = request.getParameter("id");
        String pw = request.getParameter("pw");

        // DB에서 로그인 확인
        UsersDAO dao = new UsersDAO();
        UsersDTO dto = dao.login(id, pw);

        // 로그인 성공
        if (dto != null) {

            getServletContext().setAttribute(
                    "loginCheck",
                    dto.getName()
            );

            response.sendRedirect("loginOk.jsp");

        // 로그인 실패
        } else {

            response.sendRedirect("loginFail.jsp");
        }
    }
}