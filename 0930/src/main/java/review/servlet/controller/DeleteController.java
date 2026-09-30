package review.servlet.controller;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import review.servlet.common.*;
import review.servlet.model.*;

@WebServlet("/delete.do")
public class DeleteController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UsersDTO login = WebSupport.requireLogin(request, response);
        if (login == null) return;
        if (!new UsersDAO().delete(login.getId())) {
            WebSupport.error(request, response, 404, "탈퇴할 회원 정보를 찾을 수 없습니다.");
            return;
        }
        request.getSession().invalidate();
        response.sendRedirect(request.getContextPath() + "/index.jsp?deleted=1");
    }
}
