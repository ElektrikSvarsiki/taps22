package org.svarsik.taps22.model;

import java.math.BigDecimal;

public record User(Long id, String fullName, BigDecimal balance) {}