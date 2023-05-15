/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bjm.bc.mbean;

import bjm.bc.ejb.RevenueCategoryEjbLocal;
import bjm.bc.ejb.RevenuePartyEjbLocal;
import bjm.bc.ejb.exception.UserRegisteredAlreadyException;
import bjm.bc.model.RevenueCategory;
import bjm.bc.model.RevenueParty;
import bjm.bc.model.State;
import bjm.bc.util.BJMConstants;
import bjm.bc.util.FinancialYear;
import bjm.bc.util.HashGenerator;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.flow.FlowScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
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
    private List<RevenueCategory> revenueCategories;
    private List<String> revenueCategoriesStr;
    private String[] partyRevenueCategories;
    private String memorableDateStr;
    
    @Inject
    private RevenueCategoryEjbLocal revenueCategoryEjbLocal;
    @Inject
    private RevenuePartyEjbLocal revenuePartyEjbLocal;
    
    @PostConstruct
    public void init(){
        externalContext=FacesContext.getCurrentInstance().getExternalContext();
        revenueParty=new RevenueParty();
        revenueCategories=new ArrayList<>();
        revenueCategoriesStr= new ArrayList<>();
        RevenueCategory dummyRevCat= new RevenueCategory();
        dummyRevCat.setRevenueCategory(DEFAULT_CODE);
        revenueCategories.add(dummyRevCat);
        revenueCategories.addAll(revenueCategoryEjbLocal.getRevenueCategoriesForYear(FinancialYear.financialYear()));
        for (RevenueCategory rc: revenueCategories){
           revenueCategoriesStr.add(rc.getRevenueCategory());
        }
        LOGGER.info("New Revenue Party initialised");
    }
    
    public String amendDetails(){
        return "RevenuePartyRegister?faces-redirect=true";
    }
    
    public String validateRevenueParty() {
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
        
        //Memorable Date now
        if (!memorableDateStr.isEmpty()){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            revenueParty.setMemorableDate(LocalDate.parse(memorableDateStr, formatter));
        }
        
        //finally create PartyHash
        String partyHash=HashGenerator.generateHash(revenueParty.getName().concat(revenueParty.getEmail()).concat(revenueParty.getOwnerAdhaarNumber()));
	revenueParty.setPartyHash(partyHash);
        
        if (!FacesContext.getCurrentInstance().getMessageList().isEmpty()){
            toReturn =null; //generate same page with errors
        }else{
            toReturn = "RevenuePartyRegisterConfirm?faces-redirect=true";
        }
        return toReturn;
    }
    
    public void submitRevenueParty(){
        try {
            revenueParty = revenuePartyEjbLocal.createRevenueParty(revenueParty);
        } catch (UserRegisteredAlreadyException ex) {
            Logger.getLogger(RevenuePartyRegisterMBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(RevenuePartyRegisterMBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        LOGGER.log(Level.INFO, "Revenue Party persisted with ID: {0} ",revenueParty.getId());
    }
    
    public String getReturnValue() {
        submitRevenueParty();
        return "/flowreturns/RevenuePartyRegister-return?faces-redirect=true";
    }

    public List<RevenueCategory> getRevenueCategories() {
        return revenueCategories;
    }

    public void setRevenueCategories(List<RevenueCategory> revenueCategories) {
        this.revenueCategories = revenueCategories;
    }

    public RevenueParty getRevenueParty() {
        return revenueParty;
    }

    public void setRevenueParty(RevenueParty revenueParty) {
        this.revenueParty = revenueParty;
    }

    public String[] getPartyRevenueCategories() {
        return partyRevenueCategories;
    }

    public void setPartyRevenueCategories(String[] partyRevenueCategories) {
        this.partyRevenueCategories = partyRevenueCategories;
    }

    public String getMemorableDateStr() {
        return memorableDateStr;
    }

    public void setMemorableDateStr(String memorableDateStr) {
        this.memorableDateStr = memorableDateStr;
    }

    

    public List<String> getRevenueCategoriesStr() {
        return revenueCategoriesStr;
    }

    public void setRevenueCategoriesStr(List<String> revenueCategoriesStr) {
        this.revenueCategoriesStr = revenueCategoriesStr;
    }
    
    

   
    
    
    
    
}
