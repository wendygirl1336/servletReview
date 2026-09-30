package review.servlet.common;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import review.servlet.model.UsersDTO;

public final class WebSupport {
    private WebSupport() {}
    public static String escape(String value) {
        return value == null ? "" : value.replace("&", "&amp;").replace("<", "&lt;")
            .replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#39;");
    }
    public static boolean valid(String value, int maximum) {
        return value != null && !value.isBlank() && value.length() <= maximum;
    }
    public static UsersDTO requireLogin(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        UsersDTO user = session == null ? null : (UsersDTO) session.getAttribute("loginUser");
        if (user == null) response.sendRedirect(request.getContextPath() + "/login.jsp");
        return user;
    }
    public static void error(HttpServletRequest request, HttpServletResponse response,
            int status, String message) throws ServletException, IOException {
        response.setStatus(status);
        request.setAttribute("error", message);
        request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
    }
}
