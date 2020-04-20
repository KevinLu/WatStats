package com.kevinlu.watstats.data;

//Class for a transactions which includes this data
public class Transaction {
    private String terminal;
    private int color;
    private String location;
    private String amount;
    private String balanceType;
    private Date dateCategory; //used with Date class for headings
    private String dateTime; //The actual text of date and time to output

    public Transaction(String terminal, int color, String location, String amount, String balanceType, String dateTime, Date dateCategory) {
        this.terminal = terminal;
        this.color = color;
        this.location = location;
        this.amount = amount;
        this.balanceType = balanceType;
        this.dateCategory = dateCategory;
        this.dateTime = dateTime;
    }

    public String getLocation() {
        return location;
    }

    public String getBalanceType() {
        return balanceType;
    }

    public String getTerminal() {
        return terminal;
    }

    public int getColor() {
        return color;
    }

    public String getAmount() {
        return amount;
    }

    public String getDateTime() {
        return dateTime;
    }

    public int getDateID() {
        return dateCategory.getId();
    }

    public String getDate() {
        return dateCategory.getDate();
    }
}
