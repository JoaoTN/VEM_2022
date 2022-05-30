package main;

import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

import data.Flight;
import data.FlightDaemon;
import exceptions.PermissionDeniedException;

public class ControllerFlight {
	MainServer server;
	Scanner scanner;
	
	public ControllerFlight(MainServer server, Scanner scanner) {
		this.server = server;
		this.scanner = scanner;
	};
	
	public static void systemMessage(String str) {
		System.out.println(str);
	}
	
	private static Integer strToInteger(String str) {
		return Integer.valueOf(str);
	}
	
	private String scanNextLine() {
		return scanner.nextLine();
	}
	
	protected Date scanStartTime() {
		systemMessage("Please enter the Starttime, formatted with: year-month-date-hr-min-sec: ");
		String[] startime = scanNextLine().split("-");
		int year = Integer.parseInt(startime[0]);
		int month = Integer.parseInt(startime[1]);
		int date = Integer.parseInt(startime[2]);
		int hr = Integer.parseInt(startime[3]);
		int min = Integer.parseInt(startime[4]);
		int sec = Integer.parseInt(startime[5]);
		return Flight.calendar(year, month, date, hr, min, sec);
	}
	
	protected Date scanArriveTime() {
		systemMessage("Please enter the arrivetime, formatted with: year-month-date-hr-min-sec: ");
		String[] arrivetime = scanNextLine().split("-");
		int year1 = Integer.parseInt(arrivetime[0]);
		int month1 = Integer.parseInt(arrivetime[1]);
		int date1 = Integer.parseInt(arrivetime[2]);
		int hr1 = Integer.parseInt(arrivetime[3]);
		int min1 = Integer.parseInt(arrivetime[4]);
		int sec1 = Integer.parseInt(arrivetime[5]);
		return Flight.calendar(year1, month1, date1, hr1, min1, sec1);
	}
	
	protected void addFlight(){
		try {
			systemMessage("Available City: ");
			server.displayCity();
			systemMessage("flightName: ");
			String flightName = scanNextLine();
			Date startTime = scanStartTime();
			Date arriveTime = scanArriveTime();
			if (arriveTime.before(startTime) || startTime.before(new Date())) {
				throw new NumberFormatException();
			}
			systemMessage("Period of the flight(day)(0 for no period): ");
			int period = scanner.nextInt();
			systemMessage("startCityID: ");
			int startCityID = scanner.nextInt();
			systemMessage("arriveCityID: ");
			int arriveCityID = scanner.nextInt();
			if (startCityID == arriveCityID) {
				throw new NumberFormatException();
			}
			systemMessage("price");
			int price = scanner.nextInt();
			systemMessage("seatCapacity: ");
			int seatCapacity=scanner.nextInt();
			systemMessage("distance(m): ");
			int distance = scanner.nextInt();
			scanNextLine();
			if (!server.createFlightDaemon(flightName, startTime, arriveTime, period, startCityID, arriveCityID, price, seatCapacity, distance)) {
				systemMessage("Error in cityID. retry?");
				if (scanNextLine().toLowerCase().equals("y")) {
					addFlight();
				}
			} else {
				systemMessage("Flight added successfully\n");
			}
		} catch (PermissionDeniedException e) {
			systemMessage(e.getMessage());
		} catch (IndexOutOfBoundsException | NumberFormatException | InputMismatchException e) {
			systemMessage("Input error. retry?");
			if (scanNextLine().toLowerCase().equals("y")) {
				addFlight();				
			}
		}
	}
	
