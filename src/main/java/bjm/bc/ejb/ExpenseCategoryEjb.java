/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package bjm.bc.ejb;

import bjm.bc.model.ExpenseCategory;
import java.util.Collection;
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
public class ExpenseCategoryEjb implements ExpenseCategoryEjbLocal {
    
    private static Logger LOGGER = Logger.getLogger(ExpenseCategoryEjb.class.getName());
    
    @PersistenceContext(name = "bjmbcPU")
    private EntityManager em;

    @Override
    public List<ExpenseCategory> getExpenseCategories() {
        TypedQuery<ExpenseCategory> tQ = em.createQuery("select ec from ExpenseCategory ec", ExpenseCategory.class);
        List<ExpenseCategory> expenseCategories = tQ.getResultList();
        LOGGER.info("Total Expense Categories are: "+expenseCategories.size());
        return expenseCategories;
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public List<ExpenseCategory> getExpenseCategoriesForYear(int year) {
        TypedQuery<ExpenseCategory> tQ = em.createQuery("select ec from ExpenseCategory ec where ec.year=?1", ExpenseCategory.class);
        tQ.setParameter(1, year);
        List<ExpenseCategory> expenseCategories = tQ.getResultList();
        LOGGER.info(String.format("Total Expense Categories for year: %d are %d", year, expenseCategories.size()));
        return expenseCategories;
        
    }

    @Override
    public ExpenseCategory findByNameAndYear(String expCat, int year) {
        TypedQuery<ExpenseCategory> tQ = em.createQuery("select ec from ExpenseCategory ec where ec.expenseCategory=?1 and ec.year=?2", ExpenseCategory.class);
        tQ.setParameter(1, expCat);
        tQ.setParameter(2, year);
        return tQ.getSingleResult();
    }

    @Override
    public ExpenseCategory findById(Integer expCatId) {
        TypedQuery<ExpenseCategory> tQ = em.createQuery("select ec from ExpenseCategory ec where ec.id=?1", ExpenseCategory.class);
        tQ.setParameter(1, expCatId);
        return tQ.getSingleResult();
    }
}
