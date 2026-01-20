package org.svarsik.taps22.dto;

import java.math.BigDecimal;

public record CreatePaymentRequest(Long userId, BigDecimal amount) {}