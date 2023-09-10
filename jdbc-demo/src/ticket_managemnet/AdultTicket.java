package ticket_managemnet;

import booking_management.Booking;
import seat_management.Seat;
import timetable_management.TimeTable;

public class AdultTicket extends Ticket{
    private String ticketType;
    private static double price_rate=1.0;
    //Setter
    public String getTicketType() {
        return ticketType;
    }
    public static double getPrice_rate() {
        return price_rate;
    }
    //Getter
    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }
    public static void setPrice_rate(double price_rate) {
        AdultTicket.price_rate = price_rate;
    }

    public AdultTicket(int ticket_id, Seat seat, Booking booking, String ticketType) {
        super(ticket_id, seat, booking);
        this.ticketType = ticketType;
    }
}
