package review.servlet.model;

import java.sql.*;
import java.util.*;
import review.servlet.common.DatabaseException;
import review.servlet.common.JDBCutil;

public class UsersDAO {
    private UsersDTO user(ResultSet result) throws SQLException {
        return new UsersDTO(result.getString("id"), null, result.getString("name"), result.getString("role"));
    }
    public void register(UsersDTO dto) {
        try (Connection con = JDBCutil.getConnection();
             PreparedStatement stmt = con.prepareStatement(
                 "insert into users (id, password, name, role) values (?, ?, ?, 'user')")) {
            stmt.setString(1, dto.getId());
            stmt.setString(2, dto.getPassword());
            stmt.setString(3, dto.getName());
            stmt.executeUpdate();
        } catch (SQLException e) { throw new DatabaseException("회원가입 실패", e); }
    }
    public UsersDTO login(String id, String password) {
        try (Connection con = JDBCutil.getConnection();
             PreparedStatement stmt = con.prepareStatement(
                 "select id, name, role from users where id = ? and password = ?")) {
            stmt.setString(1, id);
            stmt.setString(2, password);
            try (ResultSet result = stmt.executeQuery()) {
                return result.next() ? user(result) : null;
            }
        } catch (SQLException e) { throw new DatabaseException("로그인 조회 실패", e); }
    }
    public UsersDTO findById(String id) {
        try (Connection con = JDBCutil.getConnection();
             PreparedStatement stmt = con.prepareStatement("select id, name, role from users where id = ?")) {
            stmt.setString(1, id);
            try (ResultSet result = stmt.executeQuery()) {
                return result.next() ? user(result) : null;
            }
        } catch (SQLException e) { throw new DatabaseException("회원 조회 실패", e); }
    }
    public boolean update(String id, String password, String name) {
        // 비밀번호를 비워 제출하면 기존 비밀번호를 유지한다.
        boolean changePassword = password != null && !password.isEmpty();
        String sql = changePassword ? "update users set name = ?, password = ? where id = ?"
                                    : "update users set name = ? where id = ?";
        try (Connection con = JDBCutil.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, name);
            if (changePassword) stmt.setString(2, password);
            stmt.setString(changePassword ? 3 : 2, id);
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) { throw new DatabaseException("회원 수정 실패", e); }
    }
    public boolean delete(String id) {
        try (Connection con = JDBCutil.getConnection();
             PreparedStatement stmt = con.prepareStatement("delete from users where id = ?")) {
            stmt.setString(1, id);
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) { throw new DatabaseException("회원탈퇴 실패", e); }
    }
    public List<UsersDTO> findAll() {
        List<UsersDTO> users = new ArrayList<>();
        try (Connection con = JDBCutil.getConnection();
             PreparedStatement stmt = con.prepareStatement("select id, name, role from users order by id");
             ResultSet result = stmt.executeQuery()) {
            while (result.next()) users.add(user(result));
            return users;
        } catch (SQLException e) { throw new DatabaseException("회원목록 조회 실패", e); }
    }
}
