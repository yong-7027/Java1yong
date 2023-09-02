package movie_management;

import Connect.DatabaseUtils;
import Driver.Name;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

public class AdminMovie extends Movie {
    private AdminMovie(){
    }

    public static void adminMovie(Scanner sc) throws Exception {
        boolean error;
        boolean continues;

        ArrayList<Movie> movies = Movie.getAllMovies();

        String exit = "N";
        int choice = 0;
        do {
            try {
                System.out.println("\nSelect the operation:");
                System.out.println("1. View Movie");
                System.out.println("2. Add Movie");
                System.out.println("3. Modify Movie");
                System.out.println("4. Delete Movie");
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
                    do {
                        int choice1 = Movie.viewMovieList(movies, sc);
                        if (choice1 != 0) {
                            AdminMovie viewMovie = (AdminMovie) movies.get(choice1 - 1);
                            viewMovie.movieDetail();
                            continues = true;
                        } else {
                            continues = false;
                        }
                    } while (continues);
                    break;
                case 2:
                    do {
                        AdminMovie newMovie = new AdminMovie();
                        newMovie.addMovie(sc);
                        String addMovie;
                        do {
                            System.out.println("\nDo you want add another new movie? (Y / N)");
                            System.out.print("Answer: ");
                            String answer = sc.next();
                            sc.nextLine();

                            addMovie = MovieUtils.askForContinue(answer);
                        } while (addMovie.equals("Invalid"));

                        if (addMovie.equals("Y")) {
                            continues = true;
                        } else {
                            continues = false;
                        }
                    } while (continues);
                    break;
                case 3:
                    do {
                        System.out.println("\nSelect the movie you want to modify: ");
                        int choice2 = Movie.viewMovieList(movies, sc);
                        if (choice2 != 0) {
                            AdminMovie targetMovie = (AdminMovie) movies.get(choice2 - 1);
                            targetMovie.modifyMovie(sc);
                            continues = true;
                        } else {
                            continues = false;
                        }
                    } while (continues);
                    break;
                case 4:
                    do {
                        System.out.println("\nSelect the movie you want to delete: ");
                        int choice3 = Movie.viewMovieList(movies, sc);
                        if (choice3 != 0) {
                            AdminMovie deleteMovie = (AdminMovie) movies.get(choice3 - 1);
                            deleteMovie.deleteMovie(sc);
                            continues = true;
                        } else {
                            continues = false;
                        }
                    } while (continues);
                    break;
                default:
                    System.out.println("Your choice is not among the available options! PLease try again.");
                    exit = "N";
            }
        } while (exit.equals("N"));
    }

