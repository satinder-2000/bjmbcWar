/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bjm.bc.mbean;

import bjm.bc.ejb.ExpenseCategoryEjbLocal;
import bjm.bc.ejb.ExpensePartyEjbLocal;
import bjm.bc.model.ExpenseAccount;
import bjm.bc.model.ExpenseCategory;
import bjm.bc.model.ExpenseParty;
import bjm.bc.util.BJMConstants;
import bjm.bc.util.FinancialYear;
import bjm.bc.util.HashGenerator;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.flow.FlowScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author user
 */
@Named(value = "expensePartyRegisterMBean")
@FlowScoped("ExpensePartyRegister")
public class ExpensePartyRegisterMBean implements Serializable {
    
    private static final Logger LOGGER=Logger.getLogger(ExpensePartyRegisterMBean.class.getName());
    
    
    private ExpenseParty expenseParty;
    
    private List<ExpenseCategory> expenseCategories;
    private List<String> expenseCategoriesStr;
    private String[] partyExpenseCategories;
    private String memorableDateStr;
    
    @Inject
    private ExpenseCategoryEjbLocal expenseCategoryEjbLocal;
    @Inject
    private ExpensePartyEjbLocal expensePartyEjbLocal;
    
    @PostConstruct
    public void init(){
        expenseCategories= new ArrayList<>();
        expenseCategoriesStr = new ArrayList<>();
        expenseCategories.addAll(expenseCategoryEjbLocal.getExpenseCategoriesForYear(FinancialYear.financialYear()));
        for (ExpenseCategory ec: expenseCategories){
            expenseCategoriesStr.add(ec.getExpenseCategory());
        }
        
        LOGGER.info(String.format("Total ExpenseCategories for year %d are %d",FinancialYear.financialYear(), expenseCategories.size()));
        expenseParty= new ExpenseParty();
        
        LOGGER.info("ExpenseParty initialised.");
    }
    
    public String amendDetails(){
        return "ExpensePartyRegister?faces-redirect=true";
    }
    
    public String validateExpenseParty(){
        String toReturn =null;
        FacesContext context=FacesContext.getCurrentInstance();
        //Validate name
        if(expenseParty.getName().isEmpty()){
            context.addMessage("name",new FacesMessage(FacesMessage.SEVERITY_ERROR, "Name not entered","Name not entered"));
        }else if (expenseParty.getName().length()<2 || expenseParty.getName().length()>45){
            context.addMessage("name",new FacesMessage(FacesMessage.SEVERITY_ERROR, "Chars in Name must be 2 to 45","Chars in Name must be 2 to 45"));
        }
        //Validate Email
        if(expenseParty.getEmail().isEmpty()){
            context.addMessage("email",new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email not entered","Email not entered"));
        }else{
            Pattern p = Pattern.compile(BJMConstants.EMAIL_REGEX);
            Matcher m = p.matcher(expenseParty.getEmail());
            if (!m.find()){
                context.addMessage("email",new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Email","Invalid Email"));
            }else{
                boolean isEmailRegistered = expensePartyEjbLocal.isEmailRegistered(expenseParty.getEmail());
                if (isEmailRegistered){
                   context.addMessage("email",new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email registered already.","Email registered already."));
                }
            }
        }
        //Memorable Date
        if(!memorableDateStr.isEmpty()){
            DateTimeFormatter formatter= DateTimeFormatter.ofPattern("dd/MM/yyyy");
            expenseParty.setMemorableDate(LocalDate.parse(memorableDateStr,formatter));
        }
        //Expense Categories of the Party now
        for (String expCat: partyExpenseCategories){
            ExpenseAccount ea=new ExpenseAccount();
            ea.setExpenseAccountHash(HashGenerator.generateHash(expCat));
            ExpenseCategory ec = expenseCategoryEjbLocal.findByNameAndYear(expCat, FinancialYear.financialYear());
            ea.setExpenseCategoryId(ec.getId());
            //Will attach the ExpenseParty Id in the EJB, when the Party ID becomes available.
            if (expenseParty.getExpenseAccounts()==null){
                expenseParty.setExpenseAccounts(new ArrayList());
            }
            expenseParty.getExpenseAccounts().add(ea);
        }
        //Party Hash
        String partyHash=HashGenerator.generateHash(expenseParty.getName().concat(expenseParty.getEmail().concat(expenseParty.getOwnerAdhaarNumber())));
        expenseParty.setPartyHash(partyHash);
        //Direct now
        if(!context.getMessageList().isEmpty()){
            toReturn = null;
        }else{
            toReturn = "ExpensePartyRegisterConfirm?faces-redirect=true";
        }
        return toReturn;
    }
    
    public String getReturnValue(){
        return "/flowreturns/ExpensePartyRegister-return?faces-redirect=true";
    }

    public ExpenseParty getExpenseParty() {
        return expenseParty;
    }

    public void setExpenseParty(ExpenseParty expenseParty) {
        this.expenseParty = expenseParty;
    }

    public List<ExpenseCategory> getExpenseCategories() {
        return expenseCategories;
    }

    public void setExpenseCategories(List<ExpenseCategory> expenseCategories) {
        this.expenseCategories = expenseCategories;
    }

    public String[] getPartyExpenseCategories() {
        return partyExpenseCategories;
    }

    public void setPartyExpenseCategories(String[] partyExpenseCategories) {
        this.partyExpenseCategories = partyExpenseCategories;
    }

    public List<String> getExpenseCategoriesStr() {
        return expenseCategoriesStr;
    }

    public void setExpenseCategoriesStr(List<String> expenseCategoriesStr) {
        this.expenseCategoriesStr = expenseCategoriesStr;
    }

    public String getMemorableDateStr() {
        return memorableDateStr;
    }

    public void setMemorableDateStr(String memorableDateStr) {
        this.memorableDateStr = memorableDateStr;
    }
    
    
}
