/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package bjm.bc.ejb;

import bjm.bc.model.ExpenseAllocation;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface ExpenseAllocationEjbLocal {
    
    public List<ExpenseAllocation> getAllocationsForYear(int year);
    
}
