package bjm.bc.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity(name = "REVENUE_ACCOUNT")
public class RevenueAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    @Column(name = "REVENUE_PARTY_ID")
    private int revenuePartyId;
    @Column(name = "REVENUE_ACCOUNT_HASH")
    private String revenueAccountHash;
    @Column(name = "REVENUE_CATEGORY_ID")
    private int revenueCategoryId;
    @Column(name = "NAME")
    private String name;
    @Column(name="CREATED_ON")
    private Timestamp createdOn;
    @Column(name = "YEAR")
    private int year;
    @Column(name="YTD_BALANCE")
    private double ytdBalance;
    @Transient
    private double moneyIn;
    @Transient
    private double moneyOut;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRevenuePartyId() {
        return revenuePartyId;
    }

    public void setRevenuePartyId(int revenuePartyId) {
        this.revenuePartyId = revenuePartyId;
    }

    

    public String getRevenueAccountHash() {
        return revenueAccountHash;
    }

    public void setRevenueAccountHash(String revenueAccountHash) {
        this.revenueAccountHash = revenueAccountHash;
    }

    public int getRevenueCategoryId() {
        return revenueCategoryId;
    }

    public void setRevenueCategoryId(int revenueCategoryId) {
        this.revenueCategoryId = revenueCategoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    public double getYtdBalance() {
        return ytdBalance;
    }

    public void setYtdBalance(double ytdBalance) {
        this.ytdBalance = ytdBalance;
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
    
 }
