package timetable_management;

import Connect.DatabaseUtils;
import Driver.Name;
import genre_management.Genre;
import hall_management.Hall;
import movie_management.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.util.*;

public class AdminTimeTable extends TimeTable{
    private AdminTimeTable(){
    }

    private AdminTimeTable(Movie movie, Hall hall, ShowDate showDate, LocalTime startTime){
        //super(movie, hall, showDate, startTime);
    }

    private AdminTimeTable(Movie movie, Hall hall, ShowDate showDate){
        super(movie, hall, showDate);
    }

    public static void adminTimeTable(Scanner sc) throws Exception{
        //AdminTimeTable newShow = new AdminTimeTable();
        //newShow.addSchedule(sc);
        String exit = "N";
        int choice = 0;
        do {
            try {
                System.out.println("\nSelect the operation:");
                System.out.println("1. View Schedule");
                System.out.println("2. Add Schedule");
                System.out.println("3. Modify Schedule");
                System.out.println("4. Delete Schedule");
                System.out.print("\nEnter your selection (0 - Exit): ");

                choice = sc.nextInt();
                sc.nextLine();
            }
            catch (InputMismatchException e) {
                System.out.println("Please enter a valid choice!");
                sc.nextLine();
                exit = "N";
            }

            switch (choice) {
                case 0:
                    do {
                        System.out.println("\nAre you sure you want to exit? (Y / N)");
                        System.out.print("Answer: ");
                        String answer = sc.next();
                        sc.nextLine();

                        exit = MovieUtils.askForContinue(answer);
                    } while (exit.equals("Invalid"));

                    if (exit.equals("Y")) {
                        System.out.println("\nThanks for your using! GoodBye.");
                    }
                    break;
                case 1:
                    ArrayList<TimeTable> viewTimeTables = viewSchedule(sc);
                    break;
                case 2:
                    AdminTimeTable addSchedule = new AdminTimeTable();
                    // addSchedule.addSchedule(sc);
                    break;
                case 3:
                    boolean error = true;
                    int scheduleNo = 0;
                    ArrayList<TimeTable> timeTables = viewSchedule(sc);
                    do {
                        try {
                            System.out.print("\nEnter the schedule no. you want to modify (0 - Back): ");
                            scheduleNo = sc.nextInt();
                            sc.nextLine();

                            if (scheduleNo < 0 || scheduleNo > timeTables.size()) {
                                System.out.println("Your choice is not among the available options! PLease try again.");
                            }
                            else {
                                error = false;
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Please enter a valid schedule no.");
                            sc.nextLine();
                        }
                    } while (error);
                    AdminTimeTable modifySchedule = new AdminTimeTable(timeTables.get(scheduleNo - 1).getMovie(), timeTables.get(scheduleNo - 1).getHall(), timeTables.get(scheduleNo - 1).getShowDate(), timeTables.get(scheduleNo - 1).getStartTime());
                    modifySchedule.modifySchedule(sc);
                    break;
                case 4:
                    break;
                default:
                    System.out.println("Your choice is not among the available options! PLease try again.");
                    exit = "N";
            }
        } while (exit.equals("N"));
    }

    private void modifySchedule(Scanner sc) throws Exception {
        boolean error = true;

        error = true;
        int choice = 0;
        do {
            try {
                System.out.println("\nSelect the operation:");
                System.out.println("1. Modify the movie show time");
                System.out.println("2. Modify the movie show date");
                System.out.println("3. Modify the movie to be played");
                System.out.println("4. Modify the location of the movie to be played");
                System.out.print("\nEnter your selection (0 - Back): ");
                choice = sc.nextInt();
                sc.nextLine();

                if (choice < 0 || choice > 4) {
                    System.out.println("Your choice is not among the available options! PLease try again.");
                } else {
                    error = false;
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid operation no.");
                sc.nextLine();
            }
        } while (error);

        switch (choice) {
            case 1:
                LocalTime[] selectedTimeSlots = availableTimeSlots(sc);

                int rowAffected = 0;

                try {
                    Object[] params = {selectedTimeSlots[0], selectedTimeSlots[1], getHall().getHallID(), getMovie().getMovieID(), String.valueOf(getShowDate().getDate()), getStartTime()};
                    String sql = "UPDATE `timeTable` SET `movie_startTime` = ?, `movie_endTime` = ? WHERE `hall_id` = ? AND `movie_id` = ? AND `movie_showDate` = ? AND `movie_startTime` = ?";
                    rowAffected = DatabaseUtils.updateQuery(sql, params);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                if (rowAffected > 0) {
                    System.out.println("\nSchedule successfully updated...");
                } else {
                    System.out.println("\nSomething went wrong...");
                }
                break;
            case 2:
                // 日期调整
                error = true;
                String date = null, oldShowDate = null;
                ShowDate modifyDate = null;  // Store the old show date
                boolean validDate = false;

                do {
                    System.out.print("\nEnter the new movie show date (YYYY-MM-DD): ");
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
                            modifyDate = new ShowDate(year, month, day);
                            validDate = modifyDate.isValidDate();

                            if (validDate == true) {
                                //modifyDate.setMonth(month - 1);  // Months are 0-based in calendar
                                //modifyDate.parseDate();

                                String errorMessage = modifyDate.checkLocalDate();

                                if (errorMessage == null) {
                                    oldShowDate = String.valueOf(getShowDate().getDate());
                                    super.setShowDate(modifyDate);
                                    error = false;
                                } else {
                                    System.out.println(errorMessage);
                                    error = true;
                                }
                            } else {
                                System.out.println("Please enter a valid date!");
                                error = true;
                            }
                        } catch (Exception e) {
                            System.out.println("The date format entered in wrong!");
                        }
                    }
                } while (error);

                // 时间调整
                selectedTimeSlots = availableTimeSlots(sc);

                rowAffected = 0;
                try {
                    Object[] params = {String.valueOf(getShowDate().getDate()), selectedTimeSlots[0], selectedTimeSlots[1], getHall().getHallID(), getMovie().getMovieID(), oldShowDate, getStartTime()};
                    String sql = "UPDATE `timeTable` SET `movie_showDate` = ?, `movie_startTime` = ?, `movie_endTime` = ? WHERE `hall_id` = ? AND `movie_id` = ? AND `movie_showDate` = ? AND `movie_startTime` = ?";
                    rowAffected = DatabaseUtils.updateQuery(sql, params);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                if (rowAffected > 0) {
                    System.out.println("\nSchedule successfully updated...");
                } else {
                    System.out.println("\nSomething went wrong...");
                }
                break;
            case 3:
                // Modify the movie to be played
                ArrayList<Movie> moviesAfterFiltered;
                int movieID = 1;
                do {
                    moviesAfterFiltered = Movie.viewMovieListByFilter(sc);
                    do {
                        try {
                            System.out.print("\nEnter the movie id (0 - Back): ");
                            movieID = sc.nextInt();
                            sc.nextLine();

                            if (movieID == 0 || (movieID > 0 && movieID <= moviesAfterFiltered.size() && moviesAfterFiltered.get(movieID - 1).getStutus() == 1)) {
                                error = false;
                            } else {
                                System.out.println("Your choice is not among the available options! PLease try again.");
                                error = true;
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Please enter a valid movie id!");
                            sc.nextLine();
                            error = true;
                        }
                    } while (error);
                } while (movieID == 0);
                break;
        }
    }
}