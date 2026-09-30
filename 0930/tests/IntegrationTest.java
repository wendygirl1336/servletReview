import java.net.*;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.sql.*;
import java.util.*;
import org.apache.catalina.startup.Tomcat;
import review.servlet.common.JDBCutil;

/** Run only against a disposable MySQL instance; creates its own temporary schema. */
public class IntegrationTest {
    static String base;
    static int checks;
    static HttpClient client() {
        return HttpClient.newBuilder().cookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ALL))
                .followRedirects(HttpClient.Redirect.NEVER).build();
    }
    static HttpResponse<String> get(HttpClient client, String path) throws Exception {
        return client.send(HttpRequest.newBuilder(URI.create(base + path)).GET().build(),
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    }
    static HttpResponse<String> post(HttpClient client, String path, String... pairs) throws Exception {
        StringJoiner form = new StringJoiner("&");
        for (int i=0; i<pairs.length; i+=2)
            form.add(URLEncoder.encode(pairs[i], StandardCharsets.UTF_8) + "="
                    + URLEncoder.encode(pairs[i+1], StandardCharsets.UTF_8));
        return client.send(HttpRequest.newBuilder(URI.create(base + path))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(form.toString())).build(),
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    }
    static void check(boolean condition, String name) {
        if (!condition) throw new AssertionError(name);
        System.out.println("PASS " + (++checks) + ": " + name);
    }
    static boolean redirect(HttpResponse<String> response, String target) {
        return response.statusCode()==302 && response.headers().firstValue("location").orElse("").endsWith(target);
    }
    public static void main(String[] args) throws Exception {
        String server = System.getProperty("test.mysql.url");
        if (server == null) throw new IllegalArgumentException("Set -Dtest.mysql.url=jdbc:mysql://127.0.0.1:13307");
        String user = System.getProperty("test.mysql.user", "root");
        String password = System.getenv().getOrDefault("TEST_MYSQL_PASSWORD", "");
        String schema = "codex_review_test_" + Long.toUnsignedString(System.nanoTime());
        String url = server + "/" + schema;
        Class.forName("com.mysql.cj.jdbc.Driver");
        Tomcat tomcat = new Tomcat();
        try (Connection admin = DriverManager.getConnection(server, user, password)) {
            admin.createStatement().execute("CREATE DATABASE " + schema + " CHARACTER SET utf8mb4");
            try (Connection db = DriverManager.getConnection(url, user, password)) {
                String sql = Files.readString(Path.of(args[1]));
                db.createStatement().execute(sql.substring(sql.indexOf("CREATE TABLE")));
            }
            System.setProperty("db.url", url);
            System.setProperty("db.user", user);
            System.setProperty("db.password", password);
            tomcat.setBaseDir(args[2]);
            tomcat.setPort(0);
            tomcat.getConnector().setProperty("address", "127.0.0.1");
            tomcat.addWebapp("/review", Path.of(args[0]).toAbsolutePath().toString());
            try {
                tomcat.start();
                base = "http://127.0.0.1:" + tomcat.getConnector().getLocalPort() + "/review";
                HttpClient guest=client(), alice=client(), bob=client(), secondAlice=client();
                HttpResponse<String> response=get(guest,"/index.jsp");
                check(response.statusCode()==200 && response.body().contains(">로그인</a>")
                    && response.body().contains(">회원가입</a>") && !response.body().contains(">회원목록</a>"),"guest menu");
                for(String path : new String[]{"/mypage.do","/users.do"})
                    check(redirect(get(guest,path),"/login.jsp"),"anonymous GET "+path);
                for(String path : new String[]{"/mypage.do","/delete.do"})
                    check(redirect(post(guest,path),"/login.jsp"),"anonymous POST "+path);
                for(String path : new String[]{"/mypage.jsp","/users.jsp"})
                    check(get(guest,path).statusCode()==302,"direct JSP redirects "+path);
                check(get(guest,"/WEB-INF/views/mypage.jsp").statusCode()==404,"protected JSP not public");
                check(redirect(get(guest,"/loginOk.jsp"),"/login.jsp"),"login success page guarded");
                check(post(guest,"/register.do","id"," ","pw","","name","").statusCode()==400,"blank registration rejected");
                check(redirect(post(alice,"/register.do","id","alice","pw","old-pass","name","홍길동"),"/login.jsp?registered=1"),"registration");
                response=post(guest,"/register.do","id","alice","pw","unused","name","duplicate");
                check(response.statusCode()==409 && response.body().contains("이미 사용"),"duplicate id handled");
                check(redirect(post(bob,"/register.do","id","bob","pw","bob-pass","name","다른 회원"),"/login.jsp?registered=1"),"second registration");
                check(redirect(post(alice,"/login.do","id","alice","pw","wrong"),"/loginFail.jsp"),"wrong password");
                check(redirect(post(guest,"/login.do","id","' OR 1=1 --","pw","test"),"/loginFail.jsp"),"SQL injection rejected");
                check(redirect(post(alice,"/login.do","id","alice","pw","old-pass"),"/loginOk.jsp"),"login");
                response=get(alice,"/index.jsp");
                check(response.body().contains(">로그아웃</a>") && response.body().contains(">마이페이지</a>")
                    && response.body().contains(">회원목록</a>") && !response.body().contains(">회원가입</a>"),"authenticated menu");
                check(!get(guest,"/index.jsp").body().contains(">회원목록</a>"),"sessions isolated");
                response=get(alice,"/mypage.do");
                check(response.statusCode()==200 && response.body().contains("홍길동") && response.body().contains("readonly")
                    && !response.body().contains("old-pass"),"profile renders without stored password");
                check(post(alice,"/mypage.do","name"," ","pw","new-pass").statusCode()==400,"blank update rejected");
                check(redirect(post(alice,"/mypage.do","id","bob","role","admin","name","새이름<script>","pw","new-pass"),
                    "/mypage.do?updated=1"),"profile update");
                response=get(alice,"/users.do");
                check(response.statusCode()==200 && response.body().contains("새이름&lt;script&gt;")
                    && !response.body().contains("새이름<script>") && !response.body().contains("new-pass"),"member list escapes names and excludes passwords");
                check(redirect(post(bob,"/login.do","id","bob","pw","bob-pass"),"/loginOk.jsp"),"cannot update another id");
                check(!get(alice,"/users.do").body().contains(">admin<"),"role cannot be escalated");
                check(get(alice,"/login.jsp").body().contains("새이름&lt;script&gt;"),"session display refreshed");
                check(redirect(post(secondAlice,"/login.do","id","alice","pw","old-pass"),"/loginFail.jsp"),"old password invalid");
                check(redirect(post(secondAlice,"/login.do","id","alice","pw","new-pass"),"/loginOk.jsp"),"new password works");
                check(redirect(post(alice,"/mypage.do","name","유지","pw",""),"/mypage.do?updated=1"),"empty password preserves existing");
                check(redirect(post(secondAlice,"/login.do","id","alice","pw","new-pass"),"/loginOk.jsp"),"preserved password works");
                check(get(alice,"/delete.do").statusCode()==405,"GET cannot delete");
                System.setProperty("db.url","jdbc:mysql://127.0.0.1:1/unavailable");
                response=post(guest,"/register.do","id","failure","pw","pass","name","failure");
                check(response.statusCode()==503 && response.body().contains("데이터베이스")
                    && !response.body().contains("NullPointerException"),"connection failure has friendly error");
                check(get(alice,"/users.do").statusCode()==503,"failed list is not shown as empty");
                check(post(alice,"/mypage.do","name","failed","pw","").statusCode()==503,"failed update not reported as success");
                check(post(alice,"/delete.do").statusCode()==503,"failed deletion does not log out");
                System.setProperty("db.url",url);
                check(get(alice,"/mypage.do").statusCode()==200,"session survives transient DB failure");
                try(Connection db=DriverManager.getConnection(url,user,password)) {
                    db.createStatement().execute("RENAME TABLE users TO users_saved");
                    check(get(alice,"/users.do").statusCode()==503,"missing table handled");
                    db.createStatement().execute("RENAME TABLE users_saved TO users");
                }
                check(redirect(post(alice,"/delete.do","id","bob"),"/index.jsp?deleted=1"),"withdrawal uses session id");
                check(redirect(get(alice,"/users.do"),"/login.jsp"),"withdrawal invalidates session");
                check(redirect(get(secondAlice,"/users.do"),"/login.jsp"),"other stale session rejected");
                check(redirect(post(guest,"/login.do","id","alice","pw","new-pass"),"/loginFail.jsp"),"deleted member cannot login");
                check(get(bob,"/mypage.do").statusCode()==200,"other member preserved");
                check(redirect(get(bob,"/logout.do"),"/login.jsp"),"logout");
                check(redirect(get(bob,"/mypage.do"),"/login.jsp"),"logged out cannot access profile");
                JDBCutil.close(null,null);
                JDBCutil.close(null,null,null);
                check(true,"null-safe cleanup regression");
                System.out.println("ALL "+checks+" HTTP/MYSQL CHECKS PASSED");
            } finally {
                tomcat.stop();
                tomcat.destroy();
                admin.createStatement().execute("DROP DATABASE " + schema);
            }
        }
    }
}
