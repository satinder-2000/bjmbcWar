package bjm.bc.util;

import java.time.LocalDate;
import java.time.Month;

public class FinancialYear {
	
	public static int financialYear() {
		int finYear=0;
		Month month = LocalDate.now().getMonth();
		if (month.equals(Month.JANUARY)) {
			finYear=LocalDate.now().getYear()-1;
		}else {
			finYear=LocalDate.now().getYear();
		}
		return finYear;
	}

}