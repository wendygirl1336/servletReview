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

@WebServlet("/delete.do")
public class DeleteController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("loginUser") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        UsersDTO loginUser = (UsersDTO) session.getAttribute("loginUser");

        UsersDAO dao = new UsersDAO();

        if (dao.delete(loginUser.getId())) {
            session.invalidate();
            response.sendRedirect("index.jsp");
            return;
        }

        response.sendRedirect("mypage.do");
    }
}
