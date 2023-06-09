/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package bjm.bc.ejb;

import bjm.bc.model.CentralAccount;
import bjm.bc.model.ExpenseAccount;
import bjm.bc.model.ExpenseAllocation;
import bjm.bc.model.ExpenseCategory;
import bjm.bc.model.RevenueAccount;
import bjm.bc.model.RevenueAllocation;
import bjm.bc.model.RevenueCategory;
import bjm.bc.util.FinancialYear;
import java.time.LocalDateTime;
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
import java.math.BigDecimal;

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
    @Inject
    private ExpenseCategoryEjbLocal expenseCategoryEjbLocal;
    @Inject
    private CentralAccountEjbLocal centralAccountEjbLocal;
    
    private static final int BUDGET_FUNDS=10000000;

    @Override
    public Future<String> performAllocations(String allocationJob) {

        int year = FinancialYear.financialYear();
        List<RevenueAccount> revenueAccounts = revenueAccountEjbLocal.getAll(year);
        List<RevenueAllocation> revenueAllocations = revenueAllocationEjbLocal.getAllocationsForYear(year);
        Map<String, Integer> revAcctCt = new HashMap<>();
        Map<String, BigDecimal> revCatAllocs = new HashMap<>();
        Map<String, BigDecimal> expCatAllocs = new HashMap<>();
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
            BigDecimal revCatAlloc=revCatAllocs.get(revCatStr);
            if (revCatAlloc==null){
                revCatAllocs.put(revCatStr, new BigDecimal(rAloc.getAllocation()));
            }
        }
        //At this stage the revCatAllocs Map should be all populated. Let's print the values
        Set<String> keysAlloc = revCatAllocs.keySet();
        Iterator<String> keysItrAlloc = keysAlloc.iterator();
        while (keysItrAlloc.hasNext()) {
            String revCatAllocStrKey = keysItrAlloc.next();
            BigDecimal allocationVal=revCatAllocs.get(revCatAllocStrKey);
            LOGGER.info(String.format("RevenueAllocation category %s has %.2f allocation", revCatAllocStrKey, revCatAllocs.get(revCatAllocStrKey)));
        }
        //And merge RevenueAccount(s) with the DB
        for (RevenueAllocation ral : revenueAllocations) {
            String revCat = ral.getCategory();
            BigDecimal revCatAlloc = revCatAllocs.get(revCat);
            BigDecimal revCatAllocPerAcct = revCatAlloc.divide(new BigDecimal(revAcctCt.get(revCat)));
            List<RevenueAccount> revenueAccountsOfCat = revenueAccountEjbLocal.getAllByCategoryAndYear(revCat, year);
            for (RevenueAccount raOfCat : revenueAccountsOfCat) {
                raOfCat.setYtdBalance(revCatAllocPerAcct.toString());
                revenueAccountEjbLocal.saveRevenueAccount(raOfCat);
            }
        }
        //Lets load the merged RevenueAccount(s) to poputate CentralAccount(s)
        revenueAccounts = revenueAccountEjbLocal.getAll(year);
        for (RevenueAccount ra : revenueAccounts) {
            while (new BigDecimal(ra.getYtdBalance()).compareTo(new BigDecimal("0")) == 1) {
                if (new BigDecimal(ra.getYtdBalance()).compareTo(new BigDecimal("1000")) == 1) {
                    CentralAccount ca = new CentralAccount();
                    ca.setYear(FinancialYear.financialYear());
                    ca.setRevenueAccountHash(ra.getRevenueAccountHash());
                    ca.setAmount("10");
                    ca.setAccountName(ra.getName() + " to ");
                    ca.setTransactionDate(LocalDateTime.now());
                    //Will set other fields such as Timestamp and Account Name (append Expense Account Name0 when we popuate the Expense records
                    ra.setYtdBalance(new BigDecimal(ra.getYtdBalance()).subtract(new BigDecimal("1000")).toString());
                    ca = centralAccountEjbLocal.createAllocation(ca);
                } else {
                    BigDecimal amt = new BigDecimal(ra.getYtdBalance());//should be < 1000
                    
                    CentralAccount ca = new CentralAccount();
                    ca.setYear(FinancialYear.financialYear());
                    ca.setRevenueAccountHash(ra.getRevenueAccountHash());
                    ca.setAmount(amt.toString());
                    ca.setAccountName(ra.getName() + " to ");
                    ca.setTransactionDate(LocalDateTime.now());
                    //Will set other fields such as Timestamp and Account Name (append Expense Account Name0 when we popuate the Expense records
                    ra.setYtdBalance("0");//We have moved all the money
                    ca = centralAccountEjbLocal.createAllocation(ca);

                }
            }
        }
        
        
        //Dealing with the expense side now.
        //Load ExpenseAllocation(s) first
        List<ExpenseAllocation> expenseAllocations=expenseAllocationEjbLocal.getAllocationsForYear(FinancialYear.financialYear());
        LOGGER.info(String.format("%d ExpenseAllocations found for year %d", expenseAllocations.size(), FinancialYear.financialYear()));
        List<ExpenseAccount> expenseAccounts=expenseAccountEjbLocal.getAll(FinancialYear.financialYear());
        Map<String, Integer> expenseAccountsCountMap=new HashMap<>();
        for (ExpenseAccount ea : expenseAccounts) {
            Integer expCatId = ea.getExpenseCategoryId();
            ExpenseCategory expCat = expenseCategoryEjbLocal.findById(expCatId);
            //Is there any value in the Map for this revCat
            String expCatStr = expCat.getExpenseCategory();
            Integer acctCt = expenseAccountsCountMap.get(expCatStr);
            if (acctCt == null) {
                expenseAccountsCountMap.put(expCatStr, 1);
            } else {
                expenseAccountsCountMap.put(expCatStr, expenseAccountsCountMap.get(expCatStr) + 1);
            }
        }
        //At this stage the expenseAccountsCountMap Map should be all populated. Let's print the values
        Set<String> keysExpAcct = expenseAccountsCountMap.keySet();
        Iterator<String> keysExpAcctItr = keysExpAcct.iterator();
        while (keysExpAcctItr.hasNext()) {
            String expCatStrKey = keysExpAcctItr.next();
            LOGGER.info(String.format("ExpenseCategory %s has %d accounts", expCatStrKey, expenseAccountsCountMap.get(expCatStrKey)));
        }
        
        //Store the allocated amount for each ExpenseCategory
        for(ExpenseAllocation eAloc : expenseAllocations){
            String expCatStr=eAloc.getCategory();
            //Do we have mapping of this expCatStr in revCatAllocs Map
            BigDecimal expCatAlloc=expCatAllocs.get(expCatStr);
            if (expCatAlloc==null){
                expCatAllocs.put(expCatStr, new BigDecimal(eAloc.getAllocation()));
            }
        }
        //At this stage the expCatAllocs Map should be all populated. Let's print the values
        Set<String> expKeysAlloc = expCatAllocs.keySet();
        Iterator<String> expKeysItrAlloc = expKeysAlloc.iterator();
        while (expKeysItrAlloc.hasNext()) {
            String expCatAllocStrKey = expKeysItrAlloc.next();
            BigDecimal allocationVal=expCatAllocs.get(expCatAllocStrKey);
            LOGGER.info(String.format("ExpenseAllocation category %s has %.2f allocation", expCatAllocStrKey, expCatAllocs.get(expCatAllocStrKey)));
        }
        //And merge ExpenseAccount(s) with the DB
        for (ExpenseAllocation eal : expenseAllocations) {
            String expCat = eal.getCategory();
            BigDecimal expCatAlloc = expCatAllocs.get(expCat);
            BigDecimal expCatAllocPerAcct = expCatAlloc.divide(new BigDecimal(expenseAccountsCountMap.get(expCat)));
            List<ExpenseAccount> expenseAccountsOfCat = expenseAccountEjbLocal.getAllByCategoryAndYear(expCat, year);
            for (ExpenseAccount eaOfCat : expenseAccountsOfCat) {
                eaOfCat.setYtdBalance(expCatAllocPerAcct.toString());
                expenseAccountEjbLocal.saveExpenseAccount(eaOfCat);
            }
        }
        
        //Lets load the merged ExpenseAccount(s) to update CentralAccount(s)
        expenseAccounts = expenseAccountEjbLocal.getAll(year);
        List<CentralAccount> centralAccounts = centralAccountEjbLocal.getAllForYear(year);
        //Logic to pupulate each Central Account record by 10. 
        int counterForCentralAccount=0;
        for (ExpenseAccount ea : expenseAccounts) {
            while (new BigDecimal(ea.getYtdBalance()).compareTo(new BigDecimal("0")) == 0) {
                
                CentralAccount ca = centralAccounts.get(counterForCentralAccount);
                ca.setYear(FinancialYear.financialYear());
                ca.setExpenseAccountHash(ea.getExpenseAccountHash());
                ca.setAmount("1000");
                ca.setAccountName(ca.getAccountName().concat(ea.getName()));
                ca.setTransactionDate(LocalDateTime.now());
                //Will set other fields such as Timestamp and Account Name (append Expense Account Name0 when we popuate the Expense records
                ea.setYtdBalance(new BigDecimal(ea.getYtdBalance()).subtract(new BigDecimal("1000")).toString());
                ca = centralAccountEjbLocal.saveAllocation(ca);
                counterForCentralAccount++;
            }
        }
        return new AsyncResult<String>(allocationJob);
    }
    
}