    private void addMovie(Scanner sc) throws Exception {
        boolean error;

        // Movie Name
        Name name = null;
        do {
            System.out.print("\nEnter movie name: ");
            String mvName = sc.nextLine();

            name = new Name(mvName);
            name.capitalizeWords();

            ResultSet result = null;
            try {
                result = DatabaseUtils.selectQueryById("mv_name", "movie", null, null);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            String errorMessage = name.checkName("movie", result, "mv_name");

            if (errorMessage == null) {
                setMvName(name);
                error = false;
            } else {
                System.out.println(errorMessage + "\n");
                error = true;
            }
        } while (error);

        // Movie Genre ID
        do {
            try {
                ResultSet result = DatabaseUtils.selectQueryById("genre_id, genre_name", "genre", null, null);

                try {
                    int i = 1;
                    ArrayList<Integer> genreID = new ArrayList<>();

                    System.out.println("\nAvailable Genres");
                    while (result.next()) {
                        System.out.println(i + ". " + result.getString("genre_name"));
                        genreID.add(result.getInt("genre_id"));  // Store the genre ID
                        i++;
                    }
                    System.out.print("\nEnter your selection: ");
                    int choice = sc.nextInt();
                    sc.nextLine();

                    String errorMessage = MovieValidator.checkGenreID(genreID.size(), choice);

                    if (errorMessage == null) {
                        setGenreID(genreID.get(choice - 1));  // ArrayList starts from index 0
                        error = false;
                    }
                    else {
                        System.out.println(errorMessage);
                        error = true;
                    }
                }
                catch (Exception e) {
                    System.out.println("Please enter a valid choice!");
                    sc.nextLine();
                    error = true;
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        } while (error);

        // Movie Release Date
        do {
            System.out.print("\nEnter movie release date (YYYY-MM-DD): ");
            String releaseDate = sc.nextLine();

            if (releaseDate.trim().isEmpty()) {
                System.out.println("Please enter the release date.");
                error = true;
            } else {
                try {
                    String[] parts = releaseDate.split("-");
                    int year = Integer.parseInt(parts[0]);  // Java's built-in method for converting strings to integers (int type)
                    int month = Integer.parseInt(parts[1]);
                    int day = Integer.parseInt(parts[2]);

                    // 验证日期是否 valid
                    ShowDate date = new ShowDate(year, month, day);
                    boolean validDate = date.isValidDate();

                    if (validDate == true) {
                        String errorMessage = date.checkLocalDate();

                        if (errorMessage == null) {
                            setReleaseDate(date);
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
                    error = true;
                }
            }
        } while (error);

        // Movie Duration
        do {
            try {
                System.out.print("\nEnter movie duration (in minutes): ");
                int duration = sc.nextInt();
                sc.nextLine();  // Consume the newline left by nextInt()

                String errorMessage = MovieValidator.checkDuration(duration);

                if (errorMessage == null) {
                    setDuration(duration);
                    error = false;
                } else {
                    System.out.println(errorMessage);
                    error = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid duration!");
                sc.nextLine(); // Consume the erroneous input
                error = true;
            }
        } while (error);

        // Movie Language
        String[] languages = {"English", "Chinese", "Japanese", "Korean", "German", "Italian", "Spanish", "Cantonese", "French", "Russian", "Arabic", "Hindi", "Tamil"};

        String mvLanguage = getMultipleChosens(sc, languages, "languages");
        setLang(mvLanguage);

        // Movie Director
        String mvDirector = getMultipleValues(sc, "director", "directors");
        setDirector(mvDirector);

        // Movie Writter
        String mvWritter = getMultipleValues(sc, "writter", "writters");
        setWritter(mvWritter);

        // Movie Starring
        String mvStarring = getMultipleValues(sc, "starring", "starrings");
        setStarring(mvStarring);

        // Movie Music Provider
        String mvMusicProvider = getMultipleValues(sc, "music provider", "music prodivers");
        setMusicProvider(mvMusicProvider);

        // Movie Country
        String[] countries = {"United States", "United Kingdom", "Canada", "China", "Taiwan", "Malaysia", "Singapore", "Japan", "North Korea", "Italy", "Hong Kong", "France", "Russia", "India"};

        String mvCountry = getMultipleChosens(sc, countries, "countries");
        setCountry(mvCountry);

        // Movie Meta Description
        do {
            System.out.print("\nEnter movie meta description: ");
            String mvDescription = sc.nextLine();

            String errorMessage = MovieValidator.checkMetaDescription(mvDescription);

            if (errorMessage == null) {
                setMetaDescription(mvDescription);
                error = false;
            }
            else {
                System.out.println(errorMessage);
                error = true;
            }
        } while (error);

        // Movie Child Ticket Price
        double mvChildTicketPrice = getTicketPrice(sc, "child");
        setChildTicketPrice(mvChildTicketPrice);

        // Movie Adult Ticket Price
        double mvAdultTicketPrice = getTicketPrice(sc, "adult");
        setAdultTicketPrice(mvAdultTicketPrice);

        // sql statement
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

    private void modifyMovie(Scanner sc) throws Exception {
        boolean error = true;
        boolean stop = true;

        do {
            int serialNum = modifyMovieDetail(sc);
            switch (serialNum) {
                case 0:
                    String save;
                    do {
                        System.out.println("\nDo you want to save the changes? (Y / N)");
                        System.out.print("Answer: ");
                        String answer = sc.next();
                        sc.nextLine();

                        save = MovieUtils.askForContinue(answer);
                    } while (save.equals("Invalid"));

                    stop = false;

                    if (save.equals("Y")) {
                        String updateSql = "UPDATE `movie` SET `genre_id`= ?, `mv_name`= ?," +
                                           "`release_date`= ?,`duration`= ?,`lang`= ?," +
                                           "`director`= ?,`writter`= ?,`starring`= ?,`music`= ?," +
                                           "`country`= ?,`meta_description`= ?, `childTicket_Price`= ?," +
                                           "`adultTicket_Price`= ? WHERE movie_id = ?";
                        Object[] params = {getGenreID(), getMvName().getName(), String.valueOf(getReleaseDate().getDate()), getDuration(), getLang(), getDirector(), getWritter(), getStarring(), getMusicProvider(), getCountry(), getMetaDescription(), getChildTicketPrice(), getAdultTicketPrice(), getMovieID()};
                        int rowAffected = DatabaseUtils.updateQuery(updateSql, params);
                        if (rowAffected > 0) {
                            System.out.println("\nThe changes have been saved.");
                        }
                        else {
                            System.out.println("\nSomething went wrong...");
                        }
                    }
                    else {
                        System.out.println("\nThe changes have not been saved.");
                    }
                    break;
                case 1:
                    // Movie Name
                    Name name = null;
                    do {
                        System.out.print("\nEnter the new movie name: ");
                        String newMvName = sc.nextLine();

                        name = new Name(newMvName);
                        name.capitalizeWords();

                        ResultSet result = null;
                        try {
                            result = DatabaseUtils.selectQueryById("mv_name", "movie", null, null);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }

                        String errorMessage = name.checkEditName("movie", result, "mv_name", getMvName().getName());

                        if (errorMessage == null) {
                            setMvName(name);
                            error = false;
                        } else {
                            System.out.println(errorMessage);
                            error = true;
                        }
                    } while (error);
                    break;
                case 2:
                    // Movie Genre ID
                    do {
                        try {
                            ResultSet result = DatabaseUtils.selectQueryById("genre_id, genre_name", "genre", null, null);

                            try {
                                int i = 1;
                                ArrayList<Integer> genreID = new ArrayList<>();

                                System.out.println("\nAvailable Genres");
                                while (result.next()) {
                                    System.out.println(i + ". " + result.getString("genre_name"));
                                    genreID.add(result.getInt("genre_id"));  // Store the genre ID
                                    i++;
                                }
                                System.out.print("\nEnter your selection: ");
                                int newGenre = sc.nextInt();
                                sc.nextLine();

                                String errorMessage = MovieValidator.checkGenreID(genreID.size(), newGenre);

                                if (errorMessage == null) {
                                    setGenreID(genreID.get(newGenre - 1));  // ArrayList starts from index 0
                                    error = false;
                                } else {
                                    System.out.println(errorMessage);
                                    error = true;
                                }
                            } catch (Exception e) {
                                System.out.println("Please enter a valid choice!");
                                sc.nextLine();
                                error = true;
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } while (error);
                    break;
                case 3:
                    // Movie Release Date
                    do {
                        System.out.print("\nEnter movie release date (YYYY-MM-DD) (X - Back): ");
                        String editReleaseDate = sc.nextLine();

                        if (editReleaseDate.trim().isEmpty()) {
                            System.out.println("Please enter the release date.");
                            error = true;
                        } else if (editReleaseDate.equals("x") || editReleaseDate.equals("X")) {
                            break;
                        } else {
                            try {
                                String[] editParts = editReleaseDate.split("-");
                                int editYear = Integer.parseInt(editParts[0]);  // Java's built-in method for converting strings to integers (int type)
                                int editMonth = Integer.parseInt(editParts[1]);
                                int editDay = Integer.parseInt(editParts[2]);

                                // 验证日期是否 valid
                                ShowDate editDate = new ShowDate(editYear, editMonth, editDay);
                                boolean editValidDate = editDate.isValidDate();

                                if (editValidDate == true) {
                                    //editDate.setMonth(editMonth - 1);  // Months are 0-based in calendar
                                    //editDate.parseDate();

                                    String errorMessage = editDate.checkLocalDate();

                                    if (errorMessage == null) {
                                        setReleaseDate(editDate);
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
                                error = true;
                            }
                        }
                    } while (error);
                    break;
                case 4:
                    // Movie Duration
                    do {
                        try {
                            System.out.print("\nEnter the new movie duration (in minutes): ");
                            int editDuration = sc.nextInt();
                            sc.nextLine();  // Consume the newline left by nextInt()

                            String errorMessage = MovieValidator.checkDuration(editDuration);

                            if (errorMessage == null) {
                                setDuration(editDuration);
                                error = false;
                            } else {
                                System.out.println(errorMessage);
                                error = true;
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Please enter a valid duration!");
                            sc.nextLine(); // Consume the erroneous input
                            error = true;
                        }
                    } while (error);
                    break;
                case 5:
                    // Movie Language
                    String[] languages = {"English", "Chinese", "Japanese", "Korean", "German", "Italian", "Spanish", "Cantonese", "French", "Russian", "Arabic", "Hindi", "Tamil"};

                    String editMvLanguage = getMultipleChosens(sc, languages, "languages");
                    setLang(editMvLanguage);
                    break;
                case 6:
                    // Movie Director
                    String editMvDirector = getMultipleValues(sc, "director", "directors");
                    setDirector(editMvDirector);
                    break;
                case 7:
                    // Movie Writter
                    String editMvWritter = getMultipleValues(sc, "writter", "writters");
                    setWritter(editMvWritter);
                    break;
                case 8:
                    // Movie Starring
                    String editMvStarring = getMultipleValues(sc, "starring", "starrings");
                    setStarring(editMvStarring);
                    break;
                case 9:
                    // Movie Music Provider
                    String editMvMusicProvider = getMultipleValues(sc, "music provider", "music prodivers");
                    setMusicProvider(editMvMusicProvider);
                    break;
                case 10:
                    // Movie Country
                    String[] countries = {"United States", "United Kingdom", "Canada", "China", "Taiwan", "Malaysia", "Singapore", "Japan", "North Korea", "Italy", "Hong Kong", "France", "Russia", "India"};

                    String editMvCountry = getMultipleChosens(sc, countries, "countries");
                    setCountry(editMvCountry);
                    break;
                case 11:
                    // Movie Meta Description
                    do {
                        System.out.print("\nEnter the new movie meta description: ");
                        String editMvDescription = sc.nextLine();

                        String errorMessage = MovieValidator.checkMetaDescription(editMvDescription);

                        if (errorMessage == null) {
                            setMetaDescription(editMvDescription);
                            error = false;
                        }
                        else {
                            System.out.println(errorMessage);
                            error = true;
                        }
                    } while (error);
                    break;
                case 12:
                    // Movie Child Ticket Price
                    double editMvChildTicketPrice = getTicketPrice(sc, "child");
                    setChildTicketPrice(editMvChildTicketPrice);
                    break;
                case 13:
                    // Movie Adult Ticket Price
                    double editMvAdultTicketPrice = getTicketPrice(sc, "adult");
                    setAdultTicketPrice(editMvAdultTicketPrice);
                    break;
            }
        } while (stop);
    }

    // Delete Movie
    private void deleteMovie(Scanner sc) throws SQLException {
        movieDetail();
        String delete;
        do {
            System.out.println("\nDo you want to delete this movie? (Y / N)");
            System.out.print("Answer: ");
            String answer = sc.next();
            sc.nextLine();

            delete = MovieUtils.askForContinue(answer);
        } while (delete.equals("Invalid"));

        if (delete.equals("Y")) {
            Object[] params = {getMovieID()};
            int rowAffected = DatabaseUtils.deleteQueryById("movie", "movie_id", params);

            if (rowAffected > 0) {
                System.out.println("\nThe movie has been deleted.");
            }
            else {
                System.out.println("\nSomething went wrong...");
            }
        }
        else {
            System.out.println("\nThe movie is saved.");
        }
    }

    // Method for add movie
    private static String getMultipleValues(Scanner sc, String propertyName, String propertyPluralName) throws Exception {
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

    private static String getMultipleChosens(Scanner sc, String[] array, String propertyName){
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

    private static double getTicketPrice(Scanner sc, String propertyName){
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
    private int modifyMovieDetail(Scanner sc) throws SQLException {
        boolean error = true;

        do {
            try {
                Object[] params = {getGenreID()};
                ResultSet result = DatabaseUtils.selectQueryById("genre_name", "genre", "genre_id = ?", params);

                try {
                    int count = 1;
                    System.out.printf("\nMovie Detail:\n");
                    System.out.println(count + ". Movie Name: " + getMvName());
                    count++;
                    if (result.next()) {
                        System.out.println(count + ". Genre: " + result.getString("genre_name"));
                        count++;
                    }
                    System.out.println(count + ". Release Date: " + getReleaseDate());
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
}