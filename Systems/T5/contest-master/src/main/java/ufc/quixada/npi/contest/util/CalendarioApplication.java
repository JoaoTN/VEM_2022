package ufc.quixada.npi.contest.util;

import java.util.Calendar;
import java.util.Date;

public class CalendarioApplication {
	private static Calendar calendar = Calendar.getInstance();
	
	public static Date getDateCalendario(Date terminoSubmissao, int value) {
		setTime(terminoSubmissao);
		addCalendario(value);
		return getTimeCalendario();
	}
	
	public static boolean equalDateToday(Date data) {
		Date dataAtual = new Date();
		Date dataComper = CalendarioApplication.getDateCalendario(data, 1);
		return (dataAtual.compareTo(dataComper) >= 0);
	}
	
	private static void setTime(Date terminoSubmissao) {
		calendar.setTime(terminoSubmissao);
	}
	
	private static void addCalendario(int value) {
		calendar.add(Calendar.DAY_OF_MONTH, value);
	}
	
	private static Date getTimeCalendario() {
		return calendar.getTime();
	}
}
