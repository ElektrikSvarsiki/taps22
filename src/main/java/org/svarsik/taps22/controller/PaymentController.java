package org.svarsik.taps22.controller;

import org.springframework.web.bind.annotation.*;
import org.svarsik.taps22.dto.CreatePaymentRequest;
import org.svarsik.taps22.dto.PaymentResult;
import org.svarsik.taps22.model.Payment;
import org.svarsik.taps22.service.PaymentService;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @PostMapping
    public PaymentResult create(@RequestBody CreatePaymentRequest req) {
        return service.createPayment(req.userId(), req.amount());
    }

    @GetMapping
    public List<Payment> all() {
        return service.getAllPayments();
    }

    @GetMapping("/user/{userId}")
    public List<Payment> byUser(@PathVariable Long userId) {
        return service.getPaymentsByUser(userId);
    }

    @GetMapping("/{id}")
    public Payment one(@PathVariable Long id) {
        return service.getPayment(id);
    }
}
