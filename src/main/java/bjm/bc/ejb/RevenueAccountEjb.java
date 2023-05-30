/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package bjm.bc.ejb;

import bjm.bc.model.RevenueAccount;
import bjm.bc.model.RevenueAccountTransaction;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.logging.Logger;
import javax.persistence.TypedQuery;

/**
 *
 * @author user
 */
@Stateless
public class RevenueAccountEjb implements RevenueAccountEjbLocal {
    
    private static Logger LOGGER = Logger.getLogger(RevenueAccountEjb.class.getName());
    
    @PersistenceContext(name = "bjmbcPU")
    private EntityManager em;
    
    @Inject
    private RevenuePartyEjbLocal rpl;
    
    @Inject
    private EmailerEjbLocal eMailer;

    @Override
    public RevenueAccount createRevenueAccount(RevenueAccount revenueAccount) {
        em.persist(revenueAccount);
        em.flush();
        LOGGER.info("RevenueAccount record persisted with ID: "+revenueAccount.getId());
        return revenueAccount;
    }

    @Override
    public RevenueAccount findById(int id) {
        return em.find(RevenueAccount.class, id);
    }

    @Override
    public RevenueAccount saveRevenueAccount(RevenueAccount revenueAccount) {
        em.merge(revenueAccount);
        em.flush();
        LOGGER.info("ExpenseAccount record saved with ID: "+revenueAccount.getId());
        return revenueAccount;
    }

    @Override
    public boolean addToBalanceRevenueAccount(int accountId, double balanceToAdd) {
        RevenueAccount rA=findById(accountId);
        /*rA.setBalance(rA.getBalance()+balanceToAdd);
        rA = saveRevenueAccount(rA);
        LOGGER.info(String.format("Revenue Account {1} new Balance is {2}", accountId, rA.getBalance()));
        */
        return true;
    }

    @Override
    public boolean createMoneyInRevenueAccount(RevenueAccountTransaction revenueAccountTransaction) {
        em.persist(revenueAccountTransaction);
        em.flush();
        LOGGER.info(String.format("RevenueAccountTransaction record saved with ID: %d",+revenueAccountTransaction.getId()));
        return true;
    }

    @Override
    public boolean createMoneyOutRevenueAccount(RevenueAccountTransaction revenueAccountTransaction) {
        em.persist(revenueAccountTransaction);
        em.flush();
        LOGGER.info(String.format("RevenueAccountTransaction record saved with ID: %d",+revenueAccountTransaction.getId()));
        return true;
    }

    @Override
    public List<RevenueAccountTransaction> getRevenueAccountTransactions(int accountId, int year) {
        TypedQuery<RevenueAccountTransaction> tQ =em.createQuery("select rat from RevenueAccountTransaction rat where rat.revenueAccountId=?1 and rat.year=?2", RevenueAccountTransaction.class);
        tQ.setParameter(1, accountId);
        tQ.setParameter(2, year);
        List<RevenueAccountTransaction> revAcctTxs= tQ.getResultList();
        return revAcctTxs;
    }

}
