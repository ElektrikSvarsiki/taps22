package org.svarsik.taps22.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.svarsik.taps22.dto.PaymentResult;
import org.svarsik.taps22.exceptions.InsufficientBalanceException;
import org.svarsik.taps22.exceptions.UserNotFoundException;
import org.svarsik.taps22.model.Payment;
import org.svarsik.taps22.model.User;
import org.svarsik.taps22.repository.PaymentRepository;
import org.svarsik.taps22.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    private UserRepository userRepo;
    private PaymentRepository paymentRepo;
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        userRepo = mock(UserRepository.class);
        paymentRepo = mock(PaymentRepository.class);
        paymentService = new PaymentService(userRepo, paymentRepo);
    }

    @Test
    void createPayment_success() {
        Long userId = 1L;
        BigDecimal amount = BigDecimal.valueOf(50);
        User user = new User(userId, "John Doe", BigDecimal.valueOf(100));

        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(paymentRepo.create(userId, amount, "PENDING")).thenReturn(123L);

        PaymentResult result = paymentService.createPayment(userId, amount);

        assertEquals(123L, result.paymentId());
        assertEquals("SUCCESS", result.status());
        assertEquals(BigDecimal.valueOf(50), result.balance());

        verify(userRepo).updateBalance(userId, BigDecimal.valueOf(50));
        verify(paymentRepo).updateStatus(123L, "SUCCESS");
    }

    @Test
    void createPayment_insufficientBalance() {
        Long userId = 1L;
        BigDecimal amount = BigDecimal.valueOf(200);
        User user = new User(userId, "John Doe", BigDecimal.valueOf(100));

        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(paymentRepo.create(userId, amount, "PENDING")).thenReturn(123L);

        assertThrows(InsufficientBalanceException.class,
                () -> paymentService.createPayment(userId, amount));

        verify(userRepo, never()).updateBalance(anyLong(), any());
        verify(paymentRepo, never()).updateStatus(anyLong(), any());
    }

    @Test
    void createPayment_userNotFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> paymentService.createPayment(1L, BigDecimal.valueOf(50)));

        verify(paymentRepo, never()).create(anyLong(), any(), any());
    }

    @Test
    void getAllPayments_callsRepo() {
        List<Payment> payments = List.of(new Payment(1L, 1L, BigDecimal.valueOf(50), "SUCCESS", LocalDateTime.now()));
        when(paymentRepo.findAll()).thenReturn(payments);

        List<Payment> result = paymentService.getAllPayments();
        assertEquals(payments, result);
    }

    @Test
    void getPaymentsByUser_userNotFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,
                () -> paymentService.getPaymentsByUser(1L));
    }

    @Test
    void getPayment_paymentNotFound() {
        when(paymentRepo.findById(999L)).thenReturn(Optional.empty());
        assertThrows(org.svarsik.taps22.exceptions.PaymentNotFoundException.class,
                () -> paymentService.getPayment(999L));
    }
}
