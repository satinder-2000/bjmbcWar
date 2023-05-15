package bjm.bc.ejb;

import bjm.bc.model.RevenueAccount;
import javax.ejb.Local;

@Local
public interface RevenueAccountEjbLocal {
    
    public RevenueAccount createRevenueAccount(RevenueAccount revenueAccount);
    public RevenueAccount findById(int id);
    public RevenueAccount saveRevenueAccount(RevenueAccount revenueAccount);
    public boolean addToBalanceRevenueAccount(int accountId, double balanceToAdd);
}
