package bjm.bc.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity(name = "EXPENSE_ACCOUNT")
public class ExpenseAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "EXPENSE_ACCOUNT_HASH")
    private String expenseAccountHash;
    @Column(name = "EXPENSE_CATEGORY_ID")
    private int expenseCategoryId;
    @Column(name = "EXPENSE_PARTY_ID")
    private int expensePartyId;
    @Column(name = "CREATED_ON")
    private Timestamp createdOn;
    @Column(name = "YTD_BALANCE")
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

    public String getExpenseAccountHash() {
        return expenseAccountHash;
    }

    public void setExpenseAccountHash(String expenseAccountHash) {
        this.expenseAccountHash = expenseAccountHash;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getExpenseCategoryId() {
        return expenseCategoryId;
    }

    public void setExpenseCategoryId(int expenseCategoryId) {
        this.expenseCategoryId = expenseCategoryId;
    }

    public int getExpensePartyId() {
        return expensePartyId;
    }

    public void setExpensePartyId(int expensePartyId) {
        this.expensePartyId = expensePartyId;
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

    
    

}
