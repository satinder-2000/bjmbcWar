/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package bjm.bc.ejb;

import bjm.bc.model.CentralAccount;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface CentralAccountEjbLocal {
    
    public CentralAccount createAllocation(CentralAccount centralAccount);
    public List<CentralAccount> getAllForYear(int year); 
    public CentralAccount saveAllocation(CentralAccount ca);
    
}
