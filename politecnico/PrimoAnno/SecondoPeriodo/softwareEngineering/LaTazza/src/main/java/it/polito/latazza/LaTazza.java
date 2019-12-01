package it.polito.latazza;
import java.sql.SQLException;
/*import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;*/

import it.polito.latazza.data.DataImpl;
import it.polito.latazza.data.DataInterface;
//import it.polito.latazza.data.Database;
import it.polito.latazza.gui.MainSwing;

public class LaTazza {
	//double balance=0;
	public static void main(String[] args) throws ClassNotFoundException, SQLException, Exception{
		
		DataInterface data = new DataImpl();
		//Database database = new Database();
		//Date date= new Date();
		//data.reset();
		//Integer id=data.createEmployee("ndjekoua", "sandjo");
		//data.createBeverage("coffee",10, 100);
		//data.getEmployeeReport(id,date, new Date());
		//System.out.println(data.getReport(shiftDate(-1), date));
		new MainSwing(data);
	}
	
	/*public static Date shiftDate(int dayNumber) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, dayNumber); // number represents number of days
		Date shiftedDate = cal.getTime();
		return shiftedDate;
	}*/
	
}
