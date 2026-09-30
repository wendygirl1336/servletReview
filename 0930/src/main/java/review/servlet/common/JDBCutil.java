package review.servlet.common;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public final class JDBCutil {
    private JDBCutil() {}

    // JVM -D 옵션 > 환경 변수 > 클래스패스 db.properties 순서로 설정한다.
    private static String setting(String key, String environment, String fallback) {
        String value = System.getProperty(key);
        if (value != null) return value;
        value = System.getenv(environment);
        if (value != null) return value;
        Properties properties = new Properties();
        try (InputStream in = JDBCutil.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) properties.load(in);
        } catch (java.io.IOException e) {
            throw new DatabaseException("DB 설정 파일을 읽을 수 없습니다.", e);
        }
        return properties.getProperty(key, fallback);
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Connector/J가 WEB-INF/lib에 필요합니다.", e);
        }
        Properties config = new Properties();
        config.setProperty("user", setting("db.user", "DB_USER", "root"));
        config.setProperty("password", setting("db.password", "DB_PASSWORD", ""));
        config.setProperty("connectTimeout", "5000");
        config.setProperty("socketTimeout", "10000");
        return DriverManager.getConnection(
                setting("db.url", "DB_URL", "jdbc:mysql://localhost:3306/springdb"), config);
    }

    // 기존 코드와 호환. 한 자원 close 실패가 나머지 close를 막지 않는다.
    public static void close(PreparedStatement statement, Connection connection) {
        close(null, statement, connection);
    }

    public static void close(ResultSet result, PreparedStatement statement, Connection connection) {
        for (AutoCloseable resource : new AutoCloseable[] { result, statement, connection }) {
            if (resource != null) {
                try { resource.close(); }
                catch (Exception e) {
                    java.util.logging.Logger.getLogger(JDBCutil.class.getName())
                        .log(java.util.logging.Level.WARNING, "DB 자원 정리 실패", e);
                }
            }
        }
    }
}
