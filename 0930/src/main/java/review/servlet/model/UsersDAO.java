package review.servlet.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import review.servlet.common.JDBCutil;

public class UsersDAO {

    // 회원가입
    public void register(UsersDTO dto) {

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = JDBCutil.getConnection();

            pstmt = con.prepareStatement(
                "insert into users (id, password, name, role) values (?, ?, ?, ?);"
            );

            pstmt.setString(1, dto.getId());
            pstmt.setString(2, dto.getPassword());
            pstmt.setString(3, dto.getName());
            pstmt.setString(4, "user");

            pstmt.executeUpdate();

        } catch (SQLException e1) {
            e1.printStackTrace();

        } finally {
            JDBCutil.close(pstmt, con);
        }
    }


    // 로그인
    public UsersDTO login(String id, String pw) {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        UsersDTO dto = null;

        try {
            con = JDBCutil.getConnection();

            pstmt = con.prepareStatement(
                "select * from users where id = ? and password = ?"
            );

            pstmt.setString(1, id);
            pstmt.setString(2, pw);

            rs = pstmt.executeQuery();

            if (rs.next()) {

                dto = new UsersDTO();

                dto.setId(rs.getString("id"));
                dto.setPassword(rs.getString("password"));
                dto.setName(rs.getString("name"));
                dto.setRole(rs.getString("role"));
            }

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            JDBCutil.close(rs, pstmt, con);
        }

        return dto;
    }
}