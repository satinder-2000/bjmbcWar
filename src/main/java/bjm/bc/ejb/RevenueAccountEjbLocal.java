package bjm.bc.ejb;

import bjm.bc.model.RevenueAccount;
import bjm.bc.model.RevenueAccountTransaction;
import java.util.List;
import javax.ejb.Local;

@Local
public interface RevenueAccountEjbLocal {
    
    public RevenueAccount createRevenueAccount(RevenueAccount revenueAccount);
    public RevenueAccount findById(int id);
    public RevenueAccount saveRevenueAccount(RevenueAccount revenueAccount);
    public boolean addToBalanceRevenueAccount(int accountId, double balanceToAdd);
    public boolean createMoneyInRevenueAccount(RevenueAccountTransaction revenueAccountTransaction);
    public boolean createMoneyOutRevenueAccount(RevenueAccountTransaction revenueAccountTransaction);
    public List<RevenueAccountTransaction> getRevenueAccountTransactions(int accountId, int year);
    public List<RevenueAccount> getAll(int year);
}
