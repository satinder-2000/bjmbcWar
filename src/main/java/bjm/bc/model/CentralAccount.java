package bjm.bc.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;



@Entity(name = "CENTRAL_ACCOUNT")
public class CentralAccount {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID")
	private int id;
	@Column(name="YEAR")
	private int year;
	@Column(name="REVENUE_ACCOUNT_HASH")
	private String revenueAccountHash;
	@Column(name="AMOUNT")
	private double amount;
	@Column(name="EXPENSE_ACCOUNT_HASH")
	private String expenseAccountHash;
	@Column(name="TRANSACTION_DATE")
	private LocalDateTime transactionDate;
	@Column(name="REVENUE_CATEGORY")
	private String revenueCategory;
	@Column(name="EXPENSE_CATEGORY")
	private String expenseCategory;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getRevenueAccountHash() {
		return revenueAccountHash;
	}
	public void setRevenueAccountHash(String revenueAccountHash) {
		this.revenueAccountHash = revenueAccountHash;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getExpenseAccountHash() {
		return expenseAccountHash;
	}
	public void setExpenseAccountHash(String expenseAccountHash) {
		this.expenseAccountHash = expenseAccountHash;
	}
	public LocalDateTime getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(LocalDateTime transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getRevenueCategory() {
		return revenueCategory;
	}
	public void setRevenueCategory(String revenueCategory) {
		this.revenueCategory = revenueCategory;
	}
	public String getExpenseCategory() {
		return expenseCategory;
	}
	public void setExpenseCategory(String expenseCategory) {
		this.expenseCategory = expenseCategory;
	}
	
}