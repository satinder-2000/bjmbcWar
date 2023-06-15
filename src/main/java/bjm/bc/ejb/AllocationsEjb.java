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
import java.util.ArrayList;

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
    public Future<String> performAllocations(String allocationJob, String granularity) {

        int year = FinancialYear.financialYear();
       List<RevenueAccount> revenueAccounts = revenueAccountEjbLocal.getAll(year);
        List<RevenueAllocation> revenueAllocations = revenueAllocationEjbLocal.getAllocationsForYear(year);
        //Map to hold number of RevenueAccount(s) by RevenueCategory
        Map<String, Integer> revAcctByCategoryMap = new HashMap<>();
        //Hold allocation for RevenueCategory
        Map<String, BigDecimal> revCatAllocnsMap = new HashMap<>();
        //Hold allocation for RevenueCategory
        Map<String, BigDecimal> expCatAllocnsMap = new HashMap<>();
        //Store the count of each RevenueAccount category Id
        for (RevenueAccount ra : revenueAccounts) {
            Integer revCatId = ra.getRevenueCategoryId();
            RevenueCategory revenueCategory = revenueCategoryEjbLocal.findById(revCatId);
            //Is there any value in the Map for this revCat
            String revCatStr = revenueCategory.getRevenueCategory();
            Integer acctCt = revAcctByCategoryMap.get(revCatStr);
            if (acctCt == null) {
                revAcctByCategoryMap.put(revCatStr, 1);
            } else {
                revAcctByCategoryMap.put(revCatStr, revAcctByCategoryMap.get(revCatStr) + 1);
            }
        }
        //At this stage the revAcctByCategoryMap Map should be all populated. Let's print the values
        Set<String> keys = revAcctByCategoryMap.keySet();
        Iterator<String> keysItr = keys.iterator();
        while (keysItr.hasNext()) {
            String revCatStrKey = keysItr.next();
            LOGGER.info(String.format("RevenueCategory %s has %d accounts", revCatStrKey, revAcctByCategoryMap.get(revCatStrKey)));
        }
        //Store the allocated amount for each RevenueCategory
        for(RevenueAllocation rAloc : revenueAllocations){
            String revCatStr=rAloc.getCategory();
            //Do we have mapping of this recCatStr in revCatAllocs Map
            BigDecimal revCatAlloc=revCatAllocnsMap.get(revCatStr);
            if (revCatAlloc==null){
                revCatAllocnsMap.put(revCatStr, new BigDecimal(rAloc.getAllocation()));
            }
        }
        //At this stage the revCatAllocnsMap should be all populated. Let's print the values
        Set<String> keysAlloc = revCatAllocnsMap.keySet();
        Iterator<String> keysItrAlloc = keysAlloc.iterator();
        while (keysItrAlloc.hasNext()) {
            String revCatAllocStrKey = keysItrAlloc.next();
            BigDecimal allocationVal=revCatAllocnsMap.get(revCatAllocStrKey);
            LOGGER.info(String.format("RevenueAllocation category %s has %.2f allocation", revCatAllocStrKey, allocationVal));
        }
        //And merge RevenueAccount(s) with the DB
        for (RevenueAllocation ral : revenueAllocations) {
            String revCat = ral.getCategory();
            BigDecimal revCatAlloc = revCatAllocnsMap.get(revCat);
            BigDecimal revCatAllocPerAcct = revCatAlloc.divide(new BigDecimal(revAcctByCategoryMap.get(revCat)));
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
                CentralAccount ca = new CentralAccount();
                ca.setYear(FinancialYear.financialYear());
                ca.setRevenueAccountHash(ra.getRevenueAccountHash());
                ca.setAmount(granularity);
                ca.setAccountName(ra.getName() + " to ");
                ca.setTransactionDate(LocalDateTime.now());
                //Will set other fields such as Timestamp and Account Name (append Expense Account Name0 when we popuate the Expense records
                ra.setYtdBalance(new BigDecimal(ra.getYtdBalance()).subtract(new BigDecimal(granularity)).toString());
                ca = centralAccountEjbLocal.createAllocation(ca);
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
            BigDecimal expCatAlloc=expCatAllocnsMap.get(expCatStr);
            if (expCatAlloc==null){
                expCatAllocnsMap.put(expCatStr, new BigDecimal(eAloc.getAllocation()));
            }
        }
        //At this stage the expCatAllocs Map should be all populated. Let's print the values
        Set<String> expKeysAlloc = expCatAllocnsMap.keySet();
        Iterator<String> expKeysItrAlloc = expKeysAlloc.iterator();
        while (expKeysItrAlloc.hasNext()) {
            String expCatAllocStrKey = expKeysItrAlloc.next();
            BigDecimal allocationVal=expCatAllocnsMap.get(expCatAllocStrKey);
            LOGGER.info(String.format("ExpenseAllocation category %s has %.2f allocation", expCatAllocStrKey, expCatAllocnsMap.get(expCatAllocStrKey)));
        }
        //And merge ExpenseAccount(s) with the DB
        for (ExpenseAllocation eal : expenseAllocations) {
            String expCat = eal.getCategory();
            BigDecimal expCatAlloc = expCatAllocnsMap.get(expCat);
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
        //These CAs has full granularty amount populated.
        List<CentralAccount> asIsAccountsToKeep = new ArrayList<>();
        //If we split a CA into two because of granularty amount issue, we populate then in newCentralAccounts and mark the current CA for removal
        List<CentralAccount> centralAccountsToRemove = new ArrayList<>();
        //All the new Central Accounts to be posted  - effectively created from centralAccountsToRemove.
        List<CentralAccount> newCentralAccounts = new ArrayList<>();
        //Logic to pupulate each Central Account record by 10000 
        int counterForCentralAccount=0;
        CentralAccount centralAccountOnHold=null;
        outer: for (ExpenseAccount ea : expenseAccounts)  {
            inner : while(true && counterForCentralAccount< centralAccounts.size()){
                if (centralAccountOnHold!=null){//Revenue related fields already have been populated
                   centralAccountOnHold.setExpenseAccountHash(ea.getExpenseAccountHash());
                   centralAccountOnHold.setTransactionDate(LocalDateTime.now());
                   centralAccountOnHold.setAccountName(centralAccountOnHold.getAccountName().concat(ea.getName()));
                   newCentralAccounts.add(centralAccountOnHold);
                   centralAccountOnHold=null;
                   //counterForCentralAccount++;
                   continue inner;
                }
                CentralAccount ca = centralAccounts.get(counterForCentralAccount);
                
                if (new BigDecimal(ea.getYtdBalance()).compareTo(new BigDecimal(granularity)) == 1)//EA YTD BAL > granurarity
                {
                    ca.setExpenseAccountHash(ea.getExpenseAccountHash());
                    ca.setTransactionDate(LocalDateTime.now());
                    ca.setAmount(granularity);
                    ea.setYtdBalance(new BigDecimal(ea.getYtdBalance()).subtract(new BigDecimal(granularity)).toString());
                    ca.setAccountName(ca.getAccountName().concat(ea.getName()));
                    asIsAccountsToKeep.add(ca);
                    counterForCentralAccount +=1 ;
                    continue inner;
                } else if (new BigDecimal(ea.getYtdBalance()).compareTo(new BigDecimal(granularity)) == -1)//EA YTD BAL > granurarity
                {
                    //mark the original CA record for deletion
                    centralAccountsToRemove.add(ca);
                    //And then create two CA Records.
                    //First with YTD Bal of current EA
                    CentralAccount caNew1 = new CentralAccount();
                    caNew1.setAccountName(ca.getAccountName().concat(ea.getName()));
                    caNew1.setYear(ca.getYear());
                    caNew1.setRevenueAccountHash(ca.getRevenueAccountHash());
                    caNew1.setExpenseAccountHash(ea.getExpenseAccountHash());
                    caNew1.setTransactionDate(LocalDateTime.now());
                    caNew1.setAmount(ea.getYtdBalance());
                    newCentralAccounts.add(caNew1);
                    CentralAccount caNew2 = new CentralAccount();
                    caNew2.setAccountName(ca.getAccountName());//Expense side of name completed in next iteration.
                    caNew2.setYear(ca.getYear());
                    caNew2.setRevenueAccountHash(ca.getRevenueAccountHash());
                    caNew2.setAmount(new BigDecimal(granularity).subtract(new BigDecimal(caNew1.getAmount())).toString());
                    //Rest of the props from the New EA such a accountName.
                    centralAccountOnHold = caNew2;
                    counterForCentralAccount +=1 ;
                    continue outer;
                }
                
            }
        }
        //Merge(s) and Persist(s)
        for(CentralAccount ca: asIsAccountsToKeep){
            centralAccountEjbLocal.saveAllocation(ca);
        }
        for(CentralAccount ca: centralAccountsToRemove){
            centralAccountEjbLocal.removeCentralAccount(ca);
        }
        for(CentralAccount ca: newCentralAccounts){
            centralAccountEjbLocal.createAllocation(ca);
        }
        return new AsyncResult<String>(allocationJob);
    }
    
}
