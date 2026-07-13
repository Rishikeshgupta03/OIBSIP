package model;

/**
 * Train
 * -----
 * Represents a train that reservations can be booked against.
 * Simple immutable-style data holder with encapsulated fields.
 */
public class Train {

    private String trainNumber;
    private String trainName;

    public Train() {
    }

    public Train(String trainNumber, String trainName) {
        this.trainNumber = trainNumber;
        this.trainName = trainName;
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

    @Override
    public String toString() {
        return trainNumber + " - " + trainName;
    }
}
