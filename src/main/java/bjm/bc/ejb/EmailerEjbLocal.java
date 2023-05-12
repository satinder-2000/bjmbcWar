/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package bjm.bc.ejb;

import bjm.bc.model.ExpenseParty;
import bjm.bc.model.RevenueParty;
import jakarta.ejb.Local;
import jakarta.mail.MessagingException;

/**
 *
 * @author user
 */
@Local
public interface EmailerEjbLocal {
    
    public void sendRevenuePartyRegistrationEmail(RevenueParty rp);
    
    public void sendExpensePartyRegistrationEmail(ExpenseParty ep);
    
    public void sendAccessCreatedEmail(String email);
    
    public void sendExpensePartyAccountOverdrawnEmail(ExpenseParty ep) throws MessagingException;

}
