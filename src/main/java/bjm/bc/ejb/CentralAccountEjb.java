/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package bjm.bc.ejb;

import bjm.bc.model.CentralAccount;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author user
 */
@Stateless
public class CentralAccountEjb implements CentralAccountEjbLocal {
    
    private static Logger LOGGER = Logger.getLogger(CentralAccountEjb.class.getName());
    
    @PersistenceContext(name = "bjmbcPU")
    private EntityManager em;


    @Override
    public CentralAccount createAllocation(CentralAccount centralAccount) {
        em.persist(centralAccount);
        em.flush();
        LOGGER.info(String.format("CentralAccount record %d created",centralAccount.getId()));
        return centralAccount;
    }

    
}