package data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.function.Predicate;

import exceptions.StatusUnavailableException;

public class FlightDaemon implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -462004565055095904L;
	public static int ID = 0;
	private int flightDaemonID;
	private String flightName;
	private FlightDaemonData data = new FlightDaemonData();
	protected boolean status;
	protected ArrayList<Flight> children;

	public FlightDaemon(String flightName, Date startTime, Date arriveTime, int period, City startCity, City arriveCity, int price,
			int seatCapacity, int distance) {
		this.flightName = flightName;
		this.data.setStartTime(startTime);
		this.data.setArriveTime(arriveTime);
		this.data.setPeriod(period);
		this.data.setStartCity(startCity);
		this.data.setArriveCity(arriveCity);
		this.data.setPrice(price);
		this.data.setSeatCapacity(seatCapacity);
		this.data.setDistance(distance);
		status = true;
		children = new ArrayList<>();
		flightDaemonID = FlightDaemon.ID;
		ID++;
		startCity.flightsOut.add(this);
		arriveCity.flightsIn.add(this);
	}

	@Override
	public String toString() {
		return data.toString();
	}
	
	@Override
	public int hashCode() {
		return data.hashCode(flightName, status);
	}

	public boolean flightIsDaemon(Flight flight) {
		return flight.isDaemon();
	}
	
	public long getFlightStartTime(Flight flight) {
		return flight.getStartTime().getTime();
	}
	
	public void setFlightStartTime(Flight flight, Date date) {
		try {
			flight.setStartTime(date);
		} catch (StatusUnavailableException e) { /* ignored */ }
	}
	
	public long getFlightArriveTime(Flight flight) {
		return flight.getArriveTime().getTime();
	}
	
	public void setFlightArriveTime(Flight flight, Date date) {
		try {
			flight.setArriveTime(date);
		} catch (StatusUnavailableException e) { /* ignored */ }
	}
	
	public int getFlightDaemonID() {
		return flightDaemonID;
	}

	public void setFlightDaemonID(int flightDaemonID) {
		this.flightDaemonID = flightDaemonID;
	}

	public String getFlightName() {
		return flightName;
	}

	public void setFlightName(String flightName) {
		this.flightName = flightName;
		for (Flight flight : children) {
			try {
				if (flight.isDaemon()) {
					flight.setFlightName(flightName);
				}
			} catch (StatusUnavailableException e) { /* ignored */ }
		}
	}

	public Date getStartTime() {
		return data.getStartTime();
	}
	
	public void setStartTime(Date startTime) {
		long shift = startTime.getTime() - this.data.getStartTime().getTime();
		this.data.setStartTime(startTime);
		for (Flight flight : children) {
			if (flightIsDaemon(flight)) {
				setFlightStartTime(flight, new Date(getFlightStartTime(flight) + shift));
			}
		}
	}

	public Date getArriveTime() {
		return data.getArriveTime();
	}

	public void setArriveTime(Date arriveTime) {
		long shift = arriveTime.getTime() - this.data.getArriveTime().getTime();		
		this.data.setArriveTime(arriveTime);
		for (Flight flight : children) {
			if (flightIsDaemon(flight)) {
				setFlightArriveTime(flight, new Date(getFlightArriveTime(flight) + shift));
			}
		}
	}

	public int getPeriod() {
		return data.getPeriod();
	}

	public void setPeriod(int period) {
		this.data.setPeriod(period);
	}

	public City getStartCity() {
		return data.getStartCity();
	}

	public void setStartCity(City startCity) {
		this.data.setStartCity(startCity);
		for (Flight flight : children) {
			try {
				if (flight.isDaemon()) {
					flight.setStartCity(startCity);
				}
			} catch (StatusUnavailableException e) { /* ignored */ }
		}
	}

	public City getArriveCity() {
		return data.getArriveCity();
	}

	public void setArriveCity(City arriveCity) {
		this.data.setArriveCity(arriveCity);
		for (Flight flight : children) {
			try {
				if (flight.isDaemon()) {
					flight.setArriveCity(arriveCity);
				}
			} catch (StatusUnavailableException e) { /* ignored */ }
		}
	}

	public int getPrice() {
		return data.getPrice();
	}

	public void setPrice(int price) {
		this.data.setPrice(price);
		for (Flight flight : children) {
			try {
				if (flight.isDaemon()) {
					flight.setPrice(price);
				}
			} catch (StatusUnavailableException e) { /* ignored */ }
		}
	}

	public int getSeatCapacity() {
		return data.getSeatCapacity();
	}

	public void setSeatCapacity(int seatCapacity) {
		this.data.setSeatCapacity(seatCapacity);
		for (Flight flight : children) {
			try {
				if (flight.isDaemon()) {
					flight.setSeatCapacity(seatCapacity);
				}
			} catch (StatusUnavailableException e) { /* ignored */ }
		}
	}

	public int getDistance() {
		return data.getDistance();
	}

	public void setDistance(int distance) {
		this.data.setDistance(distance);
		for (Flight flight : children) {
			if (flight.isDaemon()) {
				flight.setDistance(distance);
			}
		}
	}
	
	public boolean getStatus() {
		return status;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Flight> getChildren() {
		return (ArrayList<Flight>) children.clone();
	}

	public void removeFlight() {
		children.removeIf(new Predicate<Flight>() {
			
			@Override
			public boolean test(Flight t) {
				return t.flightStatus == FlightStatus.UNPUBLISHED;
			}
		});
		status = false;
	}

}
