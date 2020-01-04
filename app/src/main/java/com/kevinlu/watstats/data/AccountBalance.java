package com.kevinlu.watstats.data;

public class AccountBalance {
    private int accountIcon;
    private String accountTitle;
    private String accountBalance;
    private String accountBalanceChange;

    public AccountBalance(int accountIcon, String accountTitle, String accountBalance, String accountBalanceChange) {
        this.accountIcon = accountIcon;
        this.accountTitle = accountTitle;
        this.accountBalance = accountBalance;
        this.accountBalanceChange = accountBalanceChange;
    }

    public int getAccountIcon() {
        return accountIcon;
    }

    public void setAccountIcon(int accountIcon) {
        this.accountIcon = accountIcon;
    }

    public String getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

    public String getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getAccountBalanceChange() {
        return accountBalanceChange;
    }

    public void setAccountBalanceChange(String accountBalanceChange) {
        this.accountBalanceChange = accountBalanceChange;
    }
}
