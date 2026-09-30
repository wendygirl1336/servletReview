package review.servlet.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import review.servlet.model.UsersDAO;
import review.servlet.model.UsersDTO;

@WebServlet("/register.do")
public class RegisterController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String id = request.getParameter("id");
        String password = request.getParameter("pw");
        String name = request.getParameter("name");

        UsersDTO dto = new UsersDTO();

        dto.setId(id);
        dto.setPassword(password);
        dto.setName(name);
        dto.setRole("user");

        UsersDAO dao = new UsersDAO();

        dao.register(dto);

        response.sendRedirect("login.jsp");
    }
}