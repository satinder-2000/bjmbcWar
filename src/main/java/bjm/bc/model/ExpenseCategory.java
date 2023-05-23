package bjm.bc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "EXPENSE_CATEGORY")
public class ExpenseCategory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID")
	private int id;
	@Column(name = "EXPENSE_CATEGORY")
	private String expenseCategory;
	@Column(name="YEAR")
	private int year;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public String getExpenseCategory() {
		return expenseCategory;
	}

	public void setExpenseCategory(String expenseCategory) {
		this.expenseCategory = expenseCategory;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

}