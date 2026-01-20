package org.svarsik.taps22.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.svarsik.taps22.model.User;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbc;

    public UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Optional<User> findById(Long id) {
        String sql = "SELECT id, full_name, balance FROM users WHERE id = ?";
        return jdbc.query(sql, rs -> {
            if (!rs.next()) return Optional.empty();
            return Optional.of(new User(
                    rs.getLong("id"),
                    rs.getString("full_name"),
                    rs.getBigDecimal("balance")
            ));
        }, id);
    }

    public void updateBalance(Long id, BigDecimal newBalance) {
        jdbc.update("UPDATE users SET balance = ? WHERE id = ?", newBalance, id);
    }
}
