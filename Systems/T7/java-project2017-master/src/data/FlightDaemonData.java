package data;

import java.util.Date;

public class FlightDaemonData {
	private Date startTime;
	private Date arriveTime;
	private int period;
	private City startCity;
	private City arriveCity;
	private int price;
	private int seatCapacity;
	private int distance;

	public FlightDaemonData() {
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

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
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
	
	public int hashCode(String flightName, boolean status) {
        int hashCode = 1;
        hashCode = 31*hashCode + flightName.hashCode();
        hashCode = 31*hashCode + getStartCity().hashCode();
        hashCode = 31*hashCode + getStartTime().hashCode();
        hashCode = 31*hashCode + getArriveCity().hashCode();
        hashCode = 31*hashCode + getArriveTime().hashCode();
        hashCode = 31*hashCode + ((Integer)getPrice()).hashCode();
        hashCode = 31*hashCode + ((Integer)getSeatCapacity()).hashCode();
        hashCode = 31*hashCode + ((Boolean)status).hashCode();
        return hashCode;
	}
	
	public String toString(int flightDaemonID, String flightName, boolean status) {
		return String.format("%d\t%s\t%-8s\t%-8s\t%s\t%dmin\t%dday\t%d\t%d",
			flightDaemonID,
			flightName,
			getStartCity(),
			getArriveCity(),
			getStartTime(),
			(getArriveTime().getTime() - getStartTime().getTime())/60000,
			getPeriod()/(24*3600*1000),
			getPrice(),
			getSeatCapacity())
			+ (status ? "" : "\tdeleted");
	}
}