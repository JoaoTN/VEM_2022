package main;

import java.util.Date;
import java.util.Scanner;

import data.Flight;

public class MainSearch {
	private static MainServer server;
	private static Scanner scanner;
	
	Date date1 = null;
	Date date2 = null;
	int cityFromId = -1;
	int cityToId = -1;
	
	public MainSearch(MainServer ms_server, Scanner ms_scanner){
		server = ms_server;
		scanner = ms_scanner;
	}
	
	private static void systemMessage(String str) {
		System.out.println(str);
	}
	
	private Integer strToInteger(String str) {
		return Integer.valueOf(str);
	}
	
	private void printHeadInfo() {
		systemMessage("welcome to search page! you can input: \n"
				+ "\tcity CityFromId-CityToId\n"
				+ "\t\tset filter of city\n"
				+ "\tcity -\n"
				+ "\t\tclear the filter of city\n"
				+ "\tdate ( yyyy-mm-dd~yyyy-mm-dd | ~yyyy-mm-dd | yyyy-mm-dd~ | ~ )\n"
				+ "\t\tset filter of 'set off date' interval\n"
				+ "\tprint|p\tprint result using filter\n"
				+ "\texit|e\texit wizard\n\n"
				+ "\tavailibal city: \n");
	}
	
	private void printCurrentFilter() {
		systemMessage("current filter: \n"
			+ String.format("\tcity: %s-%s\n",
				cityFromId == -1 ? "unset" :
					String.valueOf(cityFromId),
				cityToId == -1 ? "unset" :
					String.valueOf(cityToId)
			)
			+ String.format("\tdate: %s~%s\n\n",
				date1 == null ? "unset" :
					date1.toString(),
				date2 == null ? "unset" :
				date2.toString()
			)
			+ "use 'print' or 'p' to print result\n");
	}
	
	private void searchByCity(String param) {
		try {
			String[] cityid = param.split("-");
			cityFromId = param.equals("-") ? -1 :
				param.startsWith("-") ? -1 :
					strToInteger(cityid[0]);
			cityToId = param.equals("-") ? -1 :
				param.endsWith("-") ? -1 :
					strToInteger(cityid[1]);
		} catch (NumberFormatException | IndexOutOfBoundsException e) {
			systemMessage("City ID format error");
		}
	}
	
	private void searchByDate(String param) {
		try {
			String[] s0 = null;
			String[] s1 = null;
			if (param.split("~").length >= 1) {
				s0 = param.split("~")[0].split("-");						
			} 
			if (param.split("~").length == 2) {
				s1 = param.split("~")[1].split("-");
			}
			date1 = param.equals("~") ? null : 
				param.startsWith("~") ? null :
					Flight.calendar(
							strToInteger(s0[0]),
							strToInteger(s0[1]),
							strToInteger(s0[2]), 0, 0, 0);
			date2 = param.equals("~") ? null : 
				param.endsWith("~") ? null : 
					Flight.calendar(
							strToInteger(s1[0]),
							strToInteger(s1[1]),
							strToInteger(s1[2]), 0, 0, 0);
		} catch (NumberFormatException | IndexOutOfBoundsException e) {
			systemMessage("Date format error");
		}
	}

	protected void search() {
		// DONE(Dong) search
		String cmd = "";
		cityFromId = -1;
		cityToId = -1;
		date1 = null;
		date2 = null;
		printHeadInfo();
		server.displayCity();
		do {
			printCurrentFilter();
			systemMessage(">>");
			String param;
			String input = scanner.nextLine();
			input = input.replaceAll("\\s+", " ");
			input = input.replaceAll("^\\s+", "");
			input = input.replaceAll("\\s+$", "");
			if (input.contains(" ")) {
				cmd = input.split(" ")[0];
				param = input.split(" ")[1];
			} else {
				cmd = input;
				param = "";
			}
			switch (cmd) {
			case "city":
				searchByCity(param);
				break;
			case "date":
				searchByDate(param);
				break;
			case "exit":
			case "e":
				break;
			case "print":
			case "p":
				server.search(cityFromId, cityToId, date1, date2);
				break;
			default:
				systemMessage("unknown command");
				break;
			}
		} while (!(cmd.equals("exit") || cmd.equals("e")));
	}
}
