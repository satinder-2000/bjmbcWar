/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bjm.bc.mbean;

import bjm.bc.ejb.ExpenseAccountEjbLocal;
import bjm.bc.ejb.ExpensePartyEjbLocal;
import bjm.bc.model.Access;
import bjm.bc.model.AccessType;
import bjm.bc.model.ExpenseAccount;
import bjm.bc.model.ExpenseAccountTransaction;
import bjm.bc.model.ExpenseParty;
import bjm.bc.util.BJMConstants;
import bjm.bc.util.FinancialYear;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author user
 */
@Named(value = "expensePartyActionsMBean")
@ViewScoped
public class ExpensePartyActionsMBean implements Serializable{
    
    private static final Logger LOGGER = Logger.getLogger(ExpensePartyActionsMBean.class.getName());
    
    private ExpenseParty expenseParty;
    
    @Inject
    private ExpensePartyEjbLocal expensePartyEjbLocal;
    
    @Inject
    private ExpenseAccountEjbLocal expenseAccountEjbLocal;
    
    @PostConstruct
    public void init(){
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession();
        Access access = (Access) session.getAttribute(BJMConstants.ACCESS);
        assert(access.getAccessType().equals(AccessType.EXPENSE_PARTY));
        expenseParty = expensePartyEjbLocal.findByEmail(access.getEmail());
        LOGGER.info(String.format("ExpenseParty with ID %d initialise with %d Expense Accounts",expenseParty.getId(),
                            expenseParty.getExpenseAccounts().size()));
    }
    
    public void ajaxTypeListener(AjaxBehaviorEvent event){
        List<ExpenseAccount> partyAccounts=expenseParty.getExpenseAccounts();
        for (ExpenseAccount ea: partyAccounts){
            if (ea.getMoneyIn() > 0){
                ExpenseAccountTransaction eat = new ExpenseAccountTransaction();
                eat.setMoneyIn(ea.getMoneyIn());
                eat.setYtdBalance(ea.getYtdBalance()+ea.getMoneyIn());
                ea.setYtdBalance(ea.getYtdBalance()+ea.getMoneyIn());
                eat.setYear(FinancialYear.financialYear());
                eat.setCreatedOn(new Timestamp(System.currentTimeMillis()));
                eat.setExpenseAccountId(ea.getId());
                ea.setMoneyIn(0);
                expenseAccountEjbLocal.createMoneyInRevenueAccount(eat);
                expenseAccountEjbLocal.saveExpenseAccount(ea);
            }else 
            if (ea.getMoneyOut() > ea.getYtdBalance()){
                FacesContext.getCurrentInstance().addMessage("*", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Withdrawal exceeds the available funds","Withdrawal exceeds the available funds"));
            }else{
                ExpenseAccountTransaction eat=new ExpenseAccountTransaction();
                eat.setMoneyOut(ea.getMoneyOut());
                eat.setYtdBalance(ea.getYtdBalance()-ea.getMoneyOut());
                ea.setYtdBalance(ea.getYtdBalance()-ea.getMoneyOut());
                eat.setYear(FinancialYear.financialYear());
                eat.setCreatedOn(new Timestamp(System.currentTimeMillis()));
                eat.setExpenseAccountId(ea.getId());
                ea.setMoneyOut(0);
                expenseAccountEjbLocal.createMoneyOutExpenseAccount(eat);
                expenseAccountEjbLocal.saveExpenseAccount(ea);
            }
        }
    }

    public ExpenseParty getExpenseParty() {
        return expenseParty;
    }

    public void setExpenseParty(ExpenseParty expenseParty) {
        this.expenseParty = expenseParty;
    }
    
    
    
    

    
}
