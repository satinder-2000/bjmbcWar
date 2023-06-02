/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package bjm.bc.ejb;

import bjm.bc.model.ExpenseAccount;
import bjm.bc.model.RevenueAccount;
import bjm.bc.util.FinancialYear;
import java.util.List;
import java.util.concurrent.Future;
import java.util.logging.Logger;
import javax.ejb.AsyncResult;
import javax.ejb.Singleton;
import javax.inject.Inject;

/**
 *
 * @author user
 */
@Singleton
public class AllocationsEjb implements AllocationsEjbLocal {
    
    private static Logger LOGGER = Logger.getLogger(AllocationsEjb.class.getName());
    
    @Inject
    private ExpenseAccountEjbLocal expenseAccountEjbLocal;
    @Inject
    private RevenueAccountEjbLocal revenueAccountEjbLocal;
    
    private CentralAccountEjbLocal centralAccountEjbLocal;

    @Override
    public Future<String> performAllocations(String allocationJob) {
        
        int year = FinancialYear.financialYear();
        List<RevenueAccount> revenueAccounts = revenueAccountEjbLocal.getAll(year);
        List<ExpenseAccount> expenseAccounts = expenseAccountEjbLocal.getAll(year);
        //We need to a CentralAccount record at unit level
        
        
        return new AsyncResult<String>(allocationJob);
        
    }
}
