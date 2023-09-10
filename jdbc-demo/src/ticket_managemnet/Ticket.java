package ticket_managemnet;

import Connect.DatabaseUtils;
import booking_management.Booking;
import seat_management.Seat;
import timetable_management.TimeTable;

import java.sql.SQLException;

public class Ticket {
    private int ticket_id;
    private Seat seat;
    private Booking booking;
    //Constructor
//    public Ticket(int ticket_id, Seat seat, TimeTable timeTable) {
//        this.ticket_id = ticket_id;
//        this.seat = seat;
//        this.timeTable = timeTable;
//    }

    public Ticket(int ticket_id, Seat seat, Booking booking) {
        this.ticket_id = ticket_id;
        this.seat = seat;
        this.booking = booking;
    }

    private TimeTable timeTable;
    //Getter
    public int getTicket_id() {
        return ticket_id;
    }
    public Seat getSeat() {
        return seat;
    }
    public TimeTable getTimeTable() {
        return timeTable;
    }
    public Booking getBooking() {
        return booking;
    }
    //Setter
    public void setTicket_id(int ticket_id) {
        this.ticket_id = ticket_id;
    }
    public void setSeat(Seat seat) {
        this.seat = seat;
    }
    public void setTimeTable(TimeTable timeTable) {
        this.timeTable = timeTable;
    }
    public void setBooking(Booking booking) {
        this.booking = booking;
    }
//--------------------------------------------------------------------------------------------------------------------------------
//    public void addTicket() throws Exception {
//        int rowAffected = 0;
//
//        try {
//            String insertSql = "INSERT INTO `ticket` (`ticket_id`,`booking_id`,``,`seat_id`,`schedule_id`,`ticket_type`,`price_rate`) value(?,?,?,?,?,?);";
//            Object[] params = {getSeat_id(),this.hall.getHallID(),getSeatRow(),getSeatCol(),getSeat_status()};
//            rowAffected = DatabaseUtils.insertQuery(insertSql, params);
//        }
//        catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        if (rowAffected > 0) {
//            System.out.println("\nSeat successfully added...");
//        }
//        else {
//            System.out.println("\nSomething went wrong!");
//        }
//    }


}
