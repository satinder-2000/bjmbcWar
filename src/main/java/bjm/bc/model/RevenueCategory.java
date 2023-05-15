package bjm.bc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "REVENUE_CATEGORY")
public class RevenueCategory implements Serializable {
	
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