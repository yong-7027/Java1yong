package timetable_management;

import Connect.DatabaseUtils;
import Driver.Name;
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
        super(movie, hall, showDate, startTime);
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
                    addSchedule.addSchedule(sc);
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

    private void addSchedule(Scanner sc) throws Exception {
        ArrayList<Integer> foundMovieIDs = new ArrayList<>();
        int movieID = 1, hallID = 1;
        boolean error = true;

        do {
            foundMovieIDs = viewMovieListByFilter(sc);
            do {
                try {
                    System.out.print("\nEnter the movie id (0 - Back): ");
                    movieID = sc.nextInt();
                    sc.nextLine();

                    if (foundMovieIDs.contains(movieID) || movieID == 0) {
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

        error = true;
        do {
            try {
                System.out.print("\nEnter the hall id: ");
                hallID = sc.nextInt();
                sc.nextLine();
                error = false;
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid hall id!");
                sc.nextLine();
            }
        } while (error);

        // Show Date
        error = true;
        String date = null;
        ShowDate addDate = null;
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
                    addDate = new ShowDate(year, month, day);
                    validDate = addDate.isValidDate();

                    if (validDate == true) {
                        //addDate.setMonth(month - 1);  // Months are 0-based in calendar
                        //addDate.parseDate();

                        String errorMessage = addDate.checkLocalDate();
                        if (errorMessage == null) {
                            error = false;
                        } else {
                            System.out.println(errorMessage);
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

        // Movie
        boolean sqlError = true;
        ResultSet result = null;
        Movie movie = new Movie();

        do {
            try {
                Object[] params = {movieID};
                result = DatabaseUtils.selectQueryById("*", "movie", "movie_id = ? LIMIT 1", params);
                sqlError = false;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } while (sqlError);

        while (result.next()) {
            movie.setMovieID(result.getInt("movie_id"));
            movie.setGenreID(result.getInt("genre_id"));
            movie.setMvName(new Name(result.getString("mv_name")));
            movie.setReleaseDate(new ShowDate(result.getDate("release_date").toLocalDate()));
            movie.setDuration(result.getInt("duration"));
            movie.setLang(result.getString("lang"));
            movie.setDirector(result.getString("director"));
            movie.setWritter(result.getString("writter"));
            movie.setStarring(result.getString("starring"));
            movie.setMusicProvider(result.getString("music"));
            movie.setCountry(result.getString("country"));
            movie.setMetaDescription(result.getString("meta_description"));
            movie.setChildTicketPrice(result.getDouble("childTicket_Price"));
            movie.setAdultTicketPrice(result.getDouble("adultTicket_Price"));
        }

        result.close();

        // Hall
        sqlError = true;
        result = null;
        Hall hall = new Hall();

        do {
            try {
                Object[] params = {hallID};
                result = DatabaseUtils.selectQueryById("*", "hall", "hall_id = ? LIMIT 1", params);
                sqlError = false;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } while (sqlError);

        while (result.next()) {
            hall.setHallID(result.getInt("hall_id"));
        }

        AdminTimeTable adminTimeTable = new AdminTimeTable(movie, hall, addDate);

        LocalTime[] selectedTimeSlots = adminTimeTable.availableTimeSlots(sc);

        int rowAffected = 0;
        try {
            Object[] params = {hallID, movieID, date, selectedTimeSlots[0], selectedTimeSlots[1]};
            String sql = "INSERT INTO `timeTable` (`hall_id`, `movie_id`, `movie_showDate`, `movie_startTime`, `movie_endTime`) VALUES (?, ?, ?, ?, ?)";
            rowAffected = DatabaseUtils.insertQuery(sql, params);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        if (rowAffected > 0) {
            System.out.println("\nSchedule successfully added...");
        }
        else {
            System.out.println("\nSomething went wrong...");
        }
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

                if (choice < 0 || choice > 2) {
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
                ArrayList<Integer> foundMovieIDs = new ArrayList<>();
                int movieID = 1;
                do {
                    foundMovieIDs = viewMovieListByFilter(sc);
                    do {
                        try {
                            System.out.print("\nEnter the movie id (0 - Back): ");
                            movieID = sc.nextInt();
                            sc.nextLine();

                            if (foundMovieIDs.contains(movieID) || movieID == 0) {
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

    private static ArrayList<Integer> viewMovieListByFilter(Scanner sc) throws Exception {
        LocalDate currentDate = LocalDate.now();
        ArrayList<Integer> foundMovieIDs = new ArrayList<>();
        boolean error = true;
        int choice = 0;

        do {
            do {
                try {
                    System.out.println("\nPlease select a movie filtering from the list below: ");
                    System.out.println("1. Future Movie");
                    System.out.println("2. Movie within 1 week");
                    System.out.println("3. Movie within 1 month");
                    System.out.println("4. Movie within 3 months");
                    System.out.println("5. Movie within 1 year");
                    System.out.println("6. All movies");
                    System.out.print("\nEnter your selection (0 - Back): ");
                    choice = sc.nextInt();
                    sc.nextLine();

                    if (choice < 0 || choice > 6) {
                        System.out.println("Your choice is not among the available options! PLease try again.");
                    } else {
                        error = false;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Please enter a valid choice!");
                    sc.nextLine();
                }
            } while (error);

            switch (choice) {
                case 0:
                    break;
                case 1:
                    // 1. future movie
                    LocalDate futureMovie = currentDate.plusDays(1);
                    foundMovieIDs = showMovieListAfterFiltered(futureMovie, null);
                    break;
                case 2:
                    // 2. within 1 week
                    LocalDate oneWeekAgo = currentDate.minusWeeks(1);
                    foundMovieIDs = showMovieListAfterFiltered(oneWeekAgo, currentDate);
                    break;
                case 3:
                    // 3. within 1 month
                    LocalDate oneMonthAgo = currentDate.minusMonths(1);
                    foundMovieIDs = showMovieListAfterFiltered(oneMonthAgo, currentDate);
                    break;
                case 4:
                    // 4. within 3 month
                    LocalDate threeMonthAgo = currentDate.minusMonths(3);
                    foundMovieIDs = showMovieListAfterFiltered(threeMonthAgo, currentDate);
                    break;
                case 5:
                    // 5. within 1 year
                    LocalDate oneYearAgo = currentDate.minusYears(1);
                    foundMovieIDs = showMovieListAfterFiltered(oneYearAgo, currentDate);
                    break;
                case 6:
                    // 6. all movie
                    foundMovieIDs = showMovieListAfterFiltered(null, null);
                    break;
            }
        } while (foundMovieIDs.isEmpty());

        return foundMovieIDs;
    }

    private static ArrayList<Integer> showMovieListAfterFiltered(LocalDate expectedDate, LocalDate currentDate){
        ArrayList<Movie> movies = Movie.getAllMovies();
        ArrayList<Integer> foundMovieIDs = new ArrayList<>(); // store the movieID found

        System.out.printf("\n%-5s %s\n", "No", "Movie Name");

        for (int i = 0; i < movies.size(); i++) {
            //Date date = movies.get(i).getReleaseDate().getDate();
            //Instant instant = date.toInstant();
            //LocalDate localReleaseDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate localReleaseDate = movies.get(i).getReleaseDate().getDate();

            if (expectedDate != null && currentDate == null) {  // Future Movie(s)
                if (localReleaseDate.equals(expectedDate) || localReleaseDate.isAfter(expectedDate)) {
                    System.out.printf("%-5d %s\n", movies.get(i).getMovieID(), movies.get(i).getMvName());
                    foundMovieIDs.add(movies.get(i).getMovieID());
                }
            } else if (expectedDate == null && currentDate == null) {  // All Movies
                System.out.printf("%-5d %s\n", movies.get(i).getMovieID(), movies.get(i).getMvName());
                foundMovieIDs.add(movies.get(i).getMovieID());
            }
            else {
                if (localReleaseDate.equals(expectedDate) || (localReleaseDate.isAfter(expectedDate) && localReleaseDate.isBefore(currentDate))) {
                    System.out.printf("%-5d %s\n", movies.get(i).getMovieID(), movies.get(i).getMvName());
                    foundMovieIDs.add(movies.get(i).getMovieID());
                }
            }
        }

        if (foundMovieIDs.isEmpty()) {
            System.out.println("Sorry, no movie found!");
        }

        return foundMovieIDs;
    }

    private LocalTime[] availableTimeSlots(Scanner sc) throws SQLException {
        ResultSet result = null;
        String showDate = String.valueOf(getShowDate().getDate());

        try {
            Object[] params2 = {getHall().getHallID(), showDate};
            result = DatabaseUtils.selectQueryById("movie_startTime, movie_endTime", "timeTable", "hall_id = ? AND movie_showDate = ?", params2);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<LocalTime[]> timeSlots = new ArrayList<>();
        while (result.next()) {
            LocalTime startTime = result.getTime("movie_startTime").toLocalTime();
            LocalTime endTime = result.getTime("movie_endTime").toLocalTime();

            LocalTime[] timeSlot = {startTime, endTime};
            timeSlots.add(timeSlot);
        }

        // 把 duration round up to 5 minutes
        Duration duration = roundUpToNearestFiveMinutes(getMovie().getDuration());

        // 间隔时间 15分钟
        int interval = 5;

        // 营业时间 11:00:00 - 23:00:00
        LocalTime openingTime = LocalTime.of(11, 0, 0);
        LocalTime closingTime = LocalTime.of(23, 0, 0);

        ArrayList<LocalTime[]> availableTimeSlots = new ArrayList<>();
        LocalTime startTime = openingTime;

        while (startTime.plus(duration).isBefore(closingTime)) {
            boolean conflict = false;

            for (LocalTime[] timeSlot : timeSlots) {
                LocalTime scheduledStartTime = timeSlot[0]; // 13:00:00
                LocalTime scheduledEndTime = timeSlot[1]; // 15:10:00

                // 电影开始时间 小过 已经被安排时间表的结束时间 和 电影结束时间 大过 已经被安排时间表的开始时间 => 在它们之间
                if (startTime.isBefore(scheduledEndTime.plusMinutes(15)) && startTime.plus(duration).isAfter(scheduledStartTime.minusMinutes(15))) {
                    conflict = true;
                    break;
                }
            }

            if (!conflict) {
                availableTimeSlots.add(new LocalTime[]{startTime, startTime.plus(duration)});
                startTime = startTime.plusMinutes(10);
            }

            startTime = startTime.plusMinutes(interval);
        }

        boolean error = true;
        int choice = 0;
        do {
            try {
                System.out.println("\nSelect the available time slot: ");
                for (int i = 0; i < availableTimeSlots.size(); i++) {
                    LocalTime[] availableTimeSlot = availableTimeSlots.get(i);
                    System.out.println((i + 1) + ". Start Time: " + availableTimeSlot[0] + " | End Time: " + availableTimeSlot[1]);
                }

                System.out.print("\nEnter your selection: ");
                choice = sc.nextInt();
                sc.nextLine();

                if (choice > 0 && choice <= availableTimeSlots.size()) {
                    error = false;
                } else {
                    System.out.println("Your choice is not among the available options! PLease try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid choice!");
                sc.nextLine();
            }
        } while (error);

        return availableTimeSlots.get(choice - 1);
    }
}