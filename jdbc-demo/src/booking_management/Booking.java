package booking_management;

import Connect.DatabaseUtils;
import DateTime_management.DateTime;
import hall_management.Hall;
import seat_management.Seat;

import ticket_managemnet.Ticket;
import timetable_management.TimeTable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;


public class Booking {
    private int booking_id;
    private int adultTicket_qty;
    private int childTicket_qty;
    private double totalPrice;
    private DateTime bookingDateTime;
    private int booking_status;

    public Booking() {
        this.childTicket_qty=0;
        this.adultTicket_qty=0;
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
    public void countBooking_id() {
        this.booking_id=1;
        //this.ticket_id = ticket_id;
        ArrayList<Booking> bookings=Booking.getBookedBookingList();
        for(Booking b:bookings){
            this.booking_id++;
        }
//        this.ticket_id+=count;
        //return this.booking_id;
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
    public static void insertBooking(Booking b) throws Exception {
        int rowAffected = 0;

        try {
            String insertSql = "INSERT INTO `booking` (`booking_id`,`adultTicket_qty`,`childTicket_qty`,`total_price`,`booking_date`,`booking_time`,`booking_status`) value(?,?,?,?,?,?,?);";
            Object[] params = {b.getBooking_id(),b.getAdultTicket_qty(),b.getChildTicket_qty(),b.getTotalPrice(),DateTime.getCurrentDate(),DateTime.getCurrentTime(),b.getBooking_status()};
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

    public static int viewSeat_status(TimeTable schedule) {
        boolean error = false;
        ArrayList<Seat> seats = new ArrayList<>();
        int largestRow=0;
        int largestCol=0;
        try {
            Object[] params = {schedule.getHall().getHallID()};
            ResultSet result = DatabaseUtils.selectQueryById("*", "seat", "hall_id = ?", params);
            //find hall

            while (result.next()) {

                Seat seat = new Seat();
                Hall hl=new Hall();
                seat.setHall(hl);
                seat.setSeat_id(result.getString("seat_id"));
                //seat.getHall().setHallID(hallId);
                Hall hall =new Hall();
                hall.setHallID(result.getInt("hall_id"));
                seat.setHall(hall);
                seat.setSeatRow(result.getInt("seatrow"));
                seat.setSeatCol(result.getInt("seatcol"));
                seat.setSeat_status(result.getInt("seat_status"));
                //System.out.printf("%d",seat.hall.getHallID());
                largestRow=result.getInt("seatrow");
                largestCol=result.getInt("seatcol");
                seats.add(seat);
            }

            result.close();
            //resultHall.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Ticket> tickets=Ticket.getBookedTicketList(schedule.getTimetableID());
        System.out.printf("Movie : %s   Hall : %d   Date : %s   Start Time : %s:%s\n",schedule.getMovie().getMvName().getName(),schedule.getHall().getHallID(),schedule.getShowDate().getDate(),schedule.getStartTime().getHour(),schedule.getStartTime().getMinute());
        System.out.println("\t             [Screen]");
        //System.out.println("   1   2   3   4   5   6   7   8");
        int j=0;
        for(int i=1;i<=largestRow;i++) {
            char letter = (char) ('A' + i - 1);
            System.out.print("\t"+letter+" ");
            do {
                char st;
                if(seats.get(j).getSeat_status()==1) {
                    st='O';
                }else{
                    st='X';
                }
                for (Ticket t:tickets) {
                    if(t.getSeat().getSeat_id().equals(seats.get(j).getSeat_id())){
                        st='1';
                    }
                }
                System.out.printf("[%c] ",st);
                j++;
            } while (seats.get(j).getSeatCol() +1 <= largestCol);
            char st;
            if(seats.get(j).getSeat_status()==1) {
                st='O';
            }else{
                st='X';
            }
            for (Ticket t:tickets) {
                if(t.getSeat().getSeat_id().equals(seats.get(j).getSeat_id())){
                    st='1';
                }
            }
            System.out.printf("[%c] ",st);
            System.out.printf("\n");
            j += 1;
        }
        System.out.println("\t   1   2   3   4   5   6   7   8");
        System.out.printf("\nO = Available    1 = Booked    X = Unavailable/Broken\n");
        System.out.println("Movie Basic Price : "+schedule.getMovie().getBasicTicketPrice());
        return 0;
    }

    public void executeBooking(TimeTable schedule) throws Exception {
        this.countBooking_id();
        Scanner scanner = new Scanner(System.in);
        ArrayList<Ticket> tickets=Ticket.getBookedTicketList(schedule.getTimetableID());
        ArrayList<Ticket> cartTicket=new ArrayList<>();
        System.out.println("Booking ID : "+this.booking_id);
        int row,col;
        String str=" ";
        char ch=str.charAt(0);
        int inputType;
        String ticketType;

        int count=0;
        while (ch!='N' && ch!='X'){
            Ticket ticket = new Ticket();
            System.out.print("\nSelect Row    : ");
            row = scanner.nextInt();
            System.out.println(row);/////////////////////
            System.out.print("Select Column : ");
            col = scanner.nextInt();
            System.out.println(col);///////////////////////
            if(!Seat.checkSeatValidation(row,col)){
                System.out.println("Invalid Input");
                continue;
            }
            System.out.print("Select type(1.Adult 2.Child ) :");
            inputType=scanner.nextInt();

            if(inputType==1){
                ticketType="Adult";
                ticket.setTicketType(ticketType);
                ticket.setPrice_rate(1.2);
                this.adultTicket_qty++;

            }
            else if(inputType==2){
                ticketType="Child";
                ticket.setTicketType(ticketType);
                ticket.setPrice_rate(0.8);
                this.childTicket_qty++;
            }
            String letter2=Integer.toString(schedule.getHall().getHallID());
            char letter = (char) ('A' + row - 1);
            String combineSeatId =  letter2+letter+Integer.toString(col);
            //seat
            Seat seat=new Seat(combineSeatId,schedule.getHall(),row,col,1);

            //Ticket ticket = new Ticket();
            ticket.setTicket_id(ticket.countTicket_id(count));
            ticket.setBooking(this);
            ticket.setSeat(seat);
            ticket.setTimeTable(schedule);



            System.out.print("Continue ? (Y=Yes N=Next X=Exit) : ");
            str=scanner.next();
            ch=str.charAt(0);
            cartTicket.add(ticket);
            count++;
        }

        for(Ticket t:tickets){
            System.out.println(t.getSeat().getSeat_id());
        }
        System.out.println("Cart : ");
        for(Ticket t:cartTicket){
            System.out.println("\t\t-----------------------");
            System.out.printf("\t\t| Ticket id :| %6d |\n",t.getTicket_id());
            System.out.printf("\t\t| Seat id   :| %6s |\n",t.getSeat().getSeat_id());
            System.out.printf("\t\t| Price     :| %6.2f |\n",t.calculateTicketPrice());
        }
        System.out.println("\t\t-----------------------\n");
        System.out.println("Confirm This Booking ? (Y=Yes N=No, Select Again E=No Confirm, Exit) : ");
        str=scanner.next();
        ch=str.charAt(0);
        if (ch=='Y'){
            Booking.insertBooking(this);
            for(Ticket t:cartTicket){
                Ticket.insertTicket(t);
            }
        }

    }

    public static ArrayList<Booking> getBookedBookingList(){
        boolean error = false;
        ArrayList<Booking> bookings = new ArrayList<>();

        try {
            Object[] params = { };
            ResultSet result = DatabaseUtils.selectQueryById("*", "booking",null,null);

            while (result.next()) {
                Booking booking =new Booking();
                booking.setBooking_id(result.getInt("booking_id"));
                booking.setAdultTicket_qty(result.getInt("adultTicket_qty"));
                booking.setChildTicket_qty(result.getInt("childTicket_qty"));
                booking.setBooking_status(result.getInt("booking_status"));

                bookings.add(booking);
                //tickets.add(ticket);

                //seats.add(seat);
            }

            result.close();
            //resultHall.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return bookings;
    }
}
