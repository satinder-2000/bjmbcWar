/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bjm.bc.mbean;

import bjm.bc.ejb.RevenueCategoryEjbLocal;
import bjm.bc.ejb.RevenuePartyEjbLocal;
import bjm.bc.ejb.StateEjbLocal;
import bjm.bc.model.RevenueCategory;
import bjm.bc.model.RevenueParty;
import bjm.bc.model.State;
import bjm.bc.util.BJMConstants;
import bjm.bc.util.FinancialYear;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.flow.FlowScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author user
 */
@Named(value = "revenuePartyRegisterMBean")
@FlowScoped("RevenuePartyRegister")
public class RevenuePartyRegisterMBean implements Serializable {
    
    private static final Logger LOGGER=Logger.getLogger(RevenuePartyRegisterMBean.class.getName());
    private static final String DEFAULT_CODE="NA";
    
    private ExternalContext externalContext;
    private RevenueParty revenueParty;
    private List<State> states;
    private List<RevenueCategory> revenueCategories;
    
    @Inject
    private StateEjbLocal stateEjbLocal;
    @Inject
    private RevenueCategoryEjbLocal revenueCategoryEjbLocal;
    @Inject
    private RevenuePartyEjbLocal revenuePartyEjbLocal;
    
    @PostConstruct
    public void init(){
        externalContext=FacesContext.getCurrentInstance().getExternalContext();
        revenueParty=new RevenueParty();
        states=new ArrayList<>();
        State dummy=new State();
        dummy.setCode(DEFAULT_CODE);
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle rb = context.getApplication().evaluateExpressionGet(context, "#{msg}", ResourceBundle.class);
        dummy.setName(rb.getString("pleaseSelectOne"));
        states.add(dummy);
        states.addAll(stateEjbLocal.getAllStates());
        revenueCategories=new ArrayList<>();
        RevenueCategory dummyRevCat= new RevenueCategory();
        dummyRevCat.setRevenueCategory(DEFAULT_CODE);
        revenueCategories.add(dummyRevCat);
        revenueCategories.addAll(revenueCategoryEjbLocal.getRevenueCategoriesForYear(FinancialYear.financialYear()));
        LOGGER.info("New Revenue Party initialised");
    }
    
    public String processData(){
        String toReturn = validateRevenueParty();
        if (toReturn != null) {
            toReturn = "RevenuePartyConfirm?faces-redirect=true";
        }
        return toReturn;
    }
    
    private String validateRevenueParty() {
        String toReturn = null;
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle rb = context.getApplication().evaluateExpressionGet(context, "#{msg}", ResourceBundle.class);
        //Name
        if(revenueParty.getName().isEmpty()){
           FacesContext.getCurrentInstance().addMessage("name", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("nameRequired"), rb.getString("nameRequired"))); 
        }else if(revenueParty.getName().length()<2 || revenueParty.getName().length()>45){
            FacesContext.getCurrentInstance().addMessage("name", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("nameCharsLimit"), rb.getString("nameCharsLimit")));
        }
        
        
        //Validate email if Exists
        if (revenueParty.getEmail().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage("email", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("emailRequired"), rb.getString("emailRequired")));
        } else {//Email Regex validation
            Pattern p = Pattern.compile(BJMConstants.EMAIL_REGEX);
            Matcher m = p.matcher(revenueParty.getEmail());
            if (!m.find()) {
                FacesContext.getCurrentInstance().addMessage("email", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("emailInvalid"), rb.getString("emailInvalid")));
            } else {
                boolean isEmailRegistered = revenuePartyEjbLocal.isEmailRegistered(revenueParty.getEmail());
                if (isEmailRegistered) {
                    FacesContext.getCurrentInstance().addMessage("email", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("emailTaken"), rb.getString("emailTaken")));
                    
                }
            
            }

        }
        
        if (!FacesContext.getCurrentInstance().getMessageList().isEmpty()){
            toReturn = null; //generate same page with errors
        }else{
            toReturn = "RevenuePartyConfirm?faces-redirect=true";
        }
        return toReturn;
    }
    
    public String revenuePartySubmit(){
        return null;
    
    }

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }

    public List<RevenueCategory> getRevenueCategories() {
        return revenueCategories;
    }

    public void setRevenueCategories(List<RevenueCategory> revenueCategories) {
        this.revenueCategories = revenueCategories;
    }

   
    
    
    
    
}
