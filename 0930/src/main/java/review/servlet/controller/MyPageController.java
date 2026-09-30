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

@WebServlet("/mypage.do")
public class MyPageController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("loginUser") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        UsersDTO loginUser = (UsersDTO) session.getAttribute("loginUser");
        UsersDAO dao = new UsersDAO();
        UsersDTO user = dao.findById(loginUser.getId());

        if (user == null) {
            session.invalidate();
            response.sendRedirect("login.jsp");
            return;
        }

        session.setAttribute("loginUser", user);
        request.setAttribute("user", user);
        request.getRequestDispatcher("mypage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("loginUser") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        UsersDTO loginUser = (UsersDTO) session.getAttribute("loginUser");

        String password = request.getParameter("pw");
        String name = request.getParameter("name");

        UsersDAO dao = new UsersDAO();

        if (dao.update(loginUser.getId(), password, name)) {
            UsersDTO updatedUser = dao.findById(loginUser.getId());
            session.setAttribute("loginUser", updatedUser);
        }

        response.sendRedirect("mypage.do");
    }
}
