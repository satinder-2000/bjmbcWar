/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package bjm.bc.ejb;

import bjm.bc.ejb.exception.UserRegisteredAlreadyException;
import bjm.bc.model.ExpenseAccount;
import bjm.bc.model.ExpenseParty;
import jakarta.ejb.Local;
import jakarta.mail.MessagingException;
import java.util.List;

/**
 *
 * @author user
 */
@Local
public interface ExpensePartyEjbLocal {
    
    public ExpenseParty createExpenseParty(ExpenseParty expenseParty) throws UserRegisteredAlreadyException, MessagingException;
    public ExpenseParty findById(int id);
    public ExpenseParty updateExpenseParty(ExpenseParty expenseParty);
    public List<ExpenseAccount> findExpenseAccountsOfParty(String email);
    public ExpenseParty addMoreExpenseAccounts(ExpenseParty expenseParty, List<ExpenseAccount> moreExpenseAccounts); 
    
}
