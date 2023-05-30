/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package bjm.bc.ejb;

import bjm.bc.model.ExpenseAccount;
import bjm.bc.model.ExpenseAccountTransaction;
import bjm.bc.model.ExpenseParty;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
@Stateless
public class ExpenseAccountEjb implements ExpenseAccountEjbLocal {
    
    private static Logger LOGGER = Logger.getLogger(ExpenseAccountEjb.class.getName());
    
    @PersistenceContext(name = "bjmbcPU")
    private EntityManager em;
    
    @Inject
    private ExpensePartyEjbLocal epl;
    
    @Inject
    private EmailerEjbLocal eMailer;

    @Override
    public ExpenseAccount createExpenseAccount(ExpenseAccount expenseAccount){
        em.persist(expenseAccount);
        em.flush();
        LOGGER.info("ExpenseAccount record persisted with ID: "+expenseAccount.getId());
        return expenseAccount;
    }

    @Override
    public ExpenseAccount findById(int id) {
        return em.find(ExpenseAccount.class, id);
    }

    @Override
    public ExpenseAccount saveExpenseAccount(ExpenseAccount expenseAccount) {
        em.merge(expenseAccount);
        em.flush();
        LOGGER.info("ExpenseAccount record saved with ID: "+expenseAccount.getId());
        return expenseAccount;
    }

    @Override
    public boolean addToBalanceExpenseAccount(int accountId, double balanceToAdd) {
        ExpenseAccount eA=findById(accountId);
        //eA.setBalance(eA.getBalance()+balanceToAdd);
        eA = saveExpenseAccount(eA);
        //LOGGER.info(String.format("Expense Account {1} new Balance is {2}", accountId, eA.getBalance()));
        return true;
    }

    @Override
    public boolean withdrawFromBalanceExpenseAccount(int accountId, double balanceToWithdraw) {
        ExpenseAccount eA=findById(accountId);
        /*eA.setBalance(eA.getBalance()-balanceToWithdraw);
        if (eA.getBalance()<0){
            int expPartyId = eA.getExpensePartyId();
            ExpenseParty ep=epl.findById(expPartyId);
            try {
                eMailer.sendExpensePartyAccountOverdrawnEmail(ep);
            } catch (MessagingException ex) {
                Logger.getLogger(ExpenseAccountEjb.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/
        em.persist(eA);
        return true;
    }

    @Override
    public boolean createMoneyOutExpenseAccount(ExpenseAccountTransaction expenseAccountTransaction) {
        em.persist(expenseAccountTransaction);
        em.flush();
        LOGGER.info(String.format("ExpenseAccountTransaction record saved with ID: %d",+expenseAccountTransaction.getId()));
        return true;
    }
    
    
    
    
    
    
}
