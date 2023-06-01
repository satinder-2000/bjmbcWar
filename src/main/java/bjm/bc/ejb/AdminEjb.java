/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package bjm.bc.ejb;

import bjm.bc.model.Admin;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author user
 */
@Stateless
public class AdminEjb implements AdminEjbLocal {
    
    private static Logger LOGGER = Logger.getLogger(AdminEjb.class.getName());
    
    @PersistenceContext(name = "bjmbcPU")
    private EntityManager em;

    @Override
    public Admin createAdmin(Admin admin) {
        em.persist(admin);
        em.flush();
        LOGGER.info("Admin persisted in database");
        return admin;
    }

    @Override
    public Admin findAdmin() {
        Admin admin=null;
        TypedQuery<Admin> tQ=em.createQuery("select a from Admin a", Admin.class);
        try{
            admin=tQ.getSingleResult();
        }catch(NoResultException ex){
            admin=null; //good for us
        }
        return admin; 
    }

    @Override
    public Admin changeAdminPassword(Admin admin) {
        em.merge(admin);
        em.flush();
        LOGGER.info("Admin saved in database");
        return admin;
        
    }
}
