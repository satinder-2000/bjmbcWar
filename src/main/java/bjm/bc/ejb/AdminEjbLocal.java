/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package bjm.bc.ejb;

import bjm.bc.model.Admin;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdminEjbLocal {
    
    public Admin createAdmin(Admin admin);
    public Admin findAdmin();
    public Admin changeAdminPassword(Admin admin);
    
    
}
