package review.servlet.controller;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import review.servlet.common.*;
import review.servlet.model.*;

@WebServlet("/mypage.do")
public class MyPageController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UsersDTO login = WebSupport.requireLogin(request, response);
        if (login == null) return;
        UsersDTO user = new UsersDAO().findById(login.getId());
        if (user == null) {
            request.getSession().invalidate();
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        request.getSession().setAttribute("loginUser", user);
        request.setAttribute("user", user);
        request.getRequestDispatcher("/WEB-INF/views/mypage.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UsersDTO login = WebSupport.requireLogin(request, response);
        if (login == null) return;
        String name = request.getParameter("name");
        String password = request.getParameter("pw");
        if (!WebSupport.valid(name, 100) || password == null || password.length() > 255
                || (!password.isEmpty() && password.isBlank())) {
            response.setStatus(400);
            request.setAttribute("error", "이름을 입력하고 비밀번호의 입력 길이를 확인해주세요.");
            doGet(request, response);
            return;
        }
        if (!new UsersDAO().update(login.getId(), password, name.trim())) {
            request.getSession().invalidate();
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        request.getSession().setAttribute("loginUser",
                new UsersDTO(login.getId(), null, name.trim(), login.getRole()));
        response.sendRedirect(request.getContextPath() + "/mypage.do?updated=1");
    }
}
