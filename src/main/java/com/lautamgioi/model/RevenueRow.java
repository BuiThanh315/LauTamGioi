package com.lautamgioi.model;

import java.math.BigDecimal;

public class RevenueRow {
    private String period;
    private int invoiceCount;
    private BigDecimal total;

    public RevenueRow(String period, int invoiceCount, BigDecimal total) {
        this.period = period;
        this.invoiceCount = invoiceCount;
        this.total = total;
    }

    public String getPeriod() {
        return period;
    }

    public int getInvoiceCount() {
        return invoiceCount;
    }

    public BigDecimal getTotal() {
        return total;
    }
}
