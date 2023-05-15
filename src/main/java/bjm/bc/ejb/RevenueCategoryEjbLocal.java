/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package bjm.bc.ejb;

import bjm.bc.model.RevenueCategory;
import javax.ejb.Local;
import java.util.List;

/**
 *
 * @author user
 */
@Local
public interface RevenueCategoryEjbLocal {
    
    public List<RevenueCategory> getRevenueCategoriesForYear(int year);
    
}
