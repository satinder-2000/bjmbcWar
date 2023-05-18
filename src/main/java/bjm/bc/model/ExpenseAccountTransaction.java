/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bjm.bc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author user
 */
@Entity
@Table(name = "EXPENSE_ACCOUNT_TRANSACTION")
public class ExpenseAccountTransaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    @Column(name = "MONEY_IN")
    private double moneyIn;
    @Column(name = "MONEY_OUT")
    private double moneyOut;
    @Column(name = "YEAR")
    private int year;
    @Column(name = "YTD_BALANCE")
    private int ytdBalance;
    @Column(name = "EXPENSE_ACCOUNT_ID")
    private int expenseAccountId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMoneyIn() {
        return moneyIn;
    }

    public void setMoneyIn(double moneyIn) {
        this.moneyIn = moneyIn;
    }

    public double getMoneyOut() {
        return moneyOut;
    }

    public void setMoneyOut(double moneyOut) {
        this.moneyOut = moneyOut;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getYtdBalance() {
        return ytdBalance;
    }

    public void setYtdBalance(int ytdBalance) {
        this.ytdBalance = ytdBalance;
    }

    public int getExpenseAccountId() {
        return expenseAccountId;
    }

    public void setExpenseAccountId(int expenseAccountId) {
        this.expenseAccountId = expenseAccountId;
    }
    
    
    
    
}