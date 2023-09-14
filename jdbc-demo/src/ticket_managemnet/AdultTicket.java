//package ticket_managemnet;
//
//import Connect.DatabaseUtils;
//import booking_management.Booking;
//import seat_management.Seat;
//import timetable_management.TimeTable;
//
//import java.sql.SQLException;
//
//public class AdultTicket extends Ticket{
//    private String ticketType;
//    private static double price_rate=1.0;
//    //Setter
//    public String getTicketType() {
//        return ticketType;
//    }
//    public static double getPrice_rate() {
//        return price_rate;
//    }
//    //Getter
//    public void setTicketType(String ticketType) {
//        this.ticketType = ticketType;
//    }
//    public static void setPrice_rate(double price_rate) {
//        AdultTicket.price_rate = price_rate;
//    }
//
//    public AdultTicket(String ticketType) {
//        this.ticketType = ticketType;
//    }
//
//    public AdultTicket(int ticket_id, Seat seat, Booking booking, String ticketType) {
//        super(ticket_id, seat, booking);
//        this.ticketType = ticketType;
//    }
//
//    public void addTicket() throws Exception {
//        int rowAffected = 0;
//
//        try {
//            String insertSql = "INSERT INTO `ticket` (`ticket_id`,`booking_id`,`seat_id`,`schedule_id`,`ticket_type`,`price_rate`) value(?,?,?,?,?,?);";
//            Object[] params = {getTicket_id(),getBooking().getBooking_id(),getSeat().getSeat_id(),3,getTicketType(),getPrice_rate()};
//            rowAffected = DatabaseUtils.insertQuery(insertSql, params);
//        }
//        catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        if (rowAffected > 0) {
//            System.out.println("\nTicket successfully added...");
//        }
//        else {
//            System.out.println("\nSomething went wrong!");
//        }
//    }
//}
