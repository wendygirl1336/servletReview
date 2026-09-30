package review.servlet.common;

import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

@WebFilter("/*")
public class RequestFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-store");
        try {
            chain.doFilter(request, response);
        } catch (DatabaseException e) {
            request.getServletContext().log("DB 작업 실패", e);
            Throwable cause = e.getCause();
            if (cause instanceof SQLException && ((SQLException) cause).getErrorCode() == 1406) {
                WebSupport.error(request, response, 400, "입력한 값이 DB에 설정된 최대 길이를 초과했습니다.");
            } else {
                WebSupport.error(request, response, 503,
                    "데이터베이스 작업을 완료하지 못했습니다. MySQL 실행 상태, DB 접속 설정 및 users 테이블을 확인해주세요. 변경 내용은 완료되지 않았습니다.");
            }
        }
    }
}
