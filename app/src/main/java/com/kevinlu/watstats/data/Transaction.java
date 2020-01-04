package com.kevinlu.watstats.data;

//Class for a transactions which includes this data
public class Transaction {
    private String terminal;
    private int image;
    private String amount;
    private Date dateCategory; //used with Date class for headings
    private String dateTime; //The actual text of date and time to output

    public Transaction(String terminal, int image, String amount, String dateTime, Date dateCategory) {
        this.terminal = terminal;
        this.image = image;
        this.amount = amount;
        this.dateCategory = dateCategory;
        this.dateTime = dateTime;
    }

    public String getTerminal() {
        return terminal;
    }

    public int getImage() {
        return image;
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
