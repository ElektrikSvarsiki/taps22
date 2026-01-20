package org.svarsik.taps22.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.svarsik.taps22.dto.PaymentResult;
import org.svarsik.taps22.exceptions.InsufficientBalanceException;
import org.svarsik.taps22.exceptions.PaymentNotFoundException;
import org.svarsik.taps22.exceptions.UserNotFoundException;
import org.svarsik.taps22.model.Payment;
import org.svarsik.taps22.model.User;
import org.svarsik.taps22.repository.PaymentRepository;
import org.svarsik.taps22.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentService {

    private final UserRepository userRepo;
    private final PaymentRepository paymentRepo;

    public PaymentService(UserRepository userRepo, PaymentRepository paymentRepo) {
        this.userRepo = userRepo;
        this.paymentRepo = paymentRepo;
    }

    @Transactional
    public PaymentResult createPayment(Long userId, BigDecimal amount) {

        User user = userRepo.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Long paymentId = paymentRepo.create(userId, amount, "PENDING");

        BigDecimal newBalance = user.balance().subtract(amount);

        if (newBalance.signum() < 0) {
            throw new InsufficientBalanceException();
        }

        userRepo.updateBalance(userId, newBalance);
        paymentRepo.updateStatus(paymentId, "SUCCESS");

        return new PaymentResult(paymentId, "SUCCESS", newBalance);
    }


    public List<Payment> getAllPayments() {
        return paymentRepo.findAll();
    }

    public List<Payment> getPaymentsByUser(Long userId) {
        if (userRepo.findById(userId).isEmpty()) {
            throw new UserNotFoundException();
        }
        return paymentRepo.findByUserId(userId);
    }

    public Payment getPayment(Long id) {
        return paymentRepo.findById(id)
                .orElseThrow(PaymentNotFoundException::new);
    }
}
