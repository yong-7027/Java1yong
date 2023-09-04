package movie_management;

import Connect.DatabaseUtils;
import Driver.Name;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Movie {
    private int movieID;
    private int genreID;
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
    private static ArrayList<Movie> movies = new ArrayList<>();

    public Movie(){
    }

    public Movie(Name mvName, ShowDate releaseDate, int duration, String lang, String director, String writter, String starring, String musicProvider, String country, String metaDescription, double childTicketPrice, double adultTicketPrice) {
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
            Object[] params = {1};
            ResultSet result = DatabaseUtils.selectQueryById("*", "movie", "movie_status = ?", params);

            while (result.next()) {
                //Movie movie = new Movie(id, mvName, releaseDate, duration, lang, director, writter, starring, musicProvider, country, metaDescription, childTicketPrice, adultTicketPrice);
                Movie movie = new Movie();

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

                movies.add(movie);
            }

            result.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int viewMovieList(ArrayList<Movie> movies, Scanner sc) {
        boolean error = false;

        do {
            try {
                System.out.printf("\n%-5s %s\n", "No", "Movie Name");
                for (Movie movie : movies) {
                    System.out.printf("%-5d %s\n", movie.getMovieID(), movie.getMvName().getName());
                }

                System.out.print("\nEnter the movie no (0 - Back): ");
                int choice = sc.nextInt();
                sc.nextLine();

                if (choice < 0 || choice > movies.size()) {
                    System.out.println("Your choice is not among the available options! PLease try again.");
                    error = true;
                }
                else {
                    return choice;
                }
            }
            catch (InputMismatchException e) {
                System.out.println("Please enter a valid choice!");
                sc.nextLine();
                error = true;
            }
        } while (error);
        return 0;
    }

    // Show movie details
    public void movieDetail() {
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

    public void addMovie() throws Exception {
        int rowAffected = 0;

        try {
            String insertSql = "INSERT INTO `movie`(`genre_id`, `mv_name`, `release_date`, `duration`, `lang`, `director`, `writter`, `starring`, `music`, `country`,`meta_description`, `childTicket_Price`, `adultTicket_Price`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            Object[] params = {getGenreID(), getMvName().getName(), String.valueOf(getReleaseDate().getDate()), getDuration(), getLang(), getDirector(), getWritter(), getStarring(), getMusicProvider(), getCountry(), getMetaDescription(), getChildTicketPrice(), getAdultTicketPrice()};
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

    public void modifyMovie() throws Exception {
        int rowAffected = 0;

        try {
            String updateSql = "UPDATE `movie` SET `genre_id`= ?, `mv_name`= ?," +
                    "`release_date`= ?,`duration`= ?,`lang`= ?," +
                    "`director`= ?,`writter`= ?,`starring`= ?,`music`= ?," +
                    "`country`= ?,`meta_description`= ?, `childTicket_Price`= ?," +
                    "`adultTicket_Price`= ? WHERE movie_id = ?";
            Object[] params = {getGenreID(), getMvName().getName(), String.valueOf(getReleaseDate().getDate()), getDuration(), getLang(), getDirector(), getWritter(), getStarring(), getMusicProvider(), getCountry(), getMetaDescription(), getChildTicketPrice(), getAdultTicketPrice(), getMovieID()};
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
    public void deleteMovie() throws SQLException {
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

    // Method for add movie
    public static String getMultipleValues(Scanner sc, String propertyName, String propertyPluralName) throws Exception {
        StringBuilder result = new StringBuilder();
        String continues = "N";
        boolean error = true;

        do {
            System.out.print("\nEnter the movie " + propertyName + ": ");
            String value = sc.nextLine();

            String errorMessage = MovieValidator.checkValue(value, propertyName);

            if (errorMessage == null) {
                value = MovieUtils.capitalizeWords(value);
                error = false;

                do {
                    System.out.println("\nIs there another " + propertyPluralName + " for this movie? (Y / N)");
                    System.out.print("Answer: ");
                    String answer = sc.next();
                    sc.nextLine();

                    continues = MovieUtils.askForContinue(answer);

                } while (continues.equals("Invalid"));

                if (continues.equals("Y")) {
                    result.append(value).append(", ");
                } else {
                    result.append(value);
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
                Object[] params = {getGenreID()};
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

    public void setGenreID(int genreID){
        this.genreID = genreID;
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

    public int getMovieID() {
        return movieID;
    }

    public int getGenreID(){
        return genreID;
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

    public static ArrayList<Movie> getAllMovies() {
        return movies;
    }
}