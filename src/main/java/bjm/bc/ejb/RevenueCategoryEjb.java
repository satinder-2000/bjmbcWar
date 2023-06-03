/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package bjm.bc.ejb;

import bjm.bc.model.RevenueCategory;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
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
        LOGGER.info(String.format("Total Revenue Categories for year: %d are %d", year, revenueCategories.size()));
        return revenueCategories;
    }

    @Override
    public RevenueCategory findByNameAndYear(String revCat, int year) {
        TypedQuery<RevenueCategory> tQ = em.createQuery("select rc from RevenueCategory rc where rc.revenueCategory=?1 and rc.year=?2", RevenueCategory.class);
        tQ.setParameter(1, revCat);
        tQ.setParameter(2, year);
        return tQ.getSingleResult();
    }

    @Override
    public RevenueCategory findById(Integer revCatId) {
        TypedQuery<RevenueCategory> tQ = em.createQuery("select rc from RevenueCategory rc where rc.id=?1", RevenueCategory.class);
        tQ.setParameter(1, revCatId);
        return tQ.getSingleResult();
        
    }
}
