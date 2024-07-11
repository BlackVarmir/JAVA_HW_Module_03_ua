package model;

import static java.lang.System.out;

public class Fine {
    private String type;
    private double amount;

    public Fine(String type, double amount) {
        this.type = type;
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Fine{" +
                "type='" + type + '\'' +
                ", amount=" + amount +
                '}';
    }
}
