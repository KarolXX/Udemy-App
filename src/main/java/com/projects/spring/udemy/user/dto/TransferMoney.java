package com.projects.spring.udemy.user.dto;

public class TransferMoney {
    private int amount;

    TransferMoney() {
    }

    public TransferMoney(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
