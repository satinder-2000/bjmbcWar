/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package bjm.bc.ejb;

import bjm.bc.model.ExpenseCategory;
import jakarta.ejb.Local;
import java.util.List;

/**
 *
 * @author user
 */
@Local
public interface ExpenseCategoryEjbLocal {
    
    public List<ExpenseCategory> getExpenseCategories();
    
    public List<ExpenseCategory> getExpenseCategoriesForYear(int year);
    
}
