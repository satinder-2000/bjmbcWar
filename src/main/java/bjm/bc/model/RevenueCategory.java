package bjm.bc.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "REVENUE_CATEGORY")
@Table(name = "REVENUE_CATEGORY")
public class RevenueCategory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID")
	private int id;
	@Column(name="YEAR")
	private int year;
	@Column(name = "REVENUE_CATEGORY")
	private String revenueCategory;
	
	
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
	public String getRevenueCategory() {
		return revenueCategory;
	}
	public void setRevenueCategory(String revenueCategory) {
		this.revenueCategory = revenueCategory;
	}
}