package com.testing.smartbudgetmanager.models;

public class DailyBudgetModel {
    private String name;
    private int amount;
    private String description;
    private Boolean income;
    private String key;
    private String date;


    public DailyBudgetModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIncome() {
        return income;
    }

    public void setIncome(Boolean income) {
        this.income = income;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public DailyBudgetModel(String name, int amount, String description, Boolean income, String key, String date) {
        this.name = name;
        this.amount = amount;
        this.description = description;
        this.income = income;
        this.key = key;
        this.date = date;
    }



}
