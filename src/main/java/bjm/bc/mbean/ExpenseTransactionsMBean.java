package bjm.bc.mbean;

import bjm.bc.ejb.ExpenseAccountEjbLocal;
import bjm.bc.model.ExpenseAccount;
import bjm.bc.model.ExpenseAccountTransaction;
import bjm.bc.util.FinancialYear;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.annotation.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author user
 */
@Named(value = "expenseTransactionsMBean")
@RequestScoped
public class ExpenseTransactionsMBean {
    
    private static Logger LOGGER=Logger.getLogger(ExpenseTransactionsMBean.class.getName());
    
    @Inject
    private ExpenseAccountEjbLocal expenseAccountEjbLocal;
    
    private ExpenseAccount expenseAccount;
    
    private List<ExpenseAccountTransaction> expenseAccountTransactions;
    
    //@ManagedProperty(value = "#{param.acctId}")
    int accountId;
    
    private int year;
    
    @PostConstruct
    public void init(){
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        accountId=Integer.parseInt(request.getParameter("acctId"));
        expenseAccount = expenseAccountEjbLocal.findById(accountId);
        year=FinancialYear.financialYear();
        expenseAccountTransactions=expenseAccountEjbLocal.getExpenseAccountTransactions(accountId, year );
        LOGGER.info(String.format("ExpenseAccount %d loaded along with %d ExpenseAccountTransactions.", accountId,expenseAccountTransactions.size()));
    }
    
    public String viewTransactions(){
        return "ExpenseAccountsTransactions";
    }

    public ExpenseAccount getExpenseAccount() {
        return expenseAccount;
    }

    public void setExpenseAccount(ExpenseAccount expenseAccount) {
        this.expenseAccount = expenseAccount;
    }

    public List<ExpenseAccountTransaction> getExpenseAccountTransactions() {
        return expenseAccountTransactions;
    }

    public void setExpenseAccountTransactions(List<ExpenseAccountTransaction> expenseAccountTransactions) {
        this.expenseAccountTransactions = expenseAccountTransactions;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
    
    
    
}
