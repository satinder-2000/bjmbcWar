/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bjm.bc.mbean;

import bjm.bc.model.Access;
import bjm.bc.model.AccessType;
import static bjm.bc.model.AccessType.EXPENSE_PARTY;
import static bjm.bc.model.AccessType.REVENUE_PARTY;
import bjm.bc.util.BJMConstants;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author user
 */
@Named(value = "homeMBean")
@RequestScoped
public class HomeMBean {

    private static final Logger LOGGER = Logger.getLogger(HomeMBean.class.getName());

    public String redirectToHome() {
        String toReturn = "";
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession();
        Access access = (Access) session.getAttribute(BJMConstants.ACCESS);
        if (access == null) {
            toReturn = "index?faces-redirect=true";
        } else {
            String accessTypeStr = access.getAccessType();
            AccessType accessType = AccessType.valueOf(accessTypeStr);
            switch (accessType) {
                case REVENUE_PARTY:
                    toReturn = "home/RevenuePartyHome?faces-redirect=true";
                    break;
                case EXPENSE_PARTY:
                    toReturn = "home/ExpensePartyHome?faces-redirect=true";
                    break;
            }
        }
        return toReturn;
    }
}
