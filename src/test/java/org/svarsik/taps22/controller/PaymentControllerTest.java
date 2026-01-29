package org.svarsik.taps22.controller;




import org.junit.jupiter.api.Test;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.svarsik.taps22.dto.CreatePaymentRequest;
import org.svarsik.taps22.dto.PaymentResult;
import org.svarsik.taps22.model.Payment;
import org.svarsik.taps22.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createPayment_shouldReturnPaymentResult() throws Exception {
        CreatePaymentRequest req = new CreatePaymentRequest(1L, BigDecimal.valueOf(100));
        PaymentResult result = new PaymentResult(42L, "SUCCESS", BigDecimal.valueOf(900));

        when(service.createPayment(req.userId(), req.amount())).thenReturn(result);

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentId").value(42))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.balance").value(900));

        verify(service).createPayment(req.userId(), req.amount());
    }

    @Test
    void getAllPayments_shouldReturnList() throws Exception {
        Payment payment = new Payment(1L, 1L, BigDecimal.valueOf(100), "SUCCESS", LocalDateTime.now());
        when(service.getAllPayments()).thenReturn(List.of(payment));

        mockMvc.perform(get("/payments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].userId").value(1))
                .andExpect(jsonPath("$[0].amount").value(100))
                .andExpect(jsonPath("$[0].status").value("SUCCESS"));

        verify(service).getAllPayments();
    }

    @Test
    void getPaymentsByUser_shouldReturnList() throws Exception {
        Payment payment = new Payment(1L, 1L, BigDecimal.valueOf(100), "SUCCESS", LocalDateTime.now());
        when(service.getPaymentsByUser(1L)).thenReturn(List.of(payment));

        mockMvc.perform(get("/payments/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].userId").value(1));

        verify(service).getPaymentsByUser(1L);
    }

    @Test
    void getPaymentById_shouldReturnPayment() throws Exception {
        Payment payment = new Payment(1L, 1L, BigDecimal.valueOf(100), "SUCCESS", LocalDateTime.now());
        when(service.getPayment(1L)).thenReturn(payment);

        mockMvc.perform(get("/payments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.amount").value(100))
                .andExpect(jsonPath("$.status").value("SUCCESS"));

        verify(service).getPayment(1L);
    }
}
