/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package bjm.bc.ejb;

import bjm.bc.ejb.exception.UserRegisteredAlreadyException;
import bjm.bc.model.RevenueAccount;
import bjm.bc.model.RevenueParty;
import javax.ejb.Local;
import javax.mail.MessagingException;
import java.util.List;

/**
 *
 * @author user
 */
@Local
public interface RevenuePartyEjbLocal {
    
    public RevenueParty createRevenueParty(RevenueParty revenueParty) throws UserRegisteredAlreadyException, MessagingException;
    public RevenueParty findById(int id);
    public RevenueParty findByEmail(String email);
    public boolean isEmailRegistered(String email);
    public RevenueParty updateRevenueParty(RevenueParty revenueParty);
    public List<RevenueAccount> findRevenueAccountsOfParty(String email);
    public RevenueParty addMoreRevenueAccounts(RevenueParty revenueParty, List<RevenueAccount> moreRevenueAccounts);
    
}
