/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package bjm.bc.ejb;

import bjm.bc.model.RevenueCategory;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
@Stateless
public class RevenueCategoryEjb implements RevenueCategoryEjbLocal {
    
    private static Logger LOGGER = Logger.getLogger(RevenueCategoryEjb.class.getName());
    
    @PersistenceContext(name = "bjmbcPU")
    private EntityManager em;

    @Override
    public List<RevenueCategory> getRevenueCategoriesForYear(int year) {
        TypedQuery<RevenueCategory> tQ = em.createQuery("select rc from RevenueCategory rc where rc.year=?1", RevenueCategory.class);
        tQ.setParameter(1, year);
        List<RevenueCategory> revenueCategories = tQ.getResultList();
        LOGGER.info(String.format("Total Revenue Categories for year: {1} are {2}", year, revenueCategories.size()));
        return revenueCategories;
    }
}
