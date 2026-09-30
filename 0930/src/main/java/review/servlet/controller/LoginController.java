package review.servlet.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

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

        String id = request.getParameter("id");
        String pw = request.getParameter("pw");

        UsersDAO dao = new UsersDAO();
        UsersDTO dto = dao.login(id, pw);

        if (dto != null) {
            HttpSession session = request.getSession();
            session.setAttribute("loginUser", dto);
            response.sendRedirect("loginOk.jsp");
        } else {
            response.sendRedirect("loginFail.jsp");
        }
    }
}
