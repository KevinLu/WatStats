package com.kevinlu.watstats.data;

public class MonthlySpending {
    private String month;
    private String amountSpent;

    public MonthlySpending(String month, String amountSpent) {
        this.month = month;
        this.amountSpent = amountSpent;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getAmountSpent() {
        return amountSpent;
    }

    public void setAmountSpent(String amountSpent) {
        this.amountSpent = amountSpent;
    }
}
