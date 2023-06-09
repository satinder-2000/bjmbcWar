/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bigdecimal;

import java.math.BigDecimal;

/**
 *
 * @author user
 */
public class BigDecimalDemo {
    
    public static void main(String... args){
        
        double d1= 0.01;
        print("d1 "+d1);
        
        double d2 = 33.33;
        print("d2 = " + d2);

        double d3 = d2 + d1;
        print("d3 = d2+d = " + d3);

        double d4 = 33.34;
        print("d4 = " + d4);

        // Let's work for BigDecimal
        BigDecimal bd1 = new BigDecimal(0.01);
        print("bd1 = " + bd1.doubleValue());

        BigDecimal bd2 = new BigDecimal(33.33);
        print("bd2 = " + bd2.doubleValue());

        BigDecimal bd3 = new BigDecimal(33.34);
        print("bd3 = " + bd3.doubleValue());

        BigDecimal bd4 = new BigDecimal(33.33 + 0.01);
        print("bd4 = " + bd4.doubleValue());

        BigDecimal bd5 = new BigDecimal(String.format("%.2f", 33.33 + 0.01));
        print("bd5 = " + bd5.doubleValue());

        // Provide values in String in BigDecimal for better precision
        // if you already have value in double like shown below
        double d11 = 0.1 + 0.1 + 0.1;
        print("d11 = " + d11);

        BigDecimal bd11 = new BigDecimal(String.valueOf(d11)); // You already lost the precision
        print("bd11 = " + bd11.doubleValue());

        /* Convert to string as per your needed precision like 2 for
           example when you might have lost the precision  */
        BigDecimal bd12 = new BigDecimal(String.format("%.2f", d11));
        print("bd12 = " + bd12);

        // Or make sure to do that before addition in double
        double d21 = 0.1;
        BigDecimal bd21 = new BigDecimal(String.valueOf(d21));
        BigDecimal bd22 = bd21.add(bd21).add(bd21);
        print("bd21 = " + bd21.doubleValue() + ", bd22 = " + bd22.doubleValue());

    }
    
    public static void print(String s){
        System.out.println(s);
    }
    
}


