/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bjm.bc.mbean;

import bjm.bc.ejb.ExpenseCategoryEjbLocal;
import bjm.bc.ejb.ExpensePartyEjbLocal;
import bjm.bc.ejb.exception.UserRegisteredAlreadyException;
import bjm.bc.model.ExpenseAccount;
import bjm.bc.model.ExpenseCategory;
import bjm.bc.model.ExpenseParty;
import bjm.bc.model.RevenueAccount;
import bjm.bc.model.RevenueCategory;
import bjm.bc.util.BJMConstants;
import bjm.bc.util.FinancialYear;
import bjm.bc.util.HashGenerator;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.flow.FlowScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;

/**
 *
 * @author user
 */
//@Named(value = "expensePartyRegisterMBeanDepr")
//@FlowScoped("ExpensePartyRegisterD")
public class ExpensePartyRegisterMBeanDEPR implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(ExpensePartyRegisterMBeanDEPR.class.getName());
    private ExpenseParty expenseParty;
    private List<ExpenseCategory> expenseCatgories;
    private List<String> expenseCategoriesStr;
    private String[] partyExpenseCategories;
    private String memorableDateStr;
    
    @Inject
    private ExpenseCategoryEjbLocal expenseCategoryEjbLocal;
    
    @Inject
    private ExpensePartyEjbLocal expensePartyEjbLocal;
    
    @PostConstruct
    public void init(){
        expenseParty = new ExpenseParty();
        expenseCatgories= new ArrayList<>();
        expenseCategoriesStr = new ArrayList<>();
        expenseCatgories.addAll(expenseCategoryEjbLocal.getExpenseCategoriesForYear(FinancialYear.financialYear()));
        for (ExpenseCategory ec: expenseCatgories){
            expenseCategoriesStr.add(ec.getExpenseCategory());
        }
        LOGGER.info("New ExpenseParty initialised");
    }
    
    public String amendDetails(){
        return "ExpensePartyRegister?faces-redirect=true";
    }
    
    public String validateExpenseParty() {
        String toReturn = null;
        //FacesContext context = FacesContext.getCurrentInstance();
        //ResourceBundle rb = context.getApplication().evaluateExpressionGet(context, "#{msg}", ResourceBundle.class);
        //Name
        if(expenseParty.getName().isEmpty()){
           FacesContext.getCurrentInstance().addMessage("name", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Name is required", "Name is required")); 
        }else if(expenseParty.getName().length()<2 || expenseParty.getName().length()>45){
            FacesContext.getCurrentInstance().addMessage("name", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Name must have 2-45 Chars","Name must have 2-45 Chars"));
        }
        //Validate email if Exists
        if (expenseParty.getEmail().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage("email", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email is required","Email is required"));
        } else {//Email Regex validation
            Pattern p = Pattern.compile(BJMConstants.EMAIL_REGEX);
            Matcher m = p.matcher(expenseParty.getEmail());
            if (!m.find()) {
                FacesContext.getCurrentInstance().addMessage("email", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email is Invalid", "Email is Invalid"));
            } else {
                boolean isEmailRegistered = expensePartyEjbLocal.isEmailRegistered(expenseParty.getEmail());
                if (isEmailRegistered) {
                    FacesContext.getCurrentInstance().addMessage("email", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email is taken", "Email is taken"));
                    
                }
            
            }

        }
        
        //Memorable Date now
        if (!memorableDateStr.isEmpty()){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            expenseParty.setMemorableDate(LocalDate.parse(memorableDateStr, formatter));
        }
        
        //Create ExpensePartyAccounts
        for(String expCat : partyExpenseCategories){
            ExpenseAccount ea= new ExpenseAccount();
            ea.setExpenseAccountHash(HashGenerator.generateHash(expCat));
            ExpenseCategory ec=expenseCategoryEjbLocal.findByNameAndYear(expCat, FinancialYear.financialYear());
            ea.setExpenseCategoryId(ec.getId());
            //Will attach the ExpenseParty Id in the EJB, when the ID becomes available.
            if (expenseParty.getExpenseAccounts()==null){
                expenseParty.setExpenseAccounts(new ArrayList<ExpenseAccount>());
            }
            expenseParty.getExpenseAccounts().add(ea);
        }
        
        //finally create PartyHash
        String partyHash=HashGenerator.generateHash(expenseParty.getName().concat(expenseParty.getEmail()).concat(expenseParty.getOwnerAdhaarNumber()));
	expenseParty.setPartyHash(partyHash);
        
        if (!FacesContext.getCurrentInstance().getMessageList().isEmpty()){
            toReturn =null; //generate same page with errors
        }else{
            toReturn = "ExpensePartyRegisterConfirm?faces-redirect=true";
        }
        return toReturn;
    }
    
    public void submitExpenseParty(){
        try{
            expenseParty = expensePartyEjbLocal.createExpenseParty(expenseParty);
            LOGGER.info(String.format("ExpenseParty created with ID; %d", expenseParty.getId()));
        }catch(UserRegisteredAlreadyException ex1){
            LOGGER.severe(ex1.getMessage());
            ex1.printStackTrace();
        }catch(MessagingException ex2){
            LOGGER.severe(ex2.getMessage());
            ex2.printStackTrace();
        }
    }
    
    public String getReturnValue(){
        submitExpenseParty();
        return "/flowreturns/ExpensePartyRegister-return?faces-redirect=true";
    }
    
    public ExpenseParty getExpenseParty() {
        return expenseParty;
    }

    public void setExpenseParty(ExpenseParty expenseParty) {
        this.expenseParty = expenseParty;
    }

    public List<ExpenseCategory> getExpenseCatgories() {
        return expenseCatgories;
    }

    public void setExpenseCatgories(List<ExpenseCategory> expenseCatgories) {
        this.expenseCatgories = expenseCatgories;
    }

    public List<String> getExpenseCategoriesStr() {
        return expenseCategoriesStr;
    }

    public void setExpenseCategoriesStr(List<String> expenseCategoriesStr) {
        this.expenseCategoriesStr = expenseCategoriesStr;
    }

    public String[] getPartyExpenseCategories() {
        return partyExpenseCategories;
    }

    public void setPartyExpenseCategories(String[] partyExpenseCategories) {
        this.partyExpenseCategories = partyExpenseCategories;
    }

    public String getMemorableDateStr() {
        return memorableDateStr;
    }

    public void setMemorableDateStr(String memorableDateStr) {
        this.memorableDateStr = memorableDateStr;
    }
    
    
    
    
    
    
}
