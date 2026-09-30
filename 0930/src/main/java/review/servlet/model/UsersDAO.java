package review.servlet.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import review.servlet.common.JDBCutil;

public class UsersDAO {

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

    public UsersDTO findById(String id) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        UsersDTO dto = null;

        try {
            con = JDBCutil.getConnection();
            pstmt = con.prepareStatement(
                "select * from users where id = ?"
            );
            pstmt.setString(1, id);
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

    public boolean update(String id, String password, String name) {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = JDBCutil.getConnection();
            pstmt = con.prepareStatement(
                "update users set password = ?, name = ? where id = ?"
            );

            pstmt.setString(1, password);
            pstmt.setString(2, name);
            pstmt.setString(3, id);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCutil.close(pstmt, con);
        }

        return false;
    }

    public boolean delete(String id) {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = JDBCutil.getConnection();
            pstmt = con.prepareStatement(
                "delete from users where id = ?"
            );
            pstmt.setString(1, id);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCutil.close(pstmt, con);
        }

        return false;
    }

    public List<UsersDTO> findAll() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<UsersDTO> list = new ArrayList<>();

        try {
            con = JDBCutil.getConnection();
            pstmt = con.prepareStatement(
                "select * from users order by id"
            );
            rs = pstmt.executeQuery();

            while (rs.next()) {
                UsersDTO dto = new UsersDTO();
                dto.setId(rs.getString("id"));
                dto.setPassword(rs.getString("password"));
                dto.setName(rs.getString("name"));
                dto.setRole(rs.getString("role"));
                list.add(dto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCutil.close(rs, pstmt, con);
        }

        return list;
    }
}
