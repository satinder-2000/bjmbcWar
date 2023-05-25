/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bjm.bc.model;

import java.sql.Timestamp;
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
@Table(name = "REVENUE_ACCOUNT_TRANSACTION")
public class RevenueAccountTransaction {
    
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
    private double ytdBalance;
    @Column(name = "REVENUE_ACCOUNT_ID")
    private int revenueAccountId;
    @Column(name = "CREATED_ON")
    private Timestamp createdOn;

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

    public double getYtdBalance() {
        return ytdBalance;
    }

    public void setYtdBalance(double ytdBalance) {
        this.ytdBalance = ytdBalance;
    }

    public int getRevenueAccountId() {
        return revenueAccountId;
    }

    public void setRevenueAccountId(int revenueAccountId) {
        this.revenueAccountId = revenueAccountId;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }
    
    
    
}
