package org.svarsik.taps22.dto;

import java.math.BigDecimal;

public record PaymentResult(Long paymentId, String status, BigDecimal balance) {}