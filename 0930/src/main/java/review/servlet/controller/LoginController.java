package review.servlet.controller;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import review.servlet.common.*;
import review.servlet.model.*;

@WebServlet("/login.do")
public class LoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        String password = request.getParameter("pw");
        if (!WebSupport.valid(id, 64) || !WebSupport.valid(password, 255)) {
            response.sendRedirect(request.getContextPath() + "/loginFail.jsp");
            return;
        }
        UsersDTO user = new UsersDAO().login(id.trim(), password);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/loginFail.jsp");
            return;
        }
        HttpSession session = request.getSession();
        request.changeSessionId();
        session.setAttribute("loginUser", user);
        response.sendRedirect(request.getContextPath() + "/loginOk.jsp");
    }
}
