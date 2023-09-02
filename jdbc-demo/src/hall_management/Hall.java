package hall_management;

import Connect.DatabaseUtils;
import Driver.Name;

import java.sql.SQLException;

public class Hall {
    private int hallID;
    private Name hallName;
    private String hallType;
    private int hallCapacity;

    // Constructor
    public Hall(){
    }

    public Hall(Name hallName, String hallType){
        this.hallName = hallName;
        this.hallType = hallType;
        calHallCapacity();
    }
    public Hall(int hallID, Name hallName, String hallType){
        this.hallID = hallID;
        this.hallName = hallName;
        this.hallType = hallType;
        calHallCapacity();
    }

    public void calHallCapacity() {
        if (hallType.equals("STANDARD")) {
            hallCapacity = 64;
        }
        else if (hallType.equals("3D")) {
            hallCapacity = 32;
        }
    }

    // Method


    // Setter
    public void setHallID(int hallID) {
        this.hallID = hallID;
    }

    public void setHallName(Name hallName) {
        this.hallName = hallName;
    }

    public void setHallType(String hallType) {
        this.hallType = hallType;
    }

    // Getter
    public int getHallID(){
        return hallID;
    }

    public Name getHallName() {
        return hallName;
    }

    public String getHallType() {
        return hallType;
    }

    public int getHallCapacity() {
        return hallCapacity;
    }
}
