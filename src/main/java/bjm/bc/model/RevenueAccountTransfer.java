/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bjm.bc.model;

import java.time.LocalDateTime;
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
@Table(name = "EXPENSE_ACCOUNT_TRANSFER")
public class RevenueAccountTransfer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    
    @Column(name = "REVENUE_ACCOUNT_ID")
    private int revenueAccountId;
    
    @Column(name = "CENTRAL_ACCOUNT_ID")
    private int centralAccountId;
    
    @Column(name = "MONEY_IN")
    private double moneyIn;
    
    @Column(name = "MONEY_OUT")
    private double moneyOut;
    
    @Column(name = "TRANSACTION_TIME")
    private LocalDateTime transactionTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRevenueAccountId() {
        return revenueAccountId;
    }

    public void setRevenueAccountId(int revenueAccountId) {
        this.revenueAccountId = revenueAccountId;
    }

    public int getCentralAccountId() {
        return centralAccountId;
    }

    public void setCentralAccountId(int centralAccountId) {
        this.centralAccountId = centralAccountId;
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

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(LocalDateTime transactionTime) {
        this.transactionTime = transactionTime;
    }
    
    
    
}
