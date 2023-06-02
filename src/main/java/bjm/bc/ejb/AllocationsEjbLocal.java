/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package bjm.bc.ejb;

import java.util.concurrent.Future;
import javax.ejb.AccessTimeout;
import javax.ejb.Asynchronous;
import javax.ejb.Local;
import javax.ejb.Lock;
import javax.ejb.LockType;

/**
 *
 * @author user
 */
@Local
public interface AllocationsEjbLocal {
    
    @Asynchronous
    @Lock(LockType.READ)
    @AccessTimeout(-1)
    public Future<String> performAllocations(String allocationJob);
    
}
