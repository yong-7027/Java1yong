package movie_management;

import Connect.DatabaseUtils;
import Driver.CrudOperations;
import Driver.Name;
import Driver.SystemClass;
import genre_management.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Movie implements CrudOperations {
    private int movieID;
    private Genre genre;
    private Name mvName;
    private ShowDate releaseDate;
    private int duration;
    private String lang;
    private String director;
    private String writter;
    private String starring;
    private String musicProvider;
    private String country;
    private String metaDescription;
    private double childTicketPrice;
    private double adultTicketPrice;
    private int status;
    private static ArrayList<Movie> movies = new ArrayList<>();

    public Movie(){
    }

    public Movie(Genre genre, Name mvName, ShowDate releaseDate, int duration, String lang, String director, String writter, String starring, String musicProvider, String country, String metaDescription, double childTicketPrice, double adultTicketPrice) {
        this.genre = genre;
        this.mvName = mvName;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.lang = lang;
        this.director = director;
        this.writter = writter;
        this.starring = starring;
        this.musicProvider = musicProvider;
        this.country = country;
        this.metaDescription = metaDescription;
        this.childTicketPrice = childTicketPrice;
        this.adultTicketPrice = adultTicketPrice;
    }

    static {
        try {
            ResultSet result = DatabaseUtils.selectQueryById("*", "movie", null, null);

            while (result.next()) {
                //Movie movie = new Movie(id, mvName, releaseDate, duration, lang, director, writter, starring, musicProvider, country, metaDescription, childTicketPrice, adultTicketPrice);
                Movie movie = new Movie();

                movie.setMovieID(result.getInt("movie_id"));
                movie.setGenre(new Genre(result.getInt("genre_id")));
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

                movies.add(movie);
            }

            result.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Show movie details
    public void viewMovieDetails() {
        System.out.printf("\nMovie Detail:\n");
        System.out.println("Movie Name: " + getMvName().getName());
        System.out.println("Release Date: " + getReleaseDate().getDate());
        System.out.println("Duration: " + getDuration() + " minutes");
        System.out.println("Language: " + getLang());
        System.out.println("Director: " + getDirector());
        System.out.println("Writter: " + getWritter());
        System.out.println("Starring: " + getStarring());
        System.out.println("Music Producer: " + getMusicProvider());
        System.out.println("Country: " + getCountry());
        System.out.printf("%s %.2f\n", "Child Ticket Price:", getChildTicketPrice());
        System.out.printf("%s %.2f\n", "Adult Ticket Price:", getAdultTicketPrice());
        System.out.println("\nSynopsis:\n" + getMetaDescription());
    }

    public void add() throws Exception {
        int rowAffected = 0;

        try {
            String insertSql = "INSERT INTO `movie`(`genre_id`, `mv_name`, `release_date`, `duration`, `lang`, `director`, `writter`, `starring`, `music`, `country`,`meta_description`, `childTicket_Price`, `adultTicket_Price`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            Object[] params = {getGenre().getGenreID(), getMvName().getName(), String.valueOf(getReleaseDate().getDate()), getDuration(), getLang(), getDirector(), getWritter(), getStarring(), getMusicProvider(), getCountry(), getMetaDescription(), getChildTicketPrice(), getAdultTicketPrice()};
            rowAffected = DatabaseUtils.insertQuery(insertSql, params);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        if (rowAffected > 0) {
            System.out.println("\nMovie successfully added...");
        }
        else {
            System.out.println("\nSomething went wrong!");
        }
    }

    public void modify() throws Exception {
        int rowAffected = 0;

        try {
            String updateSql = "UPDATE `movie` SET `genre_id`= ?, `mv_name`= ?," +
                    "`release_date`= ?,`duration`= ?,`lang`= ?," +
                    "`director`= ?,`writter`= ?,`starring`= ?,`music`= ?," +
                    "`country`= ?,`meta_description`= ?, `childTicket_Price`= ?," +
                    "`adultTicket_Price`= ? WHERE movie_id = ?";
            Object[] params = {getGenre().getGenreID(), getMvName().getName(), String.valueOf(getReleaseDate().getDate()), getDuration(), getLang(), getDirector(), getWritter(), getStarring(), getMusicProvider(), getCountry(), getMetaDescription(), getChildTicketPrice(), getAdultTicketPrice(), getMovieID()};
            rowAffected = DatabaseUtils.updateQuery(updateSql, params);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        if (rowAffected > 0) {
            System.out.println("\nThe changes have been saved.");
        }
        else {
            System.out.println("\nSomething went wrong...");
        }
    }

    // Delete Movie
    public void delete() throws SQLException {
        int rowAffected = 0;

        try {
            Object[] params = {getMovieID()};
            rowAffected = DatabaseUtils.deleteQueryById("movie", "movie_status", "movie_id", params);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        if (rowAffected > 0) {
            System.out.println("\nThe movie has been deleted.");
        } else {
            System.out.println("\nSomething went wrong...");
        }
    }

    public static ArrayList<Movie> viewMovieListByFilter(Scanner sc) throws Exception {
        LocalDate currentDate = LocalDate.now();
        ArrayList<Movie> moviesAfterFiltered = new ArrayList<>();
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
                    return null;
                case 1:
                    // 1. future movie
                    LocalDate futureMovie = currentDate.plusDays(1);
                    moviesAfterFiltered = showMovieListAfterFiltered(futureMovie, null, 1);
                    break;
                case 2:
                    // 2. within 1 week
                    LocalDate oneWeekAgo = currentDate.minusWeeks(1);
                    moviesAfterFiltered = showMovieListAfterFiltered(oneWeekAgo, currentDate, 1);
                    break;
                case 3:
                    // 3. within 1 month
                    LocalDate oneMonthAgo = currentDate.minusMonths(1);
                    moviesAfterFiltered = showMovieListAfterFiltered(oneMonthAgo, currentDate, 1);
                    break;
                case 4:
                    // 4. within 3 month
                    LocalDate threeMonthAgo = currentDate.minusMonths(3);
                    moviesAfterFiltered = showMovieListAfterFiltered(threeMonthAgo, currentDate, 1);
                    break;
                case 5:
                    // 5. within 1 year
                    LocalDate oneYearAgo = currentDate.minusYears(1);
                    moviesAfterFiltered = showMovieListAfterFiltered(oneYearAgo, currentDate, 1);
                    break;
                case 6:
                    // 6. all movie
                    moviesAfterFiltered = showMovieListAfterFiltered(null, null, 1);
                    break;
            }
        } while (moviesAfterFiltered.isEmpty() && choice != 0);

        return moviesAfterFiltered;
    }

    public static ArrayList<Movie> showMovieListAfterFiltered(LocalDate expectedDate, LocalDate currentDate, int status){
        ArrayList<Movie> movies = new ArrayList<>();

        try {
            Object[] params = {status};
            ResultSet result = DatabaseUtils.selectQueryById("*", "movie", "movie_status = ?", params);

            while (result.next()) {
                Movie movie = new Movie();

                movie.setMovieID(result.getInt("movie_id"));
                movie.setGenre(new Genre(result.getInt("genre_id")));
                movie.setMvName(new Name(result.getString("mv_name")));
                movie.setReleaseDate(new ShowDate(result.getDate("release_date").toLocalDate()));
                movie.setDuration(result.getInt("duration"));
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
                movie.setStutus(result.getInt("movie_status"));

                movies.add(movie);
            }

            result.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Movie> moviesAfterFiltered = new ArrayList<>();

        System.out.printf("\n%-5s %s\n", "No", "Movie Name");

        int countFuture = 1;
        int countAll = 1;
        int countOther = 1;

        for (int i = 0; i < movies.size(); i++) {
            LocalDate localReleaseDate = movies.get(i).getReleaseDate().getDate();

            if (expectedDate != null && currentDate == null) {  // Future Movie(s)
                if (localReleaseDate.equals(expectedDate) || localReleaseDate.isAfter(expectedDate)) {
                    System.out.printf("%-5d %s\n", countFuture, movies.get(i).getMvName().getName());
                    moviesAfterFiltered.add(movies.get(i));
                    countFuture++;
                }
            } else if (expectedDate == null && currentDate == null) {  // All Movies
                System.out.printf("%-5d %s\n", countAll, movies.get(i).getMvName().getName());
                moviesAfterFiltered.add(movies.get(i));
                countAll++;
            }
            else {
                if (localReleaseDate.equals(expectedDate) || (localReleaseDate.isAfter(expectedDate) && localReleaseDate.isBefore(currentDate))) {
                    System.out.printf("%-5d %s\n", countOther, movies.get(i).getMvName().getName());
                    moviesAfterFiltered.add(movies.get(i));
                    countOther++;
                }
            }
        }

        if (moviesAfterFiltered.isEmpty()) {
            System.out.println("Sorry, no movie found!");
        }

        return moviesAfterFiltered;
    }

    // Method for add movie
    public static String getMultipleValues(Scanner sc, String propertyName, String propertyPluralName) throws Exception {
        StringBuilder result = new StringBuilder();
        String continues = "N";
        boolean error = true;

        do {
            System.out.print("\nEnter the movie " + propertyName + ": ");
            String value = sc.nextLine();

            Name name = new Name(value);

            String errorMessage = MovieValidator.checkValue(value, propertyName);

            if (errorMessage == null) {
                name.capitalizeWords();
                error = false;

                do {
                    System.out.println("\nIs there another " + propertyPluralName + " for this movie? (Y / N)");
                    System.out.print("Answer: ");
                    String answer = sc.next();
                    sc.nextLine();

                    continues = SystemClass.askForContinue(answer);

                } while (continues.equals("Invalid"));

                if (continues.equals("Y")) {
                    result.append(name.getName()).append(", ");
                } else {
                    result.append(name.getName());
                }
            } else {
                System.out.println(errorMessage);
                error = true;
            }
        } while (error || continues.equals("Y"));

        return result.toString();
    }

    public static String getMultipleChosens(Scanner sc, String[] array, String propertyName){
        boolean error;

        do {
            System.out.println("\nAvailable " + propertyName);
            try {
                for (int i = 0; i < array.length; i++) {
                    System.out.println((i + 1) + ". " + array[i]);
                }

                System.out.print("\nEnter your selection: ");
                int choice = sc.nextInt();
                sc.nextLine();

                String errorMessage = MovieValidator.checkRange(choice, array);

                if (errorMessage == null) {
                    return array[choice - 1];  // Array starts from index 0
                } else {
                    System.out.println(errorMessage);
                    error = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid number!");
                sc.nextLine();
                error = true;
            }
        } while (error);

        return null;
    }

    public static double getTicketPrice(Scanner sc, String propertyName){
        boolean error;

        do {
            try {
                System.out.print("\nEnter movie " + propertyName + " ticket price (RM): ");
                double mvTicketPrice = sc.nextDouble();
                sc.nextLine();

                String errorMessage = MovieValidator.checkTicketPrice(mvTicketPrice);

                if (errorMessage == null) {
                    mvTicketPrice = MovieUtils.formatDouble(mvTicketPrice);
                    return mvTicketPrice;
                }
                else {
                    System.out.println(errorMessage);
                    error = true;
                }
            }
            catch (InputMismatchException e) {
                System.out.println("Please enter a valid " + propertyName + " ticket price!");
                sc.nextLine();
                error = true;
            }
        } while (error);

        return 0;
    }

    // Method for modify movie
    public int modifyMovieDetail(Scanner sc) throws SQLException {
        boolean error = true;

        do {
            try {
                Object[] params = {getGenre().getGenreID()};
                ResultSet result = DatabaseUtils.selectQueryById("genre_name", "genre", "genre_id = ?", params);

                try {
                    int count = 1;
                    System.out.printf("\nMovie Detail:\n");
                    System.out.println(count + ". Movie Name: " + getMvName().getName());
                    count++;
                    if (result.next()) {
                        System.out.println(count + ". Genre: " + result.getString("genre_name"));
                        count++;
                    }
                    System.out.println(count + ". Release Date: " + getReleaseDate().getDate());
                    count++;
                    System.out.println(count + ". Duration: " + getDuration() + " minutes");
                    count++;
                    System.out.println(count + ". Language: " + getLang());
                    count++;
                    System.out.println(count + ". Director: " + getDirector());
                    count++;
                    System.out.println(count + ". Writter: " + getWritter());
                    count++;
                    System.out.println(count + ". Starring: " + getStarring());
                    count++;
                    System.out.println(count + ". Music Producer: " + getMusicProvider());
                    count++;
                    System.out.println(count + ". Country: " + getCountry());
                    count++;
                    System.out.println("\n" + count + ". Synopsis:\n" + getMetaDescription() + "\n");
                    count++;
                    System.out.printf("%d. %s: %.2f\n", count, "Child Ticket Price", getChildTicketPrice());
                    count++;
                    System.out.printf("%d. %s: %.2f\n", count, "Adult Ticket Price", getAdultTicketPrice());

                    System.out.print("\nEnter the serial number of the movie information you want to change (0 - Stop): ");
                    int serialNum = sc.nextInt();
                    sc.nextLine();

                    if (serialNum < 0 || serialNum > count) {
                        System.out.println("Your choice is not among the available options! PLease try again.");
                        error = true;
                    } else {
                        return serialNum;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Please enter a valid choice!");
                    sc.nextLine();
                    error = true;
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        } while (error);
        return 0;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public void setGenre(Genre genre){
        this.genre = genre;
    }

    public void setMvName(Name mvName) {
        this.mvName = mvName;
    }

    public void setReleaseDate(ShowDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setWritter(String writter) {
        this.writter = writter;
    }

    public void setStarring(String starring) {
        this.starring = starring;
    }

    public void setMusicProvider(String musicProvider) {
        this.musicProvider = musicProvider;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    public void setChildTicketPrice(double childTicketPrice) {
        this.childTicketPrice = childTicketPrice;
    }

    public void setAdultTicketPrice(double adultTicketPrice) {
        this.adultTicketPrice = adultTicketPrice;
    }

    public void setStutus(int stutus) {
        this.status = stutus;
    }

    public int getMovieID() {
        return movieID;
    }

    public Genre getGenre(){
        return genre;
    }

    public Name getMvName() {
        return mvName;
    }

    public ShowDate getReleaseDate() {
        return releaseDate;
    }

    public int getDuration() {
        return duration;
    }

    public String getLang() {
        return lang;
    }

    public String getDirector() {
        return director;
    }

    public String getWritter() {
        return writter;
    }

    public String getStarring() {
        return starring;
    }

    public String getMusicProvider() {
        return musicProvider;
    }

    public String getCountry() {
        return country;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public double getChildTicketPrice() {
        return childTicketPrice;
    }

    public double getAdultTicketPrice() {
        return adultTicketPrice;
    }

    public int getStutus() {
        return status;
    }

    public static ArrayList<Movie> getAllMovies() {
        return movies;
    }
}