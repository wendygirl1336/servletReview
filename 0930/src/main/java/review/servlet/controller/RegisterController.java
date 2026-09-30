package review.servlet.controller;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import review.servlet.common.*;
import review.servlet.model.*;

@WebServlet("/register.do")
public class RegisterController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/register.jsp");
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        String password = request.getParameter("pw");
        String name = request.getParameter("name");
        if (!WebSupport.valid(id, 64) || !WebSupport.valid(password, 255) || !WebSupport.valid(name, 100)) {
            response.setStatus(400);
            request.setAttribute("error", "아이디(64자), 비밀번호(255자), 이름(100자)을 빈칸 없이 입력해주세요.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }
        try {
            new UsersDAO().register(new UsersDTO(id.trim(), password, name.trim(), "user"));
            response.sendRedirect(request.getContextPath() + "/login.jsp?registered=1");
        } catch (DatabaseException e) {
            if (e.getCause() instanceof java.sql.SQLException
                    && ((java.sql.SQLException) e.getCause()).getErrorCode() == 1062) {
                response.setStatus(409);
                request.setAttribute("error", "이미 사용 중인 아이디입니다.");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }
            throw e;
        }
    }
}
