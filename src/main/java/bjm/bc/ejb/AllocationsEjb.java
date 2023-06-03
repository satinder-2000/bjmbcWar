/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package bjm.bc.ejb;

import bjm.bc.model.ExpenseAccount;
import bjm.bc.model.RevenueAccount;
import bjm.bc.model.RevenueAllocation;
import bjm.bc.model.RevenueCategory;
import bjm.bc.util.FinancialYear;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    @Inject
    private ExpenseAllocationEjbLocal expenseAllocationEjbLocal;
    @Inject
    private RevenueAllocationEjbLocal revenueAllocationEjbLocal;
    @Inject
    private RevenueCategoryEjbLocal revenueCategoryEjbLocal;

    private CentralAccountEjbLocal centralAccountEjbLocal;

    @Override
    public Future<String> performAllocations(String allocationJob) {

        int year = FinancialYear.financialYear();
        List<RevenueAccount> revenueAccounts = revenueAccountEjbLocal.getAll(year);
        List<RevenueAllocation> revenueAllocations = revenueAllocationEjbLocal.getAllocationsForYear(year);
        Map<String, Integer> revAcctCt = new HashMap<>();
        Map<String, Double> revCatAllocs = new HashMap<>();
        //Store the count of each RevenueAccount category Id
        for (RevenueAccount ra : revenueAccounts) {
            Integer revCatId = ra.getRevenueCategoryId();
            RevenueCategory revCat = revenueCategoryEjbLocal.findById(revCatId);
            //Is there any value in the Map for this revCat
            String revCatStr = revCat.getRevenueCategory();
            Integer acctCt = revAcctCt.get(revCatStr);
            if (acctCt == null) {
                revAcctCt.put(revCatStr, 1);
            } else {
                revAcctCt.put(revCatStr, revAcctCt.get(revCatStr) + 1);
            }
        }
        //At this stage the revAcctCt Map should be all populated. Let's print the values
        Set<String> keys = revAcctCt.keySet();
        Iterator<String> keysItr = keys.iterator();
        while (keysItr.hasNext()) {
            String revCatStrKey = keysItr.next();
            LOGGER.info(String.format("RevenueCategory %s has %d accounts", revCatStrKey, revAcctCt.get(revCatStrKey)));
        }
        //Store the allocated amount for each RevenueCategory
        for(RevenueAllocation rAloc : revenueAllocations){
            String revCatStr=rAloc.getCategory();
            //Do we have mapping of this recCatStr in revCatAllocs Map
            Double revCatAlloc=revCatAllocs.get(revCatStr);
            if (revCatAlloc==null){
                revCatAllocs.put(revCatStr, rAloc.getAllocation());
            }
        }
        //At this stage the revCatAllocs Map should be all populated. Let's print the values
        Set<String> keysAlloc = revCatAllocs.keySet();
        Iterator<String> keysItrAlloc = keysAlloc.iterator();
        while (keysItrAlloc.hasNext()) {
            String revCatAllocStrKey = keysItrAlloc.next();
            Double allocationVal=revCatAllocs.get(revCatAllocStrKey);
            LOGGER.info(String.format("RevenueAllocation category %s has %.2f allocation", revCatAllocStrKey, revCatAllocs.get(revCatAllocStrKey)));
        }
        for (RevenueAllocation ral : revenueAllocations) {
            String revCat = ral.getCategory();
            double revCatAlloc = revCatAllocs.get(revCat);
            double revCatAllocPerAcct = revCatAlloc / revAcctCt.get(revCat);
            List<RevenueAccount> revenueAccountsOfCat = revenueAccountEjbLocal.getAllByCategoryAndYear(revCat, year);
            for (RevenueAccount raOfCat : revenueAccountsOfCat) {
                raOfCat.setYtdBalance(revCatAllocPerAcct);
                revenueAccountEjbLocal.saveRevenueAccount(raOfCat);
            }
        }
        return new AsyncResult<String>(allocationJob);
    }
    
}
