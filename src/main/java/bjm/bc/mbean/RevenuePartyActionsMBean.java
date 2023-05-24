/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bjm.bc.mbean;

import bjm.bc.ejb.RevenuePartyEjbLocal;
import bjm.bc.model.Access;
import bjm.bc.model.AccessType;
import bjm.bc.model.RevenueParty;
import bjm.bc.util.BJMConstants;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
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
        
    }

    public RevenueParty getRevenueParty() {
        return revenueParty;
    }

    public void setRevenueParty(RevenueParty revenueParty) {
        this.revenueParty = revenueParty;
    }
    
    
    
    
}