	private FlightDaemon serverGetDaemon(int flightID) throws PermissionDeniedException {
		FlightDaemon flight = server.getDaemon(flightID);
		if (flight == null) {
			System.out.printf("Cannot find flight daemon with ID '%d'\n", flightID);
			return null;
		} else if (!flight.getStatus()) {
			systemMessage("This flight has deleted");
			return null;
		}
		systemMessage("ID\tName\tStartCity\tArriveCity\tBeginTime\t\t\tTime\tPeriod\tPrice\tSeatCapacity");
		System.out.println(server.getDaemon(flightID));
		systemMessage("Usage: "
				+ "\tname=newname\n"
				+ "\tstarttime=yyyy-mm-dd-hr-mim-sec\n"
				+ "\tarrivetime=yyyy-mm-dd-hr-mim-sec\n"
				+ "\tstartcity=cityID\n"
				+ "\tarrivecity=cityID\n"
				+ "\tprice=newprice\n"
				+ "\tcapacity=newcapacity\n"
				+ "\tdistance=newdistance\n"
				+ "\texit|e\n"
				+ "Available City: \n");
		server.displayCity();
		return flight;
	}

	private void setFlightName(FlightDaemon flight, String string) {
		flight.setFlightName(string);
	}
	
	private void setFlightStartTime(FlightDaemon flight, String[] sdate) {
		flight.setStartTime(Flight.calendar(
			strToInteger(sdate[0]), 
			strToInteger(sdate[1]), 
			strToInteger(sdate[2]), 
			strToInteger(sdate[3]), 
			strToInteger(sdate[4]),
			strToInteger(sdate[5]))
		);
	}
	
	private void setFlightArriveTime(FlightDaemon flight, String[] adate) {
		flight.setArriveTime(Flight.calendar(
			strToInteger(adate[0]), 
			strToInteger(adate[1]), 
			strToInteger(adate[2]), 
			strToInteger(adate[3]), 
			strToInteger(adate[4]),
			strToInteger(adate[5]))
		);
	}

	private void flightSetDistance(FlightDaemon flight, int i) {
		flight.setDistance(i);
	}

	private void flighSetSeatCapacity(FlightDaemon flight, int i) {
		flight.setSeatCapacity(i);
	}

	private void flightSetPrice(FlightDaemon flight, int i) {
		flight.setPrice(i);
	}

	private void flightSetArriveCity(FlightDaemon flight, int i) throws PermissionDeniedException {
		flight.setArriveCity(server.getCity(i));
	}

	private void flightSetStartCity(FlightDaemon flight, int i) throws PermissionDeniedException {
		flight.setStartCity(server.getCity(i));
	}
	
	protected void changeFlight(int flightID) throws PermissionDeniedException {
		FlightDaemon flight = serverGetDaemon(flightID);
		if (flight == null) { return; }
		String[] input;
		do {
			systemMessage("Please input what to change: ");
			input = scanner.nextLine().replace(" ", "").split("=");
			int i = strToInteger(input[1]);
			try {	
				switch (input[0]) {
					case "name":
						setFlightName(flight, input[1]);
						systemMessage("Succeed!");
						break;
					case "starttime":
						String[] sdate = input[1].split("-");
						setFlightStartTime(flight, sdate);
						systemMessage("Succeed!");
						break;
					case "arrivetime":
						String[] adate = input[1].split("-");
						setFlightArriveTime(flight, adate);
						systemMessage("Succeed!");
						break;
					case "startcity":
						flightSetStartCity(flight, i);
						break;
					case "arrivecity":
						flightSetArriveCity(flight, i);
						systemMessage("Succeed!");
						break;
					case "price":
						flightSetPrice(flight, i);
						systemMessage("Succeed!");
						break;
					case "capacity":
						flighSetSeatCapacity(flight, i);
						systemMessage("Succeed!");
						break;
					case "distance":
						flightSetDistance(flight, i);
						systemMessage("Succeed!");
						break;
					case "exit":
					case "e":
						break;
					default:
						systemMessage("Command error");
						break;
				}
			} catch (IndexOutOfBoundsException | NumberFormatException e) {
				systemMessage("Format error");
			}
		} while (!(input[0].equals("e") || input[0].equals("exit")));
	}
	
}
