package ufc.quixada.npi.contest.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ValidacaoPeriodo {
	private static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	
	public static Date formatterDateToday() throws ParseException {
		Date hoje = new Date();
		return formatter.parse(formatter.format(hoje));
	}
	
	public static boolean isPeriodo(Date isAfter, Date isBefore) {
		try {
			Date hoje = formatterDateToday();
				
			return (hoje.after(isAfter) && hoje.before(isBefore));
		} catch (ParseException e) {
			return false;
		}
	}
	
	
	public static boolean isAfterPeriod(Date isAfter) {
		try {
			Date hoje = formatterDateToday();
			return hoje.after(isAfter);
		} catch (ParseException e) {
			return false;
		}
	}
	
}
