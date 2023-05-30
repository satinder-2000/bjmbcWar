/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bjm.bc.mbean;

import bjm.bc.model.Access;
import bjm.bc.util.BJMConstants;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author user
 */
@Named(value = "logoutMBean")
@ViewScoped
public class LogoutMBean implements Serializable{
    
    public String logout(){
        HttpServletRequest request=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session=request.getSession();
        Access access=(Access) session.getAttribute(BJMConstants.ACCESS);
        session.removeAttribute(BJMConstants.ACCESS);
        session.removeAttribute(BJMConstants.LOGGED_IN_EMAIL);
        session.invalidate();
        return "/logoutconfirm?faces-redirect=true";
    }
    
}
