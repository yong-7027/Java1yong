/*package movie_management;

import Connect.DatabaseUtils;
import Driver.Name;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
                        int choice1 = Movie.viewMovieList(1, sc);
                        if (choice1 != 0) {
                            Movie viewMovie = movies.get(choice1 - 1);
                            viewMovie.viewMovieDetails();
                            continues = true;
                        } else {
                            continues = false;
                        }
                    } while (continues);
                    break;
                case 2:
                    do {
                        AdminMovie newMovie = new AdminMovie();
                        newMovie.add();
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
                        int choice2 = Movie.viewMovieList(1, sc);
                        if (choice2 != 0) {
                            AdminMovie targetMovie = (AdminMovie) movies.get(choice2 - 1);
                            targetMovie.modify();
                            continues = true;
                        } else {
                            continues = false;
                        }
                    } while (continues);
                    break;
                case 4:
                    do {
                        System.out.println("\nSelect the movie you want to delete: ");
                        int choice3 = Movie.viewMovieList(1, sc);
                        if (choice3 != 0) {
                            AdminMovie deleteMovie = (AdminMovie) movies.get(choice3 - 1);
                            deleteMovie.delete();
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
                // value = MovieUtils.capitalizeWords(value);
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
}*/