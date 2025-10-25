package com.example.busreservation.observer;

import com.example.busreservation.model.Payment;

/**
 * Observer interface for payment events
 * Implements the Observer pattern to notify subscribers when payments are made
 */
public interface PaymentObserver {
    void onPaymentCompleted(Payment payment);
}

