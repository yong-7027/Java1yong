package cinema_management;

import Connect.DatabaseUtils;
import Driver.Name;
import hall_management.Hall;
import movie_management.Movie;
import movie_management.ShowDate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cinema {
    private Hall hall;
    private int cinemaID;
    private Name cinemaName;
    private Address cinemaAddress;
    private String cinemaPhone;
    private static ArrayList<Cinema> cinemas = new ArrayList<>();
    private static final String OFFICE_PHONE_REGEX = "^(01[0-9]-[0-9]{7}|011-[0-9]{8}|03-[0-9]{8}|08[0-9]-[0-9]{6}|0[0-9]-[0-9]{7})$";

    public Cinema(){
    }

    public Cinema(Hall hall, int cinemaID, Name cinemaName, Address cinemaAddress, String cinemaPhone) {
        this.hall = hall;
        this.cinemaID = cinemaID;
        this.cinemaName = cinemaName;
        this.cinemaAddress = cinemaAddress;
        this.cinemaPhone = cinemaPhone;
    }

    static {
        try {
            ResultSet result = DatabaseUtils.selectQueryById("*", "cinema", null, null);

            while (result.next()) {
                Cinema cinema = new Cinema();

                cinema.setCinemaID(result.getInt("cinema_id"));
                cinema.setCinemaName(new Name(result.getString("cinema_name")));
                cinema.setCinemaAddress(new Address(result.getString("cinema_address")));
                cinema.setCinemaPhone(result.getString("cinema_phone"));

                cinemas.add(cinema);
            }

            result.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method
    public static ArrayList<Cinema> viewCinemaList(int status) throws SQLException {
        ArrayList<Cinema> cinemas = new ArrayList<>();

        try {
            Object[] params = {status};
            ResultSet result = DatabaseUtils.selectQueryById("*", "cinema", "cinema_status = ?", params);

            while (result.next()) {
                Cinema cinema = new Cinema();

                cinema.setCinemaID(result.getInt("cinema_id"));
                cinema.setCinemaName(new Name(result.getString("cinema_name")));
                cinema.setCinemaAddress(new Address(result.getString("cinema_address")));
                cinema.setCinemaPhone(result.getString("cinema_phone"));

                cinemas.add(cinema);
            }

            result.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.printf("\n%-5s %s\n", "No", "Cinema Name");
        for (int i = 0; i < cinemas.size(); i++) {
            System.out.printf("%-5d %s\n", (i + 1), cinemas.get(i).getCinemaName().getName());
        }

        return cinemas;
    }

    public void viewCinemaDetails() throws SQLException {
        System.out.printf("\nCinema Detail:\n");
        System.out.println("Cinema Name: " + getCinemaName().getName());
        System.out.println("Cinema Address: " + getCinemaAddress().getAddress());
        System.out.println("Cinema Phone: " + getCinemaPhone());
    }

    public boolean isValidOfficePhoneNumber() {
        Pattern pattern = Pattern.compile(OFFICE_PHONE_REGEX);
        Matcher matcher = pattern.matcher(cinemaPhone);

        return matcher.matches();
    }

    public void addCinema() throws SQLException {
        int rowAffected = 0;

        try {
            Object[] params = {getCinemaName().getName(), getCinemaAddress().getAddress(), getCinemaPhone()};
            String sql = "INSERT INTO `cinema` (`cinema_name`, `cinema_address`, `cinema_phone`) VALUES (?, ?, ?)";
            rowAffected = DatabaseUtils.insertQuery(sql, params);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        if (rowAffected > 0) {
            System.out.println("\nCinema successfully added...");
        }
        else {
            System.out.println("\nSomething went wrong...");
        }
    }

    public ArrayList<Hall> viewHallList(int status) throws SQLException {
        ArrayList<Hall> halls = new ArrayList<>();

        try {
            Object[] params = {cinemaID, status};
            ResultSet result = DatabaseUtils.selectQueryById("*", "hall", "cinema_id = ? AND hall_status = ?", params);

            while (result.next()) {
                Hall hall = new Hall();

                hall.setHallID(result.getInt("hall_id"));
                hall.setHallName(new Name(result.getString("hall_name")));
                hall.setHallType(result.getString("hall_type"));
                hall.calHallCapacity();

                halls.add(hall);
            }

            result.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < halls.size(); i++) {
            System.out.println((i + 1) + ". " + halls.get(i).getHallName().getName());
        }

        return halls;
    }

    public boolean addHall(Hall hall){
        int rowAffected = 0;
        boolean insertError;

        try {
            Object[] params = {cinemaID, hall.getHallName().getName(), hall.getHallType(), hall.getHallCapacity()};
            String sql = "INSERT INTO `hall` (`cinema_id`, `hall_name`, `hall_type`, `hall_capacity`) VALUES (?, ?, ?, ?)";
            rowAffected = DatabaseUtils.insertQuery(sql, params);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        if (rowAffected > 0) {
            System.out.println("\nHall successfully added...");
            insertError = false;
        }
        else {
            System.out.println("\nSomething went wrong...");
            insertError = true;
        }
        return insertError;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }

    public void setCinemaID(int cinemaID) {
        this.cinemaID = cinemaID;
    }

    public void setCinemaName(Name cinemaName) {
        this.cinemaName = cinemaName;
    }

    public void setCinemaAddress(Address cinemaAddress) {
        this.cinemaAddress = cinemaAddress;
    }

    public void setCinemaPhone(String cinemaPhone) {
        this.cinemaPhone = cinemaPhone;
    }

    public Hall getHall() {
        return hall;
    }

    public int getCinemaID() {
        return cinemaID;
    }

    public Name getCinemaName() {
        return cinemaName;
    }

    public Address getCinemaAddress() {
        return cinemaAddress;
    }

    public String getCinemaPhone() {
        return cinemaPhone;
    }

    public static ArrayList<Cinema> getCinemas() {
        return cinemas;
    }
}
