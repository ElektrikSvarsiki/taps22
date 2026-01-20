package org.svarsik.taps22.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.svarsik.taps22.model.Payment;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class PaymentRepository {

    private final JdbcTemplate jdbc;

    public PaymentRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Long create(Long userId, BigDecimal amount, String status) {
        Long id = jdbc.queryForObject("SELECT nextval('payments_seq')", Long.class);

        jdbc.update("""
            INSERT INTO payments(id, user_id, amount, status, created_at)
            VALUES (?, ?, ?, ?, ?)
        """, id, userId, amount, status, Timestamp.valueOf(LocalDateTime.now()));

        return id;
    }

    public void updateStatus(Long id, String status) {
        jdbc.update("UPDATE payments SET status = ? WHERE id = ?", status, id);
    }

    public List<Payment> findAll() {
        return jdbc.query("""
            SELECT * FROM payments ORDER BY created_at DESC
        """, this::map);
    }

    public List<Payment> findByUserId(Long userId) {
        return jdbc.query("""
            SELECT * FROM payments WHERE user_id = ?
            ORDER BY created_at DESC
        """, this::map, userId);
    }

    public Optional<Payment> findById(Long id) {
        List<Payment> list = jdbc.query(
                "SELECT * FROM payments WHERE id = ?",
                this::map,
                id
        );
        return list.stream().findFirst();
    }

    private Payment map(ResultSet rs, int i) throws SQLException {
        return new Payment(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getBigDecimal("amount"),
                rs.getString("status"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
