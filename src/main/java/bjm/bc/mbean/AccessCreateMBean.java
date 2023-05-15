/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bjm.bc.mbean;

import bjm.bc.ejb.AccessEjbLocal;
import bjm.bc.ejb.ExpensePartyEjbLocal;
import bjm.bc.ejb.RevenuePartyEjbLocal;
import bjm.bc.model.Access;
import bjm.bc.model.AccessType;
import bjm.bc.model.ExpenseParty;
import bjm.bc.model.RevenueParty;
import bjm.bc.util.BJMConstants;
import bjm.bc.util.PasswordUtil;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author user
 */
@Named(value = "accessCreateMBean")
@ViewScoped
public class AccessCreateMBean implements Serializable{
    
    final static Logger LOGGER=Logger.getLogger(AccessCreateMBean.class.getName());
    
    @Inject
    private ExpensePartyEjbLocal expensePartyEjbLocal;
    @Inject
    private RevenuePartyEjbLocal revenuePartyEjbLocal;
    
    @Inject
    private AccessEjbLocal accessEjbLocal;
    
    private Access access;
    
    @PostConstruct
    public void init(){
        LOGGER.info("View initialised for Access");
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String email = request.getParameter("email");
        String accessType=request.getParameter("accessTpe");
        AccessType accessTypeE=AccessType.valueOf(accessType);
        access = accessEjbLocal.findByEmailAndAccessType(email, accessType);
    }
    
    public String processForm(){
        FacesContext context = FacesContext.getCurrentInstance();
        String toReturn=null;
        String password=access.getPassword();
        String passwordConfirm=access.getPasswordConfirm();
        if (password.trim().isEmpty()){
            FacesContext.getCurrentInstance().addMessage("pwd1",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,"No Password entered","No Password entered"));
        }else{
            //First, RegEx the password
            Pattern pCdIn=Pattern.compile(BJMConstants.PW_REGEX);
            Matcher mPCdIn=pCdIn.matcher(password);
            if (!mPCdIn.find()){
                FacesContext.getCurrentInstance().addMessage("pwd1",new FacesMessage(FacesMessage.SEVERITY_ERROR,"Invalid Password","Invalid Password"));  
            }else{//compare the password now
                if(!password.equals(passwordConfirm)){
                    FacesContext.getCurrentInstance().addMessage("pwd2",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,"Passwords mismatch","Passwords mismatch"));
                }
                
            }
        }
        List<FacesMessage> msgs= FacesContext.getCurrentInstance().getMessageList();
        if (msgs!=null && msgs.size()>0){
            toReturn =null;
        }else{
            access.setPassword(PasswordUtil.generateSecurePassword(access.getPassword(), access.getEmail()));
            access=accessEjbLocal.createAccess(access);
            //Check if the Access belongs to an ExpenseParty and if it does, update the ExpenseParty
            AccessType accessTypeE=AccessType.valueOf(access.getAccessType());
            if (accessTypeE.equals(AccessType.REVENUE_PARTY)){
                RevenueParty revenueParty=revenuePartyEjbLocal.findByEmail(access.getEmail());
                revenueParty.setPassword(access.getPassword());
                revenueParty = revenuePartyEjbLocal.updateRevenueParty(revenueParty);
                LOGGER.info(String.format("Password set for RevenueParty %d",revenueParty.getId()));
            }else if (accessTypeE.equals(AccessType.EXPENSE_PARTY)){
                ExpenseParty expenseParty=expensePartyEjbLocal.findByEmail(access.getEmail());
                expenseParty.setPassword(access.getPassword());
                expenseParty = expensePartyEjbLocal.updateExpenseParty(expenseParty);
                LOGGER.info(String.format("Password set for ExpenseParty %d",expenseParty.getId()));
            
            }
            
            
            toReturn="/home/UserWelcome?faces-redirect=true";
        }
        LOGGER.log(Level.INFO, "toReturn is :{0}", toReturn);
        return toReturn;
    }
    
}
