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
public class MyBigDecimalTest {
    
    public static void main(String[] args){
        double d1=45.33;
        print("d1 is "+d1);
        double d2=1.01;
        print("d2 is "+d2);
        double d3 = d2+d1;
        print("d3 is "+d3);
        
        print(Double.valueOf("43.333333333333333")+"");
        print(Double.valueOf(43.333333333333333)+"");
        
        print(Double.valueOf("43.33333333333333")+"");
        print(Double.valueOf(43.33333333333333)+"");
        
        BigDecimal bd1=new BigDecimal("45.33");
        print("bd1 is "+bd1.toString());
        BigDecimal bd2=new BigDecimal("1.01");
        print("bd2 is "+bd2.toString());
        BigDecimal bd3=bd1.add(bd2);
        print("bd3 is "+bd3.toString());
        
    } 
    
    public static void print(String s){
        System.out.println(s);
    }
}
