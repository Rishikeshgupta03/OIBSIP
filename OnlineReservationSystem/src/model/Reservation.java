package model;

/**
 * Reservation
 * -----------
 * Represents a single ticket reservation record.
 *
 * Demonstrates Encapsulation and Object Composition: a Reservation
 * conceptually wraps passenger details + a Train reference (by number/name)
 * + journey details, all exposed only through getters/setters.
 */
public class Reservation {

    private String pnr;
    private String passengerName;
    private int age;
    private String gender;
    private String trainNumber;
    private String trainName;
    private String journeyDate; // stored as ISO string yyyy-MM-dd
    private String travelClass;
    private String source;
    private String destination;
    private String bookingDate;  // ISO string yyyy-MM-dd

    public Reservation() {
    }

    public Reservation(String pnr, String passengerName, int age, String gender,
                        String trainNumber, String trainName, String journeyDate,
                        String travelClass, String source, String destination,
                        String bookingDate) {
        this.pnr = pnr;
        this.passengerName = passengerName;
        this.age = age;
        this.gender = gender;
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.journeyDate = journeyDate;
        this.travelClass = travelClass;
        this.source = source;
        this.destination = destination;
        this.bookingDate = bookingDate;
    }

    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getJourneyDate() {
        return journeyDate;
    }

    public void setJourneyDate(String journeyDate) {
        this.journeyDate = journeyDate;
    }

    public String getTravelClass() {
        return travelClass;
    }

    public void setTravelClass(String travelClass) {
        this.travelClass = travelClass;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    @Override
    public String toString() {
        return "Reservation{pnr='" + pnr + "', passengerName='" + passengerName
                + "', trainNumber='" + trainNumber + "'}";
    }
}
