package bjm.bc.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name="REVENUE_ACCOUNT")
public class RevenueAccount {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID")
	private int id;
	@Column(name="YEAR")
	private int year;
	@Column(name="REVENUE_PARTY_ID")
	private int revenuePartyId;
	@Column(name="BALANCE")
	private double balance;
	@Column(name="REVENUE_ACCOUNT_HASH")
	private String revenueAccountHash;
	@Column(name="REVENUE_CATEGORY")
	private String revenueCategory;
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
	
	public int getRevenuePartyId() {
		return revenuePartyId;
	}
	public void setRevenuePartyId(int revenuePartyId) {
		this.revenuePartyId = revenuePartyId;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public String getRevenueAccountHash() {
		return revenueAccountHash;
	}
	public void setRevenueAccountHash(String revenueAccountHash) {
		this.revenueAccountHash = revenueAccountHash;
	}
	public String getRevenueCategory() {
		return revenueCategory;
	}
	public void setRevenueCategory(String revenueCategory) {
		this.revenueCategory = revenueCategory;
	}
	public LocalDateTime getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}
	
}