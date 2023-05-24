/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bjm.bc.mbean;

import bjm.bc.ejb.AccessEjbLocal;
import bjm.bc.model.Access;
import bjm.bc.model.AccessType;
import bjm.bc.util.BJMConstants;
import bjm.bc.util.PasswordUtil;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author user
 */
@Named(value = "loginMBean")
@ViewScoped
public class LoginMBean implements Serializable {
    
    static final Logger LOGGER=Logger.getLogger(LoginMBean.class.getName());
    
    private String email;
    
    private String password;
    
    @Inject
    private AccessEjbLocal accessEjbLocal;
    
    public String login(){
        boolean userInputError= false;
        String toReturn=null;
        if (email.isBlank()){
            FacesContext.getCurrentInstance().addMessage("email", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email not provided", "Email not provided"));
            userInputError=true;
        }
        if (password.isBlank()){
            FacesContext.getCurrentInstance().addMessage("password", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password not provided", "Password not provided"));
            userInputError=true;
        }
        if (!userInputError){
            Access access= accessEjbLocal.findByEmail(email);
            boolean passwordValid =PasswordUtil.verifyUserPassword(password, access.getPassword(), email);
            if (!passwordValid){
                FacesContext.getCurrentInstance().addMessage("password", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Password", "Invalid Password"));
            }else{
                HttpServletRequest request=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
                HttpSession session=request.getSession(true);
                session.setAttribute(BJMConstants.ACCESS, access);
                session.setAttribute(BJMConstants.LOGGED_IN_EMAIL, access.getEmail());
                AccessType accessType= AccessType.valueOf(access.getAccessType());
                switch (accessType){
                    case REVENUE_PARTY:
                        toReturn = "home/RevenuePartyHome";
                        break;
                    case EXPENSE_PARTY:
                        toReturn = "home/ExpensePartyHome";
                        break;
                    default:
                        toReturn = "AccessNotFound";
                        break;
                    
                }
            }
        }
        
        if ((!FacesContext.getCurrentInstance().getMessageList().isEmpty())){
            return null;
        }else{
            return toReturn;
        } 
    }
    
    public String logout(){
        HttpServletRequest request=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session=request.getSession();
        Access access=(Access) session.getAttribute(BJMConstants.ACCESS);
        session.removeAttribute(BJMConstants.ACCESS);
        session.removeAttribute(BJMConstants.LOGGED_IN_EMAIL);
        session.invalidate();
        return "index";
    }
    
    public String redirectToHome(){
        return null;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
}
