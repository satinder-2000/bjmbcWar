/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bjm.bc.mbean;

import bjm.bc.ejb.RevenueAccountEjbLocal;
import bjm.bc.model.RevenueAccount;
import bjm.bc.model.RevenueAccountTransaction;
import bjm.bc.util.FinancialYear;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author user
 */
@Named(value = "revenueTransactionsMBean")
@ViewScoped
public class RevenueTransactionsMBean implements Serializable {
    
    private static Logger LOGGER=Logger.getLogger(RevenueTransactionsMBean.class.getName());
    
    @Inject
    private RevenueAccountEjbLocal revenueAccountEjbLocal;
    
    private RevenueAccount revenueAccount;
    
    private List<RevenueAccountTransaction> revenueAccountTransactions;
    
    private int year;
    
    @PostConstruct
    public void init(){
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        int accountId=Integer.parseInt(request.getParameter("acctId"));
        revenueAccount = revenueAccountEjbLocal.findById(accountId);
        year=FinancialYear.financialYear();
        revenueAccountTransactions=revenueAccountEjbLocal.getRevenueAccountTransactions(accountId, year );
        LOGGER.info(String.format("RevenueAccount %d loaded along with %d RevenueAccountTransactions.", accountId,revenueAccountTransactions.size()));
    }

    public RevenueAccount getRevenueAccount() {
        return revenueAccount;
    }

    public void setRevenueAccount(RevenueAccount revenueAccount) {
        this.revenueAccount = revenueAccount;
    }

    public List<RevenueAccountTransaction> getRevenueAccountTransactions() {
        return revenueAccountTransactions;
    }

    public void setRevenueAccountTransactions(List<RevenueAccountTransaction> revenueAccountTransactions) {
        this.revenueAccountTransactions = revenueAccountTransactions;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
    
    
    
    
    
}
