package booking_management;

import Connect.DatabaseUtils;
import DateTime_management.DateTime;
import timetable_management.TimeTable;

import java.sql.SQLException;


public class Booking {
    private int booking_id;
    private int adultTicket_qty;
    private int childTicket_qty;
    private double totalPrice;
    private DateTime bookingDateTime;
    private int booking_status;

    public Booking() {

    }

    public Booking(int booking_id, int adultTicket_qty, int childTicket_qty, double totalPrice, int booking_status) {
        this.booking_id = booking_id;
        this.adultTicket_qty = adultTicket_qty;
        this.childTicket_qty = childTicket_qty;
        this.totalPrice = totalPrice;
        this.booking_status = booking_status;
    }

    //Getter
    public int getBooking_id() {
        return booking_id;
    }
    public int getAdultTicket_qty() {
        return adultTicket_qty;
    }
    public int getChildTicket_qty() {
        return childTicket_qty;
    }
    public double getTotalPrice() {
        return totalPrice;
    }
    public DateTime getBookingDateTime() {
        return bookingDateTime;
    }
    public int getBooking_status() {
        return booking_status;
    }
    //Setter
    public void setBooking_id(int booking_id) {
        this.booking_id = booking_id;
    }
    public void setAdultTicket_qty(int adultTicket_qty) {
        this.adultTicket_qty = adultTicket_qty;
    }
    public void setChildTicket_qty(int childTicket_qty) {
        this.childTicket_qty = childTicket_qty;
    }
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    public void setBookingDateTime(DateTime bookingDateTime) {
        this.bookingDateTime = bookingDateTime;
    }
    public void setBooking_status(int booking_status) {
        this.booking_status = booking_status;
    }

//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void addBooking() throws Exception {
        int rowAffected = 0;

        try {
            String insertSql = "INSERT INTO `booking` (`booking_id`,`adultTicket_qty`,`childTicket_qty`,`total_price`,`booking_date`,`booking_time`,`booking_status`) value(?,?,?,?,?,?,?);";
            Object[] params = {getBooking_id(),getAdultTicket_qty(),getChildTicket_qty(),getTotalPrice(),DateTime.getCurrentDate(),DateTime.getCurrentTime(),getBooking_status()};
            rowAffected = DatabaseUtils.insertQuery(insertSql, params);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        if (rowAffected > 0) {
            System.out.println("\nBooking successfully added...");
        }
        else {
            System.out.println("\nSomething went wrong!");
        }
    }
}
