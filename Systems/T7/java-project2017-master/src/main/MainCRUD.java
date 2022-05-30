package main;

import java.util.Scanner;

import exceptions.PermissionDeniedException;
import exceptions.StatusUnavailableException;

public class MainCRUD {
	private static MainServer server;
	private static Scanner scanner;
		
	public MainCRUD(MainServer ms_server, Scanner ms_scanner){
		server = ms_server;
		scanner = ms_scanner;
	}
	
	private static void systemMessage(String str) {
		System.out.println(str);
	}
	
	private static String scanNextLine() {
		return scanner.nextLine();
	}
		
	public static void listCity(String[] param) {
		if (param.length == 1) {
			server.displayCity();
		} else {
			for (int i = 1; i < param.length; i++) {
				server.displayCity(Integer.valueOf(param[i]));
			}
		}
	}
	
	public static void listFlight(String[] param) {
		int param_len = param.length;
		if (param_len == 1) {
			server.displayFlight();						
		} else {
			for (int i = 1; i < param_len; i++) {
				try {
					server.displayFlight(Integer.valueOf(param[i]));
				} catch (NumberFormatException e) {
					System.out.printf("'%s' is not a flight\n", param[i]);
				}								
			}
		}
	}
	
	public static void listUser(String[] param) throws PermissionDeniedException {
		int param_len = param.length;
		if (param_len == 1) {
			try {
				server.displayUser();
			} catch (PermissionDeniedException e) {
				systemMessage(e.getMessage());
			}							
		} else {
			for (int i = 1; i < param_len; i++) {
				try {
					if (!server.displayUser(Integer.valueOf(param[i]))) {
						System.out.printf("Can't find user with id '%s'\n", param[i]);
					}
				} catch (NumberFormatException e) {
					System.out.printf("'%s' is not a user id.\n", param[i]);
				}
			}
		}
	}
	
	public void list(String[] param) {
		if (param != null && param.length >= 1) {
			try {
				switch (param[0]) {
				case "city":
					listCity(param);
					break;
				case "flight":
					listFlight(param);						
					break;
				case "daemon":
					server.displayDaemon();						
					break;
				case "user":
					listUser(param);
					break;
				case "order":
					systemMessage("Please Input your password: ");
					if (!server.checkPass(scanNextLine())) {
						throw new PermissionDeniedException("Password Error");
					}
					server.displayOrder();
					break;
				default:
					systemMessage("Format error: you can only list city, user, flight or order");
					break;
				}
			} catch (PermissionDeniedException e) {
				systemMessage(e.getMessage());
			}								
		} else {
			systemMessage("Format error: please input what to list");
		}
	}
	
	public void deleteFlight(String[] param) {
		try {
			for (int i = 1; i < param.length; i++) {
				try {
					if (server.deleteFlight(Integer.parseInt(param[i]))) {
						System.out.printf("Successfully delete flight '%s'!\n", param[i]);
					} else {
						System.out.printf("Delete flight '%s' failed: no such flight\n", param[i]);
					}
				} catch (NumberFormatException e) {
					System.out.printf("'%s' is not a flight id!\n", param[i]);
				} catch (StatusUnavailableException e) {
					System.out.printf("Delete flight '%s' failed: %s\n", param[i], e.getMessage());
				}
			}
		} catch (PermissionDeniedException e) {
			systemMessage(e.getMessage());
		}
	}
	
	public static void deleteDaemon(String[] param){
		try {
			for (int i = 1; i < param.length; i++) {
				try {
					if (server.deleteFlightDaemon(Integer.parseInt(param[i]))) {
						System.out.printf("Successfully delete flight daemon '%s'!\n", param[i]);
					} else {
						System.out.printf("Delete flight daemon '%s' failed: no such flight daemon\n", param[i]);
					}
				} catch (NumberFormatException e) {
					System.out.printf("'%s' is not a flight daemon id!\n", param[i]);
				} catch (StatusUnavailableException e) {
					System.out.printf("Delete flight daemon '%s' failed: %s\n", param[i], e.getMessage());
				}
			}
		} catch (PermissionDeniedException e) {
			systemMessage(e.getMessage());
		}
	}
	
	public static void deleteCity(String[] param) {
		try {
			for (int i = 1; i < param.length; i++) {
				try {
					if (server.deleteCity(Integer.parseInt(param[i]))) {
						System.out.printf("Successfully delete city '%s'!\n", param[i]);
					} else {
						System.out.printf("Delete city '%s' failed: no such city\n", param[i]);
					}
				} catch (NumberFormatException e) {
					System.out.printf("'%s' is not a city id!\n", param[i]);
				} catch (StatusUnavailableException e) {
					System.out.printf("Delete city '%s' failed: %s\n", param[i], e.getMessage());
				}
			}
		} catch (PermissionDeniedException e) {
			systemMessage(e.getMessage());
		}
	}
	
	public static void deleteUser(String[] param) {
		try {
			for (int i = 1; i < param.length; i++) {
				try {
					if (server.deleteUser(Integer.parseInt(param[i]))) {
						System.out.printf("Successfully delete user '%s'!\n", param[i]);
					} else {
						System.out.printf("Delete user '%s' failed: no such user\n", param[i]);
					}
				} catch (NumberFormatException e) {
					System.out.printf("'%s' is not a user id!\n", param[i]);
				}
			}
		} catch (PermissionDeniedException e) {
			systemMessage(e.getMessage());
		}
	}
	
	public void delete(String[] param){
		if (param != null && param.length >= 2) {
			switch (param[0]) {
			case "flight":
				deleteFlight(param);
				break;
			case "daemon":
				deleteDaemon(param);
				break;
			case "city":
				deleteCity(param);
				break;
			case "user":
				deleteUser(param);
				break;
			default:
				systemMessage("You can only delete a city, flight or user");
				break;
			}
		} else {
			systemMessage("Format error: please use 'delete (city|flight|user) [ID1] [ID2] ...' to delete");
		}
	}
}