/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sampledata;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class SampleDataJdbc {
    
    public static void main(String... args){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded!!");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SampleDataJdbc.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
