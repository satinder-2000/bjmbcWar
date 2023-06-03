/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bjm.bc.mbean;

import bjm.bc.ejb.AdminEjbLocal;
import bjm.bc.ejb.AllocationsEjbLocal;
import bjm.bc.model.Admin;
import bjm.bc.util.PasswordUtil;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

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
    
    @Inject
    private AllocationsEjbLocal allocationsEjbLocal;
    
    @PostConstruct
    public void init(){
        admin=new Admin();
        admin.setEmail("admin@bjmbc.net");
        admin.setPassword(PasswordUtil.generateSecurePassword("IL@ve2nu69", "admin@bjmbc.net"));
        LOGGER.info("Admin initialised!!");
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
        allocationsEjbLocal.performAllocations("2023 allocations");
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
    
    
    
    
}
