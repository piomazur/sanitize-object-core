package com.azimo.tukan.log.structure.model;

public class ProcessDetails {
    private String userId;

    private String transactionMtn;

    private String paymentId;

    private String payerId;

    public ProcessDetails() {
    }

    public ProcessDetails(final String userId, final String transactionMtn, final String paymentId, final String payerId) {
        this.userId = userId;
        this.transactionMtn = transactionMtn;
        this.paymentId = paymentId;
        this.payerId = payerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    public String getTransactionMtn() {
        return transactionMtn;
    }

    public void setTransactionMtn(final String transactionMtn) {
        this.transactionMtn = transactionMtn;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(final String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(final String payerId) {
        this.payerId = payerId;
    }
}
