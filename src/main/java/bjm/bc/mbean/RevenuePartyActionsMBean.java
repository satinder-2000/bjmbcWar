/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bjm.bc.mbean;

import bjm.bc.ejb.RevenueAccountEjbLocal;
import bjm.bc.ejb.RevenuePartyEjbLocal;
import bjm.bc.model.Access;
import bjm.bc.model.AccessType;
import bjm.bc.model.RevenueAccount;
import bjm.bc.model.RevenueAccountTransaction;
import bjm.bc.model.RevenueParty;
import bjm.bc.util.BJMConstants;
import bjm.bc.util.FinancialYear;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author user
 */
@Named(value = "revenuePartyActionsMBean")
@SessionScoped
public class RevenuePartyActionsMBean implements Serializable {
    
    private static final Logger LOGGER=Logger.getLogger(RevenuePartyActionsMBean.class.getName());
    
    private RevenueParty revenueParty ;
    
    @Inject
    private RevenuePartyEjbLocal revenuePartyEjbLocal;
    
    @Inject
    private RevenueAccountEjbLocal revenueAccountEjbLocal;
    
    @PostConstruct
    public void init(){
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session=request.getSession();
        Access access = (Access)session.getAttribute(BJMConstants.ACCESS);
        assert(access.getAccessType().equals(AccessType.REVENUE_PARTY.toString()));
        revenueParty = revenuePartyEjbLocal.findByEmail(access.getEmail());
        LOGGER.info(String.format("RevenueParty with ID %d initialised with %d Revenue Accounts",revenueParty.getId(),
                revenueParty.getRevenueAccounts().size()));
           
    }
    
    public void ajaxTypeListener(AjaxBehaviorEvent event){
        LOGGER.info("AJAX invoked.");
        List<RevenueAccount> partyAccounts = revenueParty.getRevenueAccounts();
        for (RevenueAccount ra: partyAccounts){
            if (ra.getMoneyIn() > 0){
                RevenueAccountTransaction rat = new RevenueAccountTransaction();
                rat.setMoneyIn(ra.getMoneyIn());
                rat.setYtdBalance(rat.getYtdBalance()+ra.getMoneyIn());
                ra.setYtdBalance(ra.getYtdBalance()+ra.getMoneyIn());
                rat.setYear(FinancialYear.financialYear());
                rat.setCreatedOn(new Timestamp(System.currentTimeMillis()));
                rat.setRevenueAccountId(ra.getId());
                ra.setMoneyIn(0);
                revenueAccountEjbLocal.createMoneyInRevenueAccount(rat);
                revenueAccountEjbLocal.saveRevenueAccount(ra);
            }else if(ra.getMoneyOut()>0){
                //perform check first - money should be available in the account.
                if (ra.getMoneyOut() > ra.getYtdBalance()){
                    FacesContext.getCurrentInstance().addMessage("*", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Withdrawal exceeds the available funds","Withdrawal exceeds the available funds"));
                }else{
                    RevenueAccountTransaction rat = new RevenueAccountTransaction();
                    rat.setMoneyOut(ra.getMoneyOut());
                    rat.setYtdBalance(rat.getYtdBalance()-ra.getMoneyOut());
                    ra.setYtdBalance(ra.getYtdBalance()-ra.getMoneyOut());
                    rat.setYear(FinancialYear.financialYear());
                    rat.setCreatedOn(new Timestamp(System.currentTimeMillis()));
                    rat.setRevenueAccountId(ra.getId());
                    ra.setMoneyOut(0);
                    revenueAccountEjbLocal.createMoneyOutRevenueAccount(rat);
                    revenueAccountEjbLocal.saveRevenueAccount(ra);
                    
                }
            }
        }
        
    }
    
    public RevenueParty getRevenueParty() {
        return revenueParty;
    }

    public void setRevenueParty(RevenueParty revenueParty) {
        this.revenueParty = revenueParty;
    }
    
    
    
    
}
