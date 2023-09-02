package cinema_management;

import Connect.DatabaseUtils;
import hall_management.Hall;
import movie_management.Movie;
import movie_management.ShowDate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Cinema {
    private Hall hall;
    private int cinemaID;
    private String cinemaName;
    private String cinemaAddress;
    private String cinemaPhone;
    private static ArrayList<Cinema> cinemas = new ArrayList<>();

    public Cinema(){
    }

    public Cinema(Hall hall, int cinemaID, String cinemaName, String cinemaAddress, String cinemaPhone) {
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
                cinema.setCinemaName(result.getString("cinema_name"));
                cinema.setCinemaAddress(result.getString("cinema_address"));
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
    public static ArrayList<Cinema> viewCinemaList() throws SQLException {
        ArrayList<Cinema> cinemas = new ArrayList<>();

        try {
            ResultSet result = DatabaseUtils.selectQueryById("*", "cinema", null, null);

            while (result.next()) {
                Cinema cinema = new Cinema();

                cinema.setCinemaID(result.getInt("cinema_id"));
                cinema.setCinemaName(result.getString("cinema_name"));
                cinema.setCinemaAddress(result.getString("cinema_address"));
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
            System.out.printf("%-5d %s\n", (i + 1), cinemas.get(i).getCinemaName());
        }

        return cinemas;
    }

    public static ArrayList<Hall> viewHallList(int cinemaID) throws SQLException {
        ArrayList<Hall> halls = new ArrayList<>();

        try {
            Object[] params = {cinemaID};
            ResultSet result = DatabaseUtils.selectQueryById("*", "hall", "cinema_id = ?", params);

            while (result.next()) {
                Hall hall = new Hall();

                hall.setHallID(result.getInt("hall_id"));

                halls.add(hall);
            }

            result.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < halls.size(); i++) {
            System.out.println((i + 1) + ". Hall " + halls.get(i).getHallID());
        }

        return halls;
    }

    public boolean addHall(Hall hall){
        int rowAffected = 0;
        try {
            Object[] params = {cinemaID, hall.getHallName(), hall.getHallType(), hall.getHallCapacity()};
            String sql = "INSERT INTO `hall` (`cinema_id`, `hall_name`, `hall_type`, `hall_capacity`) VALUES (?, ?, ?, ?)";
            rowAffected = DatabaseUtils.insertQuery(sql, params);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        if (rowAffected > 0) {
            System.out.println("\nHall successfully added...");
            return true;
        }
        else {
            System.out.println("\nSomething went wrong...");
            return false;
        }
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }

    public void setCinemaID(int cinemaID) {
        this.cinemaID = cinemaID;
    }

    public void setCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }

    public void setCinemaAddress(String cinemaAddress) {
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

    public String getCinemaName() {
        return cinemaName;
    }

    public String getCinemaAddress() {
        return cinemaAddress;
    }

    public String getCinemaPhone() {
        return cinemaPhone;
    }

    public static ArrayList<Cinema> getCinemas() {
        return cinemas;
    }
}
