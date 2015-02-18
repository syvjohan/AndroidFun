package com.example.johan.assignment3;

/**
 * Created by johan on 2/17/2015.
 */
public class Summary {
    private Double income = 0.0;
    private Double expense = 0.0;
    private Double summary = 0.0;

    public Summary() {}

    public Summary(Double income, Double expense, Double summary) {
        this.income = income;
        this.expense = expense;
        this.summary = summary;
    }

    public Double GetIncome() {return this.income; }
    public void SetIncome(Double income) {this.income = income; }

    public Double GetExpense() {return this.expense; }
    public void SetExpense(Double expense) {this.expense = expense; }

    public Double GetSummary() {return this.summary; }
    public void SetSummary(Double summary) {this.summary = summary; }
}

