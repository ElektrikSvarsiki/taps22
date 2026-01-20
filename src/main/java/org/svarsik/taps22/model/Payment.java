package org.svarsik.taps22.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Payment(
        Long id,
        Long userId,
        BigDecimal amount,
        String status,
        LocalDateTime createdAt
) {}