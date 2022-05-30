package data;

import java.util.Date;

public class FlightData {
	private Date startTime;
	private Date arriveTime;
	private City startCity;
	private City arriveCity;
	private int price;
	private int seatCapacity;
	private int distance;

	public FlightData() {
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getArriveTime() {
		return arriveTime;
	}

	public void setArriveTime(Date arriveTime) {
		this.arriveTime = arriveTime;
	}

	public City getStartCity() {
		return startCity;
	}

	public void setStartCity(City startCity) {
		this.startCity = startCity;
	}

	public City getArriveCity() {
		return arriveCity;
	}

	public void setArriveCity(City arriveCity) {
		this.arriveCity = arriveCity;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getSeatCapacity() {
		return seatCapacity;
	}

	public void setSeatCapacity(int seatCapacity) {
		this.seatCapacity = seatCapacity;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}
	
	public int hashCode(String flightName, FlightStatus flightStatus) {
        int hashCode = 1;
        hashCode = 31*hashCode + flightName.hashCode();
        hashCode = 31*hashCode + getStartCity().hashCode();
        hashCode = 31*hashCode + getStartTime().hashCode();
        hashCode = 31*hashCode + getArriveCity().hashCode();
        hashCode = 31*hashCode + getArriveTime().hashCode();
        hashCode = 31*hashCode + ((Integer)getPrice()).hashCode();
        hashCode = 31*hashCode + flightStatus.hashCode();
        hashCode = 31*hashCode + ((Integer)getSeatCapacity()).hashCode();
        return hashCode;
	}
	
	public String toString(int flightID, String flightName, int passagersSize, FlightStatus flightStatus) {
		return 
			String.valueOf(flightID) + "\t" +
			flightName + "\t" + 
			((getStartCity().toString().length() < 8) ? (getStartCity().toString() + "\t") : getStartCity().toString()) + "\t" +
			((getArriveCity().toString().length() < 8) ? (getArriveCity().toString() + "\t") : getArriveCity().toString()) + "\t" +
			getStartTime().toString() + "\t" +
			getArriveTime().toString() + "\t" +
			String.valueOf(getPrice()) + "\t" +
			String.valueOf(getSeatCapacity() - passagersSize) + "\t" +
			flightStatus.name();
	}
}