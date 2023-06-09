/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sampledata;


import bjm.bc.util.FinancialYear;
import bjm.bc.util.HashGenerator;
import bjm.bc.util.PasswordUtil;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class DataGen {
    
    private static Logger LOGGER = Logger.getLogger(DataGen.class.getName());

    public static void main(String[] args) {

        DataGen dataGen = new DataGen();
        dataGen.resetDatabase();
        dataGen.loadCategories();
        dataGen.processRevenue();
        dataGen.processExpense();
        dataGen.processRevenueAllocation();
        dataGen.processExpenseAllocation();
        

    }
    
    Connection con;
    final static String DB_URL="jdbc:mysql://localhost:3306/bjmbc";
    final static String DB_USER="root";
    final static String DB_PW="IL@ve2nu69";
    private static final String EXP_CAT="/Users/user/NBProjs/bjmbcData/RefData/ExpenseCategory2023.csv";
    private static final String REV_CAT="/Users/user/NBProjs/bjmbcData/RefData/RevenueCategory2023.csv";
    private static final String EXP_ALLOC="/Users/user/NBProjs/bjmbcData/RefData/ExpenseBudget-2023-24.csv";
    private static final String REV_ALLOC="/Users/user/NBProjs/bjmbcData/RefData/RevenueBudget-2023-24.csv";
    
    public DataGen(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded!!");
            //System.out.println("MANUALLY LOAD DATA IN EXPENSE_CATEGORY AND REVENUE_CATEGORY");
        } catch (ClassNotFoundException ex) {
            LOGGER.severe(ex.getMessage());
        }
        
    }
    
    private void resetDatabase(){
        //We go backwards from Transactional Data to the references data
        //Remove Access data
        try {
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PW);
            Statement stmt;
            stmt = con.createStatement();
            int i = stmt.executeUpdate("DELETE FROM Access");
            System.out.println(String.format("%d records deleted in Access",i));
            i = stmt.executeUpdate("DELETE FROM CENTRAL_ACCOUNT");
            System.out.println(String.format("%d records deleted in CENTRAL_ACCOUNT",i));
            i = stmt.executeUpdate("DELETE FROM EXPENSE_ACCOUNT_TRANSACTION");
            System.out.println(String.format("%d records deleted in EXPENSE_ACCOUNT_TRANSACTION",i));
            i = stmt.executeUpdate("DELETE FROM EXPENSE_ACCOUNT");
            System.out.println(String.format("%d records deleted in EXPENSE_ACCOUNT",i));
            i = stmt.executeUpdate("DELETE FROM EXPENSE_PARTY");
            System.out.println(String.format("%d records deleted in EXPENSE_PARTY",i));
            i = stmt.executeUpdate("DELETE FROM EXPENSE_CATEGORY");
            System.out.println(String.format("%d records deleted in EXPENSE_CATEGORY",i));
            i = stmt.executeUpdate("DELETE FROM REVENUE_ACCOUNT_TRANSACTION");
            System.out.println(String.format("%d records deleted in REVENUE_ACCOUNT_TRANSACTION",i));
            i = stmt.executeUpdate("DELETE FROM REVENUE_ACCOUNT");
            System.out.println(String.format("%d records deleted in REVENUE_ACCOUNT",i));
            i = stmt.executeUpdate("DELETE FROM REVENUE_PARTY");
            System.out.println(String.format("%d records deleted in REVENUE_PARTY",i));
            i = stmt.executeUpdate("DELETE FROM REVENUE_CATEGORY");
            System.out.println(String.format("%d records deleted in REVENUE_CATEGORY",i));
            i = stmt.executeUpdate("DELETE FROM REVENUE_ALLOCATION");
            System.out.println(String.format("%d records deleted in REVENUE_ALLOCATION",i));
            i = stmt.executeUpdate("DELETE FROM EXPENSE_ALLOCATION");
            System.out.println(String.format("%d records deleted in EXPENSE_ALLOCATION",i));
            
            stmt.execute("ALTER TABLE ACCESS AUTO_INCREMENT = 1");
            System.out.println("ALTER TABLE ACCESS AUTO_INCREMENT done");
            stmt.execute("ALTER TABLE CENTRAL_ACCOUNT AUTO_INCREMENT = 1");
            System.out.println("ALTER TABLE CENTRAL_ACCOUNT AUTO_INCREMENT done");
            stmt.execute("ALTER TABLE EXPENSE_ACCOUNT_TRANSACTION AUTO_INCREMENT = 1");
            System.out.println("ALTER TABLE EXPENSE_ACCOUNT_TRANSACTION AUTO_INCREMENT done");
            stmt.execute("ALTER TABLE EXPENSE_ACCOUNT AUTO_INCREMENT = 1");
            System.out.println("ALTER TABLE EXPENSE_ACCOUNT AUTO_INCREMENT done");
            stmt.execute("ALTER TABLE EXPENSE_PARTY AUTO_INCREMENT = 1");
            System.out.println("ALTER TABLE EXPENSE_PARTY AUTO_INCREMENT done");
            stmt.execute("ALTER TABLE EXPENSE_CATEGORY AUTO_INCREMENT = 1");
            System.out.println("ALTER TABLE EXPENSE_CATEGORY AUTO_INCREMENT done");
            stmt.execute("ALTER TABLE EXPENSE_ALLOCATION AUTO_INCREMENT = 1");
            System.out.println("ALTER TABLE EXPENSE_ALLOCATION AUTO_INCREMENT done");
            
            stmt.execute("ALTER TABLE REVENUE_ACCOUNT_TRANSACTION AUTO_INCREMENT = 1");
            System.out.println("ALTER TABLE REVENUE_ACCOUNT_TRANSACTION AUTO_INCREMENT done");
            stmt.execute("ALTER TABLE REVENUE_ACCOUNT AUTO_INCREMENT = 1");
            System.out.println("ALTER TABLE REVENUE_ACCOUNT AUTO_INCREMENT done");
            stmt.execute("ALTER TABLE REVENUE_PARTY AUTO_INCREMENT = 1");
            System.out.println("ALTER TABLE REVENUE_PARTY AUTO_INCREMENT done");
            stmt.execute("ALTER TABLE REVENUE_CATEGORY AUTO_INCREMENT = 1");
            System.out.println("ALTER TABLE REVENUE_CATEGORY AUTO_INCREMENT done");
            stmt.execute("ALTER TABLE REVENUE_ALLOCATION AUTO_INCREMENT = 1");
            System.out.println("ALTER TABLE REVENUE_ALLOCATION AUTO_INCREMENT done");
            
        } catch (SQLException ex) {
            Logger.getLogger(DataGen.class.getName()).log(Level.SEVERE, null, ex);
            
        }finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataGen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("MANUALLY LOAD DATA IN EXPENSE_CATEGORY AND REVENUE_CATEGORY");
        
    }
    
    private void loadCategories(){
        BufferedReader reader;
        
        try {
            reader=new BufferedReader(new FileReader(EXP_CAT));
            String line= reader.readLine();//First line is a header that we don't need
            StringBuilder sbE=new StringBuilder("INSERT INTO EXPENSE_CATEGORY(YEAR,EXPENSE_CATEGORY) VALUES ");
            line= reader.readLine();
            while(line != null){
                //System.out.println(line);
                StringTokenizer tokenizer=new StringTokenizer(line,",");
                while (tokenizer.hasMoreElements()){
                    String year = tokenizer.nextToken();
                    String category=tokenizer.nextToken().replace("\"","").trim();
                    sbE.append("(").append(year).append(",'").append(category).append("'),");
                    //System.out.println("YEAR: "+tokenizer.nextToken()+" ExpenseCategory: "+tokenizer.nextToken());
                }
                line= reader.readLine();
            }
            sbE.replace(sbE.lastIndexOf(","), sbE.lastIndexOf(",")+1, ";");
            System.out.println(sbE.toString());
            try {
                con = DriverManager.getConnection(DB_URL, DB_USER, DB_PW);
                PreparedStatement pstmt;
                pstmt = con.prepareStatement(sbE.toString());
                int rows=pstmt.executeUpdate();
                System.out.println(String.format("EXPENSE_CATEGORY populated with %d records",rows));
                

            } catch (SQLException ex) {
                Logger.getLogger(DataGen.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DataGen.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            reader.close();
            System.out.println();
            System.out.println("REVENUE CATEGORIES NOW");
            System.out.println();
            // REV CAT NOW
            reader=new BufferedReader(new FileReader(REV_CAT));
            line= reader.readLine();
            StringBuilder sbR=new StringBuilder("INSERT INTO REVENUE_CATEGORY(YEAR,REVENUE_CATEGORY) VALUES ");
            line= reader.readLine();
            while(line != null){
                System.out.println(line);
                StringTokenizer tokenizer=new StringTokenizer(line,",");
                while (tokenizer.hasMoreElements()){
                    String year = tokenizer.nextToken();
                    String category=tokenizer.nextToken().replace("\"","").trim();
                    sbR.append("(").append(year).append(",'").append(category).append("'),");
                    //System.out.println("YEAR: "+tokenizer.nextToken()+" RevenueCategory: "+tokenizer.nextToken());
                }
                
                line= reader.readLine();
            }
            sbR.replace(sbR.lastIndexOf(","), sbR.lastIndexOf(",")+1, ";");
            System.out.println(sbR.toString());
             try {
                con = DriverManager.getConnection(DB_URL, DB_USER, DB_PW);
                PreparedStatement pstmt;
                pstmt = con.prepareStatement(sbR.toString());
                int rows=pstmt.executeUpdate();
                System.out.println(String.format("REVENUE_CATEGORY populated with %d records",rows));
                

            } catch (SQLException ex) {
                Logger.getLogger(DataGen.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DataGen.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            reader.close();
            
        } catch (FileNotFoundException ex) {
            LOGGER.severe(ex.getMessage());
        }catch (IOException ex) {
            LOGGER.severe(ex.getMessage());
        }
        
    }

    private void processExpense() {
        List<ExpenseCategory> expenseCategories=new ArrayList<>();
        try {
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PW);
            Statement stmt;
            stmt = con.createStatement();
            
            ResultSet rs=stmt.executeQuery("SELECT ID, YEAR, EXPENSE_CATEGORY FROM EXPENSE_CATEGORY");
            while(rs.next()){
                ExpenseCategory ea=new ExpenseCategory();
                ea.setId(rs.getInt("ID"));
                ea.setYear(rs.getInt("YEAR"));
                ea.setExpenseCategory(rs.getString("EXPENSE_CATEGORY"));
                expenseCategories.add(ea);
            }
            System.out.println(String.format("%d ExpenseCategory(s) loaded.", expenseCategories.size()));
            int ctAccts = expenseCategories.size();
            int ctPW = expenseCategories.size() / 3;//ct party [whole] is 8 -
            int ctPR = 1;//ct party [remainder] for remaining A/Cs < 3
            int partyCt = 0;//Count parties as we create them
            int acctCt = 0;//Count accounts as we create them
            PreparedStatement pstmt=null;
            for (partyCt = 1; partyCt <= ctPW; partyCt++) {
                //Create Party
                String ep_name = "EP" + partyCt + " Bjm";
                String ep_email = "ep" + partyCt + "@bjmbc.net";
                LocalDate ep_memDate = LocalDate.of(1973, Month.NOVEMBER, 27);
                String ep_ownerAdhaarCard = "1234567890";
                String ep_organisation = "Bjmbc";
                String ep_password = (PasswordUtil.generateSecurePassword("IL@ve2nu69", ep_email));
                String ep_partyHash = HashGenerator.generateHash(ep_name.concat(ep_email).concat(ep_ownerAdhaarCard));
                StringBuilder sb = new StringBuilder("insert into EXPENSE_PARTY (NAME,EMAIL, OWNER_ADHAAR_NUMBER, ORGANISATION, MEMORABLE_DATE,PASSWORD,PARTY_HASH) VALUES(");
                sb.append("'" + ep_name + "',").append("'" + ep_email + "',").append("'" + ep_ownerAdhaarCard + "',").append("'" + ep_organisation + "',").append("'" + ep_memDate + "',").append("'" + ep_password + "',").append("'" + ep_partyHash + "');");
                String sql = sb.toString();
                System.out.println(sql);
                pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                pstmt.execute();
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                StringBuilder sbB = new StringBuilder("insert into ACCESS(EMAIL, PASSWORD, FAILED_ATTEMPTS,ACCOUNT_LOCKED,LOCK_TIME, ACCESS_TYPE) VALUES(");
                sbB.append("'" + ep_email + "',").append("'" + ep_password + "',").append(0).append(",").append(0).append(",").append("'" + new Timestamp(System.currentTimeMillis())+"',")
                        .append("'EXPENSE_PARTY');");
                String sqlStmtB = sbB.toString();
                System.out.println(sqlStmtB);
                PreparedStatement stmtB = con.prepareStatement(sqlStmtB);
                stmtB.execute();
                if (generatedKeys.next()) {
                    //Create 3 A/C for the party and increment acctCt after each A/C
                    int partyId = generatedKeys.getInt(1);
                    System.out.println("Generated Key is: " + partyId);
                    ExpenseAccount ea1 = new ExpenseAccount();
                    ea1.name = expenseCategories.get(acctCt).getExpenseCategory();
                    ea1.expCatId = expenseCategories.get(acctCt).getId();
                    ea1.expAcctHash = HashGenerator.generateHash(expenseCategories.get(acctCt).getExpenseCategory());
                    ea1.createdOn = new Timestamp(System.currentTimeMillis());
                    ea1.ytdBalance = 0;
                    ea1.expPartyId = partyId;
                    StringBuilder sbA = new StringBuilder("insert into EXPENSE_ACCOUNT(NAME,EXPENSE_ACCOUNT_HASH, EXPENSE_CATEGORY_ID, EXPENSE_PARTY_ID,CREATED_ON,YTD_BALANCE, YEAR) VALUES(");
                    sbA.append("'" + ea1.name + "',").append("'" + ea1.expAcctHash + "',").append(ea1.expCatId + ",").append(+ea1.expPartyId + ",").append("'" + ea1.createdOn + "',").append(+ea1.ytdBalance +",").append(+FinancialYear.financialYear()+ ");");
                    String sqlStmtA = sbA.toString();
                    System.out.println(sqlStmtA);
                    PreparedStatement stmtA = con.prepareStatement(sqlStmtA);
                    stmtA.execute();
                    acctCt++;

                    ExpenseAccount ea2 = new ExpenseAccount();
                    ea2.name = expenseCategories.get(acctCt).getExpenseCategory();
                    ea2.expCatId = expenseCategories.get(acctCt).getId();
                    ea2.expAcctHash = HashGenerator.generateHash(expenseCategories.get(acctCt).getExpenseCategory());
                    ea2.createdOn = new Timestamp(System.currentTimeMillis());
                    ea2.ytdBalance = 0;
                    ea2.expPartyId = partyId;
                    sbA = new StringBuilder("insert into EXPENSE_ACCOUNT(NAME,EXPENSE_ACCOUNT_HASH, EXPENSE_CATEGORY_ID, EXPENSE_PARTY_ID,CREATED_ON,YTD_BALANCE,YEAR) VALUES(");
                    sbA.append("'" + ea2.name + "',").append("'" + ea2.expAcctHash + "',").append(ea2.expCatId + ",").append(+ea2.expPartyId + ",").append("'" + ea2.createdOn + "',").append(+ea2.ytdBalance+",").append(+FinancialYear.financialYear()+ ");");
                    sqlStmtA = sbA.toString();
                    System.out.println(sqlStmtA);
                    stmtA = con.prepareStatement(sqlStmtA);
                    stmtA.execute();
                    acctCt++;

                    ExpenseAccount ea3 = new ExpenseAccount();
                    ea3.name = expenseCategories.get(acctCt).getExpenseCategory();
                    ea3.expCatId = expenseCategories.get(acctCt).getId();
                    ea3.expAcctHash = HashGenerator.generateHash(expenseCategories.get(acctCt).getExpenseCategory());
                    ea3.createdOn = new Timestamp(System.currentTimeMillis());
                    ea3.ytdBalance = 0;
                    ea3.expPartyId = partyId;
                    sbA = new StringBuilder("insert into EXPENSE_ACCOUNT(NAME,EXPENSE_ACCOUNT_HASH, EXPENSE_CATEGORY_ID, EXPENSE_PARTY_ID,CREATED_ON,YTD_BALANCE,YEAR) VALUES(");
                    sbA.append("'" + ea3.name + "',").append("'" + ea3.expAcctHash + "',").append(ea3.expCatId + ",").append(+ea3.expPartyId + ",").append("'" + ea3.createdOn + "',").append(+ea3.ytdBalance +",").append(+FinancialYear.financialYear()+ ");");
                    sqlStmtA = sbA.toString();
                    System.out.println(sqlStmtA);
                    stmtA = con.prepareStatement(sqlStmtA);
                    stmtA.execute();
                    acctCt++;
                }
            }
            //Create for remaining 2 Accounts
            //Create 1 Remaining Party
            //Create remaining A/Cs 2 in our case
            String ep_name = "EP" + partyCt + " Bjm"; //Create for remaining 2 Accounts
            String ep_email = "ep" + partyCt + "@bjmbc.net";
            LocalDate ep_memDate = LocalDate.of(1973, Month.NOVEMBER, 27);
            String ep_ownerAdhaarCard = "1234567890";
            String ep_organisation = "Bjmbc";
            String ep_password = (PasswordUtil.generateSecurePassword("IL@ve2nu69", ep_email));
            String ep_partyHash = HashGenerator.generateHash(ep_name.concat(ep_email).concat(ep_ownerAdhaarCard));
            StringBuilder sb = new StringBuilder("insert into EXPENSE_PARTY (NAME,EMAIL, OWNER_ADHAAR_NUMBER, ORGANISATION, MEMORABLE_DATE,PASSWORD,PARTY_HASH) VALUES(");
                sb.append("'" + ep_name + "',").append("'" + ep_email + "',").append("'" + ep_ownerAdhaarCard + "',").append("'" + ep_organisation + "',").append("'" + ep_memDate + "',").append("'" + ep_password + "',").append("'" + ep_partyHash + "');");
            String sql = sb.toString();
            System.out.println(sql);
            pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.execute();
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            StringBuilder sbB = new StringBuilder("insert into ACCESS(EMAIL, PASSWORD, FAILED_ATTEMPTS,ACCOUNT_LOCKED,LOCK_TIME, ACCESS_TYPE) VALUES(");
               sbB.append("'" + ep_email + "',").append("'" + ep_password + "',").append(0).append(",").append(0).append(",").append("'" + new Timestamp(System.currentTimeMillis())+"',")
                .append("'EXPENSE_PARTY');");
            String sqlStmtB = sbB.toString();
            System.out.println(sqlStmtB);
            if (generatedKeys.next()) {
                //Create 23 A/C for the party and increment acctCt after each A/C
                int partyId = generatedKeys.getInt(1);
                System.out.println("Generated Key is: " + partyId);
                ExpenseAccount ea1 = new ExpenseAccount();
                ea1.name = expenseCategories.get(acctCt).getExpenseCategory();
                ea1.expCatId = expenseCategories.get(acctCt).getId();
                ea1.expAcctHash = HashGenerator.generateHash(expenseCategories.get(acctCt).getExpenseCategory());
                ea1.createdOn = new Timestamp(System.currentTimeMillis());
                ea1.ytdBalance = 0;
                ea1.expPartyId = partyId;
                StringBuilder sbA = new StringBuilder("insert into EXPENSE_ACCOUNT(NAME,EXPENSE_ACCOUNT_HASH, EXPENSE_CATEGORY_ID, EXPENSE_PARTY_ID,CREATED_ON,YTD_BALANCE,YEAR) VALUES(");
                sbA.append("'" + ea1.name + "',").append("'" + ea1.expAcctHash + "',").append(ea1.expCatId + ",").append(+ea1.expPartyId + ",").append("'" + ea1.createdOn + "',").append(+ea1.ytdBalance +",").append(+FinancialYear.financialYear()+ ");");
                String sqlStmtA = sbA.toString();
                System.out.println(sqlStmtA);
                PreparedStatement stmtA = con.prepareStatement(sqlStmtA);
                stmtA.execute();
                acctCt++;

                ExpenseAccount ea2 = new ExpenseAccount();
                ea2.name = expenseCategories.get(acctCt).getExpenseCategory();
                ea2.expCatId = expenseCategories.get(acctCt).getId();
                ea2.expAcctHash = HashGenerator.generateHash(expenseCategories.get(acctCt).getExpenseCategory());
                ea2.createdOn = new Timestamp(System.currentTimeMillis());
                ea2.ytdBalance = 0;
                ea2.expPartyId = partyId;
                sbA = new StringBuilder("insert into EXPENSE_ACCOUNT(NAME,EXPENSE_ACCOUNT_HASH, EXPENSE_CATEGORY_ID, EXPENSE_PARTY_ID,CREATED_ON,YTD_BALANCE,YEAR) VALUES(");
                sbA.append("'" + ea2.name + "',").append("'" + ea2.expAcctHash + "',").append(ea2.expCatId + ",").append(+ea2.expPartyId + ",").append("'" + ea2.createdOn + "',").append(+ea2.ytdBalance +",").append(+FinancialYear.financialYear()+ ");");
                sqlStmtA = sbA.toString();
                System.out.println(sqlStmtA);
                stmtA = con.prepareStatement(sqlStmtA);
                stmtA.execute();
                acctCt++;
            }
            
            
            
        }catch (SQLException ex) {
            Logger.getLogger(DataGen.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataGen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
               
    }

    private void processRevenue() {
        List<RevenueCategory> revenueCategories=new ArrayList<>();
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bjmbc", "root", "IL@ve2nu69");
            Statement stmt;
            stmt = con.createStatement();
            
            ResultSet rs=stmt.executeQuery("SELECT ID, YEAR, REVENUE_CATEGORY FROM REVENUE_CATEGORY");
            while(rs.next()){
                RevenueCategory rc=new RevenueCategory();
                rc.setId(rs.getInt("ID"));
                rc.setYear(rs.getInt("YEAR"));
                rc.setRevenueCategory(rs.getString("REVENUE_CATEGORY"));
                revenueCategories.add(rc);
            }
            System.out.println(String.format("%d RevenueCategory(s) loaded.", revenueCategories.size()));
            int ctAccts = revenueCategories.size();
            int ctPW = revenueCategories.size() / 3;//ct party [whole] is 6 -
            int ctPR = 0;//ct party [remainder] for remaining A/Cs < 3
            int partyCt = 0;//Count parties as we create them
            int acctCt = 0;//Count accounts as we create them
            PreparedStatement pstmt=null;
            for (partyCt = 1; partyCt <= ctPW; partyCt++) {
                //Create Party
                String rp_name = "RP" + partyCt + " Bjm";
                String rp_email = "rp" + partyCt + "@bjmbc.net";
                LocalDate rp_memDate = LocalDate.of(1973, Month.NOVEMBER, 27);
                String rp_ownerAdhaarCard = "1234567890";
                String rp_organisation = "Bjmbc";
                String rp_password = (PasswordUtil.generateSecurePassword("IL@ve2nu69", rp_email));
                String rp_partyHash = HashGenerator.generateHash(rp_name.concat(rp_email).concat(rp_ownerAdhaarCard));
                StringBuilder sb = new StringBuilder("insert into REVENUE_PARTY (NAME,EMAIL, OWNER_ADHAAR_NUMBER, ORGANISATION, MEMORABLE_DATE,PASSWORD,PARTY_HASH) VALUES(");
                sb.append("'" + rp_name + "',").append("'" + rp_email + "',").append("'" + rp_ownerAdhaarCard + "',").append("'" + rp_organisation + "',").append("'" + rp_memDate + "',").append("'" + rp_password + "',").append("'" + rp_partyHash + "');");
                String sql = sb.toString();
                System.out.println(sql);
                pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                pstmt.execute();
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                StringBuilder sbB = new StringBuilder("insert into ACCESS(EMAIL, PASSWORD, FAILED_ATTEMPTS,ACCOUNT_LOCKED,LOCK_TIME, ACCESS_TYPE) VALUES(");
                sbB.append("'" + rp_email + "',").append("'" + rp_password + "',").append(0).append(",").append(0).append(",").append("'" + new Timestamp(System.currentTimeMillis())+"',")
                        .append("'REVENUE_PARTY');");
                String sqlStmtB = sbB.toString();
                System.out.println(sqlStmtB);
                PreparedStatement stmtB = con.prepareStatement(sqlStmtB);
                stmtB.execute();
                if (generatedKeys.next()) {
                    //Create 3 A/C for the party and increment acctCt after each A/C
                    int partyId = generatedKeys.getInt(1);
                    System.out.println("Generated Key is: " + partyId);
                    RevenueAccount ra1 = new RevenueAccount();
                    ra1.name = revenueCategories.get(acctCt).getRevenueCategory();
                    ra1.revCatId = revenueCategories.get(acctCt).getId();
                    ra1.revAcctHash = HashGenerator.generateHash(revenueCategories.get(acctCt).getRevenueCategory());
                    ra1.createdOn = new Timestamp(System.currentTimeMillis());
                    ra1.ytdBalance = 0;
                    ra1.revPartyId = partyId;
                    StringBuilder sbA1 = new StringBuilder("insert into REVENUE_ACCOUNT(NAME,REVENUE_ACCOUNT_HASH, REVENUE_CATEGORY_ID, REVENUE_PARTY_ID,CREATED_ON,YTD_BALANCE, YEAR) VALUES(");
                    sbA1.append("'" + ra1.name + "',").append("'" + ra1.revAcctHash + "',").append(ra1.revCatId + ",").append(+ra1.revPartyId + ",").append("'" + ra1.createdOn + "',").append(+ra1.ytdBalance +",").append(+FinancialYear.financialYear()+ ");");
                    String sqlStmt = sbA1.toString();
                    System.out.println(sqlStmt);
                    PreparedStatement pstmtAc = con.prepareStatement(sqlStmt);
                    pstmtAc.execute();
                    acctCt++;

                    RevenueAccount ra2 = new RevenueAccount();
                    ra2.name = revenueCategories.get(acctCt).getRevenueCategory();
                    ra2.revCatId = revenueCategories.get(acctCt).getId();
                    ra2.revAcctHash = HashGenerator.generateHash(revenueCategories.get(acctCt).getRevenueCategory());
                    ra2.createdOn = new Timestamp(System.currentTimeMillis());
                    ra2.ytdBalance = 0;
                    ra2.revPartyId = partyId;
                    StringBuilder sbA2 = new StringBuilder("insert into REVENUE_ACCOUNT(NAME,REVENUE_ACCOUNT_HASH, REVENUE_CATEGORY_ID, REVENUE_PARTY_ID,CREATED_ON,YTD_BALANCE, YEAR) VALUES(");
                    sbA2.append("'" + ra2.name + "',").append("'" + ra2.revAcctHash + "',").append(ra2.revCatId + ",").append(+ra2.revPartyId + ",").append("'" + ra2.createdOn + "',").append(+ra2.ytdBalance +",").append(+FinancialYear.financialYear()+ ");");
                    sqlStmt = sbA2.toString();
                    System.out.println(sqlStmt);
                    pstmtAc = con.prepareStatement(sqlStmt);
                    pstmtAc.execute();
                    acctCt++;

                    RevenueAccount ra3 = new RevenueAccount();
                    ra3.name = revenueCategories.get(acctCt).getRevenueCategory();
                    ra3.revCatId = revenueCategories.get(acctCt).getId();
                    ra3.revAcctHash = HashGenerator.generateHash(revenueCategories.get(acctCt).getRevenueCategory());
                    ra3.createdOn = new Timestamp(System.currentTimeMillis());
                    ra3.ytdBalance = 0;
                    ra3.revPartyId = partyId;
                    StringBuilder sbA3 = new StringBuilder("insert into REVENUE_ACCOUNT(NAME,REVENUE_ACCOUNT_HASH, REVENUE_CATEGORY_ID, REVENUE_PARTY_ID,CREATED_ON,YTD_BALANCE, YEAR) VALUES(");
                    sbA3.append("'" + ra3.name + "',").append("'" + ra3.revAcctHash + "',").append(ra3.revCatId + ",").append(+ra3.revPartyId + ",").append("'" + ra3.createdOn + "',").append(+ra3.ytdBalance +",").append(+FinancialYear.financialYear()+ ");");
                    sqlStmt = sbA3.toString();
                    System.out.println(sqlStmt);
                    pstmtAc = con.prepareStatement(sqlStmt);
                    pstmtAc.execute();
                    acctCt++;
                }
            }
        
            
        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
        }

        
    }

    private void processRevenueAllocation() {
        //List<RevenueAllocation> revenueAllocation=new ArrayList();
        BufferedReader reader;
        
        try {
            reader=new BufferedReader(new FileReader(REV_ALLOC));
            String line= reader.readLine();//First line is a header that we don't need
            StringBuilder sbE=new StringBuilder("INSERT INTO REVENUE_ALLOCATION(YEAR,CATEGORY,ALLOCATION, PERCENT_ALLOCATION) VALUES ");
            line= reader.readLine();
            while(line != null){
                //System.out.println(line);
                StringTokenizer tokenizer=new StringTokenizer(line,",");
                while (tokenizer.hasMoreElements()){
                    String year = tokenizer.nextToken();
                    String category=tokenizer.nextToken();
                    String allocation=tokenizer.nextToken();
                    String percentAllocation=tokenizer.nextToken();
                    sbE.append("(").append(year).append(",'").append(category).append("',").append(allocation).append(",").append(percentAllocation).append("),");
                    //System.out.println("YEAR: "+tokenizer.nextToken()+" ExpenseCategory: "+tokenizer.nextToken());
                }
                line= reader.readLine();
            }
            sbE.replace(sbE.lastIndexOf(","), sbE.lastIndexOf(",")+1, ";");
            System.out.println(sbE.toString());
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PW);
            PreparedStatement pstmt;
            pstmt = con.prepareStatement(sbE.toString());
            int rows=pstmt.executeUpdate();
            System.out.println(String.format("REVENUE_ALLOCATION populated with %d records",rows));
                
            reader.close();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataGen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }

    private void processExpenseAllocation() {
        BufferedReader reader;
        
        try {
            reader=new BufferedReader(new FileReader(EXP_ALLOC));
            String line= reader.readLine();//First line is a header that we don't need
            StringBuilder sbE=new StringBuilder("INSERT INTO EXPENSE_ALLOCATION(YEAR,CATEGORY,ALLOCATION, PERCENT_ALLOCATION) VALUES ");
            line= reader.readLine();
            while(line != null){
                //System.out.println(line);
                StringTokenizer tokenizer=new StringTokenizer(line,",");
                while (tokenizer.hasMoreElements()){
                    String year = tokenizer.nextToken();
                    String category=tokenizer.nextToken();
                    String allocation=tokenizer.nextToken();
                    String percentAllocation=tokenizer.nextToken();
                    sbE.append("(").append(year).append(",'").append(category).append("',").append(allocation).append(",").append(percentAllocation).append("),");
                    //System.out.println("YEAR: "+tokenizer.nextToken()+" ExpenseCategory: "+tokenizer.nextToken());
                }
                line= reader.readLine();
            }
            sbE.replace(sbE.lastIndexOf(","), sbE.lastIndexOf(",")+1, ";");
            System.out.println(sbE.toString());
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PW);
            PreparedStatement pstmt;
            pstmt = con.prepareStatement(sbE.toString());
            int rows=pstmt.executeUpdate();
            System.out.println(String.format("EXPENSE_ALLOCATION populated with %d records",rows));
                
            reader.close();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataGen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    class ExpenseCategory{
        int id;
        int year;
        String expenseCategory;
        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        public int getYear() {
            return year;
        }
        public void setYear(int year) {
            this.year = year;
        }
        public String getExpenseCategory() {
            return expenseCategory;
        }
        public void setExpenseCategory(String expenseCategory) {
            this.expenseCategory = expenseCategory;
        }
    }
    
    class RevenueCategory{
        int id;
        int year;
        String revenueCategory;
        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        public int getYear() {
            return year;
        }
        public void setYear(int year) {
            this.year = year;
        }

        public String getRevenueCategory() {
            return revenueCategory;
        }

        public void setRevenueCategory(String revenueCategory) {
            this.revenueCategory = revenueCategory;
        }
        
    }

    class ExpenseParty {

        String name;
        String email;
        LocalDate memDate;
        String ownerAdhaarCard;
        String organisation;
        String password;
        String partyHash;
    }

    class ExpenseAccount {

        String name;
        String expAcctHash;
        int expCatId;
        int expPartyId;
        Timestamp createdOn;
        double ytdBalance;

    }

    class RevenueParty {

        String name;
        String email;
        LocalDate memDate;
        String ownerAdhaarCard;
        String organisation;
        String password;
        String partyHash;
    }

    class RevenueAccount {

        String name;
        String revAcctHash;
        int revCatId;
        int revPartyId;
        Timestamp createdOn;
        double ytdBalance;
    }
    
    class ExpenseAllocation{
        int year;
        String category;
        double allocation;
        double percentAllocation;
    }
    
    class RevenueAllocation{
        int year;
        String category;
        double allocation;
        double percentAllocation;
    }

}
