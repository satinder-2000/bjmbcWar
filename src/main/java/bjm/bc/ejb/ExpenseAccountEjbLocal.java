/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package bjm.bc.ejb;

import bjm.bc.model.ExpenseAccount;
import bjm.bc.model.ExpenseAccountTransaction;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface ExpenseAccountEjbLocal {
    
    public ExpenseAccount createExpenseAccount(ExpenseAccount expenseAccount);
    public ExpenseAccount findById(int id);
    public ExpenseAccount saveExpenseAccount(ExpenseAccount expenseAccount);
    public boolean addToBalanceExpenseAccount(int accountId, double balanceToAdd);
    public boolean withdrawFromBalanceExpenseAccount(int accountId, double balanceToWithdraw);
    public boolean createMoneyOutExpenseAccount(ExpenseAccountTransaction expenseAccountTransaction);
    public boolean createMoneyInRevenueAccount(ExpenseAccountTransaction eat);
    public List<ExpenseAccountTransaction> getExpenseAccountTransactions(int accountId, int year);
    public List<ExpenseAccount> getAll(int year);
    
}
