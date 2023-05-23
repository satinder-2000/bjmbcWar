/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package bjm.bc.ejb;

import bjm.bc.ejb.exception.UserRegisteredAlreadyException;
import bjm.bc.model.Access;
import bjm.bc.model.ExpenseAccount;
import bjm.bc.model.ExpenseParty;
import bjm.bc.util.HashGenerator;
import java.sql.Timestamp;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.NoResultException;

/**
 *
 * @author user
 */
@Stateless
public class ExpensePartyEjb implements ExpensePartyEjbLocal {
    
    private static Logger LOGGER = Logger.getLogger(ExpensePartyEjb.class.getName());

    @PersistenceContext(name = "bjmbcPU")
    private EntityManager em;
    
    @Inject
    ExpenseAccountEjbLocal eal;
    
    @Inject
    EmailerEjbLocal eel;
    
    @Inject
    private AccessEjbLocal ael;
    
    
    
    @Override
    public ExpenseParty findById(int id) {
        //return em.find(ExpenseParty.class, id);
        return em.find(ExpenseParty.class, id);
    }

    @Override
    public ExpenseParty createExpenseParty(ExpenseParty expenseParty) throws UserRegisteredAlreadyException, MessagingException {
        String[] expAcctHashes = new String[expenseParty.getExpenseAccounts().size()];
        for (int i=0; i<expenseParty.getExpenseAccounts().size(); i++ ) {
		ExpenseAccount ea = expenseParty.getExpenseAccounts().get(i);
		expAcctHashes[i]=ea.getExpenseAccountHash();
	}
        em.persist(expenseParty);
        em.flush();
        LOGGER.info(String.format("Expense Party created with ID: %d",expenseParty.getId()));
        for(ExpenseAccount ea : expenseParty.getExpenseAccounts()){
            ea.setExpensePartyId(expenseParty.getId());
            ea.setCreatedOn(new Timestamp(System.currentTimeMillis()));
            eal.saveExpenseAccount(ea);
        }
        LOGGER.info(String.format("%d Expense Accounts updated with ExpenseParty Id %d",expenseParty.getExpenseAccounts().size(),expenseParty.getId()));
        //Create Acccess record now.
        Access access= ael.createExpensePartyAccess(expenseParty);
        LOGGER.info(String.format("Access ID for the Expense Party %d is %d",expenseParty.getId(),access.getId()));
        eel.sendExpensePartyRegistrationEmail(expenseParty);
        
        return expenseParty;
    }

    @Override
    public ExpenseParty updateExpenseParty(ExpenseParty expenseParty) {
        String partyHash=HashGenerator.generateHash(expenseParty.getName().concat(expenseParty.getEmail()).concat(expenseParty.getOwnerAdhaarNumber()));
        expenseParty.setPartyHash(partyHash);
        em.persist(expenseParty);
        LOGGER.info(String.format("Expense Party with ID: %d updated",expenseParty.getId()));
        return expenseParty;
        
    }

    @Override
    public List<ExpenseAccount> findExpenseAccountsOfParty(String email) {
        TypedQuery<ExpenseParty> tQ = em.createQuery("select ep from ExpenseParty ep where ep.email =?1", ExpenseParty.class);
        tQ.setParameter(1, email);
        ExpenseParty ep = tQ.getSingleResult();
        List<ExpenseAccount> expenseAccounts= ep.getExpenseAccounts();
        LOGGER.info(String.format("Expense Party %s had %d Expense Accounts",email, expenseAccounts.size()));
        return expenseAccounts;
    }

    @Override
    public ExpenseParty addMoreExpenseAccounts(ExpenseParty expenseParty, List<ExpenseAccount> moreExpenseAccounts) {
        expenseParty.getExpenseAccounts().addAll(moreExpenseAccounts);
        for(ExpenseAccount ea: moreExpenseAccounts){
            ea.setExpensePartyId(expenseParty.getId());
            em.persist(ea);
        }
        em.persist(expenseParty);
        return expenseParty;
    }

    @Override
    public ExpenseParty findByEmail(String email) {
        TypedQuery<ExpenseParty> tQ=em.createQuery("select ep from ExpenseParty ep where ep.email=?1", ExpenseParty.class);
        tQ.setParameter(1, email);
        return tQ.getSingleResult();
    }

    @Override
    public boolean isEmailRegistered(String email) {
        TypedQuery<ExpenseParty> tQ=em.createQuery("select ep from ExpenseParty ep where ep.email=?1", ExpenseParty.class);
        tQ.setParameter(1, email);
        try{
            tQ.getSingleResult();
            return true;
        }catch(NoResultException ex){
            //good for us
            return false;
        }
    }
}