package data;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Random;
import java.util.TimeZone;

import exceptions.StatusUnavailableException;

public class Flight implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4984381831014139467L;
	public static int ID = 0;
	private int flightID;
	private String flightName;
	protected FlightStatus flightStatus;
	private HashMap<Passenger, Integer> passagers;
	private FlightData data = new FlightData();
	private boolean isDaemon;
	
	public Flight(String flightName, Date startTime, Date arriveTime, City startCity, City arriveCity, int price,
			int seatCapacity, int distance) {
		passagers = new HashMap<>();
		this.flightName = flightName;
		this.data.setStartTime(startTime);
		this.data.setArriveTime(arriveTime);
		this.data.setStartCity(startCity);
		this.data.setArriveCity(arriveCity);
		this.data.setPrice(price);
		this.data.setSeatCapacity(seatCapacity);
		this.data.setDistance(distance);
		this.flightStatus = FlightStatus.UNPUBLISHED;
		isDaemon = true;
		flightID = Flight.ID;
		ID++;
	}
	
	@Override
	public String toString() {
		return data.toString(flightID, flightName, passagers.size(), flightStatus);
	}
	
	@Override
	public int hashCode() {
        return data.hashCode(flightName, flightStatus);
	}
	
	public boolean flightStatusIsAvaliable() {
		return flightStatus == FlightStatus.AVAILABLE ? true : false;
	}
	
	public boolean flightStatusIsUnpublished() {
		return flightStatus == FlightStatus.UNPUBLISHED ? true : false;
	}
	
	public boolean flightStatusIsFull() {
		return flightStatus == FlightStatus.FULL ? true : false;
	}
	
	public boolean flightStatusIsTerminate() {
		return flightStatus == FlightStatus.TERMINATE ? true : false;
	}
	
	public int getPassagersSize() {
		return passagers.size();
	}
	
	public static Date calendar(int year, int month, int date, int hr, int min, int sec) {
		Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+8:00"));
		clearCalendar(calendar);
		calendar.set(year, month - 1, date, hr, min, sec);
		return getTimeCalendar(calendar);
	}
	
	public static void clearCalendar(Calendar calendar) {
		calendar.clear();
	}
	public static Date getTimeCalendar(Calendar calendar) {
		return calendar.getTime();
	}
	
	/* DONE(Zhu) get and set in Flight
	 * getter and setter are generated automatically, and need mortifying
	 * basically the restriction is flightStatus(seeing requirement)
	 * if force to change, throw StatusUnavailableException(${CurrentStatus}).
	 */
	public int getFlightID() {
		return flightID;
	}
	
	public String getFlightName() {
		return flightName;
	}
	
	public void setFlightName(String flightName) throws StatusUnavailableException {
		if(flightStatusIsUnpublished()){
			this.flightName = flightName;
		}else{
			throw new StatusUnavailableException(flightStatus);
		}
	}

	public Date getStartTime() {
		return data.getStartTime();
	}

	public void setStartTime(Date startTime) throws StatusUnavailableException {
		if(flightStatusIsUnpublished()){
			this.data.setStartTime(startTime);
		}else{
			throw new StatusUnavailableException(flightStatus);
		}
	}

	public Date getArriveTime() {
		return data.getArriveTime();
	}

	public int getDistance() {
		return data.getDistance();
	}
	
	public void setDistance(int distance) {
		this.data.setDistance(distance);
	}
	
	public void setArriveTime(Date arriveTime) throws StatusUnavailableException {
		if(flightStatusIsUnpublished()){
			this.data.setArriveTime(arriveTime);
		}else{
			throw new StatusUnavailableException(flightStatus);
		}
	}

	public City getStartCity() {
		return data.getStartCity();
	}

	public void setStartCity(City startCity) throws StatusUnavailableException {
		if(flightStatusIsUnpublished()){
			this.data.setStartCity(startCity);
		}else{
			throw new StatusUnavailableException(flightStatus);
		}
	}

	public City getArriveCity() {
		return data.getArriveCity();
	}

	public void setArriveCity(City arriveCity) throws StatusUnavailableException {
		if(flightStatusIsUnpublished()){
			this.data.setArriveCity(arriveCity);
		}else{
			throw new StatusUnavailableException(flightStatus);
		}
	}

	public int getPrice() {
		return data.getPrice();
	}

	public void setPrice(int price) throws StatusUnavailableException {
		if(!flightStatusIsTerminate()){
			this.data.setPrice(price);
		}else{
			throw new StatusUnavailableException(flightStatus);
		}
	}

	public int getSeatCapacity() {
		return data.getSeatCapacity();
	}

	public void setSeatCapacity(int seatCapacity) throws StatusUnavailableException {
		if(!flightStatusIsTerminate()){
			// DONE(Zhu) you should consider more in changing seat capacity
           if(flightStatusIsFull() &&
        		   this.data.getSeatCapacity() < seatCapacity){
        	   this.data.setSeatCapacity(seatCapacity);
           }else{
        	   throw new StatusUnavailableException("Set Failed!");
           }    
		}else{
			throw new StatusUnavailableException(flightStatus);
		}
	}

	public FlightStatus getFlightStatus() {
		return flightStatus;
	}
	
	/**
	 * read only, use add/remove to operate
	 * @return a clone of field passengers
	 */
	@SuppressWarnings("unchecked")
	public HashMap<Passenger, Integer> passagers() {
		return (HashMap<Passenger, Integer>) passagers.clone();
	}
	
	protected HashMap<Passenger, Integer> getPassagers() {
		return passagers;
	}

	protected void addPassenger(Passenger passenger, int seat, boolean ignore) throws StatusUnavailableException {
		if (!ignore) {
			/* DONE(Zhu) addPassager
			 * you should generate and add order in this method meanwhile
			 * for my convenience
			 * and check for status
			 */
			if (flightStatusIsAvaliable()) {
				passagers.put(passenger, seat);
				if (getPassagersSize() == data.getSeatCapacity()) {
					flightStatus = FlightStatus.FULL;
				}
			} else {
				throw new StatusUnavailableException(flightStatus);
			}
		} else {
			passagers.put(passenger, seat);			
		}
	}
	
	protected void addPassenger(Passenger passenger, int seat) throws StatusUnavailableException {
		addPassenger(passenger, seat, false);
	}
	
	protected void addPassenger(Passenger passenger) throws StatusUnavailableException {
		addPassenger(passenger, getAvailableSeat());
	}
	
	/**
	 * remove passenger from the passenger list
	 * @return return false when no one can found
	 * @throws StatusUnavailableException when status is TERMINATE, 
	 */
	protected boolean removePassenger(Passenger passenger) throws StatusUnavailableException {
		/* DONE(Zhu) removePassenger
		 * you should remove in this method meanwhile
		 * and check for status
		 * XXX(Zhu) needs review
		 */
		if(!flightStatusIsTerminate()) {
			if (passagers.remove(passenger) != null) {
				if (getPassagersSize() < data.getSeatCapacity() && flightStatusIsFull()) {
					flightStatus = FlightStatus.AVAILABLE;
				} else if (getPassagersSize() == data.getSeatCapacity()) {
					flightStatus = FlightStatus.FULL;
				}
				return true;
			} else {
				return false;
			}
		} else
			throw new StatusUnavailableException(flightStatus);
	}

	public void publish() throws StatusUnavailableException {
		if (isDaemon) {
			throw new StatusUnavailableException("the status of this flight is under control of server!");
		}
		if (flightStatusIsUnpublished()) {
			flightStatus = FlightStatus.AVAILABLE;
		} else {
			throw new StatusUnavailableException(flightStatus);
		}
	}

	public int getAvailableSeat() {
		Collection<Integer> seat = passagers.values();
		if (seat.size() == data.getSeatCapacity()) {
			return -1;
		}
		Random random = new Random(this.hashCode() * this.hashCode());
		int result;
		do {
			result = random.nextInt(data.getSeatCapacity()) + 1;			
		} while (seat.contains(result));
		return result;
	}

	protected boolean isDaemon() {
		return isDaemon;
	}

	protected void setDaemon(boolean isDaemon) {
		this.isDaemon = isDaemon;
	}

	public void delete() {
		isDaemon = false;
		flightStatus = FlightStatus.TERMINATE;
	}
	
}
