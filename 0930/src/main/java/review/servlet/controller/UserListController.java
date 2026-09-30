package review.servlet.controller;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import review.servlet.common.*;
import review.servlet.model.*;

@WebServlet("/users.do")
public class UserListController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UsersDTO login = WebSupport.requireLogin(request, response);
        if (login == null) return;
        UsersDAO dao = new UsersDAO();
        if (dao.findById(login.getId()) == null) {
            request.getSession().invalidate();
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        request.setAttribute("users", dao.findAll());
        request.getRequestDispatcher("/users.jsp").forward(request, response);
    }
}
