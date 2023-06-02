/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package bjm.bc.ejb;

import bjm.bc.model.ExpenseAllocation;
import bjm.bc.model.RevenueAllocation;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author user
 */
@Stateless
public class ExpenseAllocationEjb implements ExpenseAllocationEjbLocal {
    
    private static Logger LOGGER = Logger.getLogger(ExpenseAllocationEjb.class.getName());
    
    @PersistenceContext(name = "bjmbcPU")
    private EntityManager em;

    @Override
    public List<ExpenseAllocation> getAllocationsForYear(int year) {
        TypedQuery<ExpenseAllocation> tQ =em.createQuery("select eal from ExpenseAllocation eal where eal.year=?1", ExpenseAllocation.class);
        tQ.setParameter(1, year);
        List<ExpenseAllocation> expAllocs = tQ.getResultList();
        LOGGER.info(String.format("ExpenseAllocation %d extracted for year %d",expAllocs.size(),year));
        return expAllocs;
    }
}
