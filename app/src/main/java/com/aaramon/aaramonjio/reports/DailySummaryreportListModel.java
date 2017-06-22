package com.aaramon.aaramonjio.reports;

/**
 * Created by dell on 6/17/2017.
 */

class DailySummaryreportListModel {
    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public String getInvoiceNo() {
        return InvoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        InvoiceNo = invoiceNo;
    }

    public String getOrderTiming() {
        return OrderTiming;
    }

    public void setOrderTiming(String orderTiming) {
        OrderTiming = orderTiming;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getPaymentType() {
        return PaymentType;
    }

    public void setPaymentType(String paymentType) {
        PaymentType = paymentType;
    }

    public int getTotalItems() {
        return TotalItems;
    }

    public void setTotalItems(int totalItems) {
        TotalItems = totalItems;
    }

    int OrderId;
    String InvoiceNo;
    String OrderTiming;
    String TotalAmount;
    String PaymentType;
    int TotalItems;
}
