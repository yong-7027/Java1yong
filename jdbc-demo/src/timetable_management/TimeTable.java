package timetable_management;

import Connect.DatabaseUtils;
import Driver.Name;
import cinema_management.Cinema;
import hall_management.Hall;
import movie_management.Movie;
import movie_management.MovieValidator;
import movie_management.ShowDate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TimeTable {
    private Movie movie;
    private Hall hall;
    private ShowDate showDate;
    private LocalTime startTime;
    private LocalTime endTime;

    // Constructor
    public TimeTable() {
    }

    public TimeTable(Movie movie, Hall hall, ShowDate showDate, LocalTime startTime) {
        this.movie = movie;
        this.hall = hall;
        this.showDate = showDate;
        this.startTime = startTime;
        calculateEndTime(movie, startTime);
    }

    public TimeTable(Movie movie, Hall hall, ShowDate showDate){
        this.movie = movie;
        this.hall = hall;
        this.showDate = showDate;
    }

    // Method
    public static ArrayList<TimeTable> viewSchedule(Scanner sc) throws Exception {
        // Cinema
        int cinemaNo = 0;
        boolean error = true;
        ArrayList<Cinema> cinemas = new ArrayList<>();
        do {
            try {
                System.out.print("\nSelect the cinema you want to view the schedule: ");
                cinemas = Cinema.viewCinemaList(1);
                System.out.print("\nEnter the cinema no: ");
                cinemaNo = sc.nextInt();
                sc.nextLine();

                if (cinemaNo > 0 && cinemaNo <= cinemas.size() && cinemas.get(cinemaNo - 1).getStatus() == 1) {
                    error = false;
                }
                else {
                    System.out.println("Your choice is not among the available options! PLease try again.");
                }
            }
            catch (InputMismatchException e) {
                System.out.println("Please enter a valid cinema no!");
                sc.nextLine();
            }
        } while (error);

        // Hall
        int hallNo = 0;
        error = true;
        ArrayList<Hall> halls = new ArrayList<>();
        do {
            try {
                System.out.println("\nSelect the hall: ");
                halls = cinemas.get(cinemaNo - 1).viewHallList(1);
                System.out.print("\nEnter the hall no: ");
                hallNo = sc.nextInt();
                sc.nextLine();

                if (hallNo > 0 && hallNo <= halls.size() && halls.get(hallNo - 1).getStatus() == 1) {
                    error = false;
                }
                else {
                    System.out.println("Your choice is not among the available options! PLease try again.");
                }
            }
            catch (InputMismatchException e) {
                System.out.println("Please enter a valid hall no!");
                sc.nextLine();
            }
        } while (error);

        // Show Date
        error = true;
        String date = null;
        ShowDate viewDate = null;
        boolean validDate = false;
        do {
            System.out.print("\nEnter movie show date (YYYY-MM-DD): ");
            date = sc.nextLine();

            if (date.trim().isEmpty()) {
                System.out.println("Please enter the show date.");
            } else {
                try {
                    String[] parts = date.split("-");
                    int year = Integer.parseInt(parts[0]);  // Java's built-in method for converting strings to integers (int type)
                    int month = Integer.parseInt(parts[1]);
                    int day = Integer.parseInt(parts[2]);

                    // 验证日期是否 valid
                    viewDate = new ShowDate(year, month, day);
                    validDate = viewDate.isValidDate();

                    if (validDate == true) {
                        error = false;
                    } else {
                        System.out.println("Please enter a valid date!");
                        error = true;
                    }
                } catch (Exception e) {
                    System.out.println("The date format entered in wrong!");
                }
            }
        } while (error);

        ResultSet result = null;
        try {
            Object[] params2 = {halls.get(hallNo - 1).getHallID(), date, 1};
            result = DatabaseUtils.selectQueryById("*", "timeTable", "hall_id = ? AND movie_showDate = ? AND timeTable_status = ?", params2);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<TimeTable> schedules = new ArrayList<>();
        while (result.next()) {
            int movieID = result.getInt("movie_id");
            LocalTime startTime = result.getTime("movie_startTime").toLocalTime();

            // Movie
            ResultSet result2 = null;
            Movie movie = new Movie();

            try {
                Object[] params = {movieID};
                result2 = DatabaseUtils.selectQueryById("*", "movie", "movie_id = ? LIMIT 1", params);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            while (result2.next()) {
                movie.setMovieID(result2.getInt("movie_id"));
                movie.setGenreID(result2.getInt("genre_id"));
                movie.setMvName(new Name(result2.getString("mv_name")));
                movie.setReleaseDate(new ShowDate(result2.getDate("release_date").toLocalDate()));
                movie.setDuration(result2.getInt("duration"));
                movie.setLang(result2.getString("lang"));
                movie.setDirector(result2.getString("director"));
                movie.setWritter(result2.getString("writter"));
                movie.setStarring(result2.getString("starring"));
                movie.setMusicProvider(result2.getString("music"));
                movie.setCountry(result2.getString("country"));
                movie.setMetaDescription(result2.getString("meta_description"));
                movie.setChildTicketPrice(result2.getDouble("childTicket_Price"));
                movie.setAdultTicketPrice(result2.getDouble("adultTicket_Price"));
            }

            // Hall
            result2 = null;
            Hall hall = new Hall();

            try {
                Object[] params = {halls.get(hallNo - 1).getHallID()};
                result2 = DatabaseUtils.selectQueryById("*", "hall", "hall_id = ? LIMIT 1", params);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            while (result2.next()) {
                hall.setHallID(result2.getInt("hall_id"));
            }

            TimeTable schedule = new TimeTable(movie, hall, viewDate, startTime);
            schedules.add(schedule);
        }

        System.out.println("\nMovie Schedule List for " + date + " at Hall " + halls.get(hallNo - 1).getHallID() + ":\n");
        if (!schedules.isEmpty()) {
            System.out.printf("%-30s %15s %15s\n", "Movie Name", "Start Time", "End Time");
            for (int i = 0; i < schedules.size(); i++) {
                System.out.printf((i + 1) + ". %-20s %17s %17s\n", schedules.get(i).movie.getMvName().getName(), schedules.get(i).startTime, schedules.get(i).endTime);
            }
        }
        else {
            System.out.println("No schedules available for the selected date and hall!");
        }

        return schedules;
    }

    public void calculateEndTime(Movie movie, LocalTime startTime){
        // Change the duration's data type from int to Duration (in minutes)
        Duration duration = Duration.ofMinutes(movie.getDuration());

        endTime = startTime.plusMinutes(duration.toMinutes());
    }

    public static Duration roundUpToNearestFiveMinutes(int duration) {
        int hours = duration / 60;
        int minutes = duration % 60;
        LocalTime time = LocalTime.of(hours, minutes);

        int minute = time.getMinute();
        int roundUpMinute = ((int) Math.ceil(minute / 5.0)) * 5;

        if (roundUpMinute == 60) {
            hours++;
            roundUpMinute = 0;
        }

        return Duration.ofMinutes((hours * 60) + roundUpMinute);
    }

    // Setter
    public void setShowDate(ShowDate showDate) {
        this.showDate = showDate;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    // Getter
    public Movie getMovie() {
        return movie;
    }

    public Hall getHall() {
        return hall;
    }

    public ShowDate getShowDate() {
        return showDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
}
