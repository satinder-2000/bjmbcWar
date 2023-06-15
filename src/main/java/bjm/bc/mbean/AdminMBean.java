/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bjm.bc.mbean;

import bjm.bc.ejb.AdminEjbLocal;
import bjm.bc.ejb.AllocationsEjbLocal;
import bjm.bc.model.Admin;
import bjm.bc.util.FinancialYear;
import bjm.bc.util.PasswordUtil;
import java.io.Serializable;
import java.util.logging.Logger;
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
@Named(value = "adminMBean")
@ViewScoped
public class AdminMBean implements Serializable {
    
    private static final Logger LOGGER=Logger.getLogger(AdminMBean.class.getName());
    
    private Admin admin;
    private String password="";
    private int year; 
    
    private String granularity="--Select One--";
        
    @Inject
    private AllocationsEjbLocal allocationsEjbLocal;
    
    @PostConstruct
    public void init(){
        admin=new Admin();
        admin.setEmail("admin@bjmbc.net");
        admin.setPassword("**********");
        LOGGER.info("Admin initialised!!");
        year=FinancialYear.financialYear();
    }
    
    @Inject
    private AdminEjbLocal adminEjbLocal;
    
    public String createAdmin(){
        Admin admindB = adminEjbLocal.findAdmin();
        if (admindB!=null){
            FacesContext.getCurrentInstance().addMessage("onw", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Admin alredy created.","Admin alredy created."));
        }else{
            adminEjbLocal.createAdmin(admin);
            FacesContext.getCurrentInstance().addMessage("firstname", new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Success")); 
        }
        return null;
    }
    
    public String changePassword(){
        admin.setPassword(PasswordUtil.generateSecurePassword(password, "admin@bjmbc.net"));
        admin = adminEjbLocal.changeAdminPassword(admin);
        FacesContext.getCurrentInstance().addMessage("firstname", new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Success")); 
        return null;
    }
    
    public String performAllocations(){
        HttpServletRequest request =(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        //String granularity = request.getParameter("granularity");
        int year =FinancialYear.financialYear();
        allocationsEjbLocal.performAllocations( year +" allocations", granularity);
        return null;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getGranularity() {
        return granularity;
    }

    public void setGranularity(String granularity) {
        this.granularity = granularity;
    }
    
    
    
 }
