/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package bjm.bc.ejb;

import bjm.bc.model.Access;
import bjm.bc.model.ExpenseParty;
import bjm.bc.model.RevenueParty;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AccessEjbLocal {
    
    static final int MAX_PERMITTED_FAILED_ATTEMPTS =3;
    
    public Access createAccess(Access access);
    
    public Access createRevenuePartyAccess(RevenueParty revenueParty);
    
    public Access updateAccess(Access access);
    
    public Access lockAccess(Access access);
    
    public Access unLockAccess(Access access);
    
    public Access findByEmail(String email);
    
    public Access findByEmailAndAccessType(String email, String accessType);
    
    public Access increaseFailedLoginnAttempt(Access access);

    public Access createExpensePartyAccess(ExpenseParty expenseParty);
    
    
}
