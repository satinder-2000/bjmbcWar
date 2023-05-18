/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package bjm.bc.ejb;

import bjm.bc.ejb.exception.UserRegisteredAlreadyException;
import bjm.bc.model.RevenueAccount;
import bjm.bc.model.RevenueParty;
import bjm.bc.util.HashGenerator;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
@Stateless
public class RevenuePartyEjb implements RevenuePartyEjbLocal {
    
    private static Logger LOGGER = Logger.getLogger(RevenuePartyEjb.class.getName());

    @PersistenceContext(name = "bjmbcPU")
    private EntityManager em;
    
    @Inject
    RevenueAccountEjbLocal ral;
    
    @Inject
    EmailerEjbLocal eel; 

    @Override
    public RevenueParty createRevenueParty(RevenueParty revenueParty) throws UserRegisteredAlreadyException, MessagingException {
        String[] revAcctHashes = new String[revenueParty.getRevenueAccounts().size()];
        for (int i=0; i<revenueParty.getRevenueAccounts().size(); i++ ) {
		RevenueAccount ra = revenueParty.getRevenueAccounts().get(i);
		revAcctHashes[i]=ra.getRevenueAccountHash();
	}
        em.persist(revenueParty);
        em.flush();
        LOGGER.info(String.format("Revenue Party created with ID: %d",revenueParty.getId()));
        for(RevenueAccount ra : revenueParty.getRevenueAccounts()){
            ra.setRevenuePartyId(revenueParty.getId());
            ral.createRevenueAccount(ra);
        }
        LOGGER.info(String.format("%d Revenue Accounts updated with RevenueParty Id %d",revenueParty.getRevenueAccounts().size(),revenueParty.getId()));
        eel.sendRevenuePartyRegistrationEmail(revenueParty);
        return revenueParty;
    }

    @Override
    public RevenueParty findById(int id) {
        return em.find(RevenueParty.class, id);
    }
    
    @Override
    public boolean isEmailRegistered(String email) {
        TypedQuery<RevenueParty> tQ=em.createQuery("select rp from RevenueParty rp where rp.email=?1", RevenueParty.class);
        tQ.setParameter(1, email);
        try{
            tQ.getSingleResult();
            return true;
        }catch(NoResultException ex){
            //good for us
            return false;
        }
    }

    @Override
    public RevenueParty updateRevenueParty(RevenueParty revenueParty) {
        String partyHash=HashGenerator.generateHash(revenueParty.getName().concat(revenueParty.getEmail()).concat(revenueParty.getOwnerAdhaarNumber()));
        revenueParty.setPartyHash(partyHash);
        em.persist(revenueParty);
        LOGGER.info(String.format("Revenue Party with ID: %d updated",revenueParty.getId()));
        return revenueParty;
    }

    @Override
    public List<RevenueAccount> findRevenueAccountsOfParty(String email) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public RevenueParty addMoreRevenueAccounts(RevenueParty revenueParty, List<RevenueAccount> moreRevenueAccounts) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public RevenueParty findByEmail(String email) {
        TypedQuery<RevenueParty> tQ=em.createQuery("select rp from RevenueParty rp where rp.email=?1", RevenueParty.class);
        tQ.setParameter(1, email);
        return tQ.getSingleResult();
    }
}
