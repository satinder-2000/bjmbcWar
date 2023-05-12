package bjm.bc.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "EXPENSE_ACCOUNT")
public class ExpenseAccount {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID")
	private int id;
	@Column(name="YEAR")
	private int year;
	@Column(name="BALANCE")
	private double balance;
	@Column(name="EXPENSE_ACCOUNT_HASH")
	private String expenseAccountHash;
	@Column(name="EXPENSE_CATEGORY")
	private String expenseCategory;
	@Column(name="EXPENSE_PARTY_ID")
	private int expensePartyId;
	@Column(name="CREATED_ON")
	private LocalDateTime createdOn;
	
	
	
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
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public String getExpenseAccountHash() {
		return expenseAccountHash;
	}
	public void setExpenseAccountHash(String expenseAccountHash) {
		this.expenseAccountHash = expenseAccountHash;
	}
	public String getExpenseCategory() {
		return expenseCategory;
	}
	public void setExpenseCategory(String expenseCategory) {
		this.expenseCategory = expenseCategory;
	}
	
	public int getExpensePartyId() {
		return expensePartyId;
	}
	public void setExpensePartyId(int expensePartyId) {
		this.expensePartyId = expensePartyId;
	}
	public LocalDateTime getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}
	
}