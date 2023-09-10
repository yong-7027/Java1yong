package genre_management;

import Connect.DatabaseUtils;
import Driver.Name;
import movie_management.MovieUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class AdminGenre extends Genre{
    // Variables
    private static boolean error;
    private static boolean continues;

    // Constructor
    private AdminGenre(){
    }

    private AdminGenre(int genreID, Name genreName, int post) {
        //super(genreID, genreName, post); // Call the constructor of the parent class
    }

    // Method
    public static void adminGenre(Scanner sc) throws Exception{
        ArrayList<AdminGenre> genres = new ArrayList<>();

        try {
            ResultSet result = DatabaseUtils.selectQueryById("*", "genre", null, null);

            while (result.next()) {
                AdminGenre genre = new AdminGenre(result.getInt("genre_id"), new Name(result.getString("genre_name")), result.getInt("post"));
                genres.add(genre);
            }

            result.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        String exit = "N";
        do {
            try {
                System.out.println("\nSelect the operation:");
                System.out.println("1. View Genre");
                System.out.println("2. Add Genre");
                System.out.println("3. Modify Genre");
                System.out.println("4. Delete Genre");
                System.out.print("\nEnter your selection (0 - Exit): ");

                int choice = sc.nextInt();
                sc.nextLine();

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
                        //Genre.viewGenreDetails(genres);
                        System.out.println("\nPress any key to back...");
                        sc.nextLine();
                        break;
                    case 2:
                        do {
                            AdminGenre newGenre = new AdminGenre();
                            newGenre.add();
                            String addGenre;
                            do {
                                System.out.println("\nDo you want add another new genre? (Y / N)");
                                System.out.print("Answer: ");
                                String answer = sc.next();
                                sc.nextLine();

                                addGenre = MovieUtils.askForContinue(answer);
                            } while (addGenre.equals("Invalid"));

                            if (addGenre.equals("Y")) {
                                continues = true;
                            } else {
                                continues = false;
                            }
                        } while (continues);
                        break;
                    case 3:
                        do {
                            try {
                                System.out.println("\nSelect the genre you want to modify: ");
                                //Genre.viewGenreDetails(genres);
                                System.out.print("\nEnter the genre no (0 - Back): ");
                                int choice1 = sc.nextInt();
                                sc.nextLine();

                                if (choice1 >= 0 && choice1 <= genres.size()) {
                                    if (choice1 != 0) {
                                        AdminGenre targetGenre = genres.get(choice1 - 1);
                                        //continues = targetGenre.modifyGenre(sc);
                                    } else {
                                        continues = false;
                                    }
                                } else {
                                    System.out.println("Your choice is not among the available options! PLease try again.");
                                    continues = true;
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Please enter a valid choice!");
                                sc.nextLine();
                                continues = true;
                            }
                        } while (continues);
                        break;
                    case 4:
                        // Delere genre
                        do {
                            try {
                                System.out.println("\nSelect the genre you want to delete: ");
                                //Genre.viewGenreDetails(genres);
                                System.out.print("\nEnter the genre no (0 - Back): ");
                                int choice2 = sc.nextInt();
                                sc.nextLine();

                                if (choice2 >= 0 && choice2 <= genres.size()) {
                                    if (choice2 != 0) {
                                        int post = genres.get(choice2 - 1).getPost();

                                        if (post == 0) {
                                            AdminGenre targetGenre = genres.get(choice2 - 1);
                                            //continues = targetGenre.deleteGenre();
                                        }
                                        else {
                                            System.out.println("Sorry, you cannot delete this genre. Please make sure there are no movie posts in this genre!");
                                            continues = true;
                                        }
                                    } else {
                                        continues = false;
                                    }
                                } else {
                                    System.out.println("Your choice is not among the available options! PLease try again.");
                                    continues = true;
                                }
                            }
                            catch (InputMismatchException e) {
                                System.out.println("Please enter a valid choice!");
                                sc.nextLine();
                                continues = true;
                            }
                        } while (continues);
                        break;
                    default:
                        System.out.println("Your choice is not among the available options! PLease try again.");
                        exit = "N";
                }
            }
            catch (InputMismatchException e) {
                System.out.println("Please enter a valid choice!");
                sc.nextLine();
                exit = "N";
            }
        } while (exit.equals("N"));
    }

    /*private void addGenre(Scanner sc) throws Exception{
        // Genre Name
        do {
            System.out.print("\nEnter genre name: ");
            String genreName = sc.nextLine();
            genreName = MovieUtils.capitalizeWords(genreName);

            String errorMessage = GenreValidator.checkGenreName(genreName);
            if (errorMessage == null) {
                setGenreName(genreName);
                error = false;
            } else {
                System.out.println(errorMessage + "\n");
                error = true;
            }
        } while (error);

        // sql statement
        String insertSql = "INSERT INTO `genre`(`genre_name`, `post`) VALUES (?, ?)";
        Object[] params = {getGenreName(), getPost()};
        int rowAffected = DatabaseUtils.insertQuery(insertSql, params);

        if (rowAffected > 0) {
            System.out.println("\nGenre successfully added...");
        }
        else {
            System.out.println("\nSomething went wrong!");
        }
    }*/

    /*private boolean modifyGenre(Scanner sc) throws Exception {
        do {
            System.out.print("\nEnter the new genre name (0 - Back): ");
            String editGenreName = sc.nextLine();

            if (editGenreName.equals("0")) {
                return true;
            } else {
                editGenreName = MovieUtils.capitalizeWords(editGenreName);

                String errorMessage = GenreValidator.checkEditGenreName(editGenreName, getGenreName());
                if (errorMessage == null) {
                    error = false;
                    String save;
                    do {
                        System.out.println("\nDo you want to save the changes? (Y / N)");
                        System.out.print("Answer: ");
                        String answer = sc.next();
                        sc.nextLine();

                        save = MovieUtils.askForContinue(answer);
                    } while (save.equals("Invalid"));

                    if (save.equals("Y")) {
                        String updateSql = "UPDATE `genre` SET `genre_name`= ? WHERE genre_id = ?";
                        Object[] params = {getGenreName(), getGenreID()};
                        int rowAffected = DatabaseUtils.updateQuery(updateSql, params);
                        if (rowAffected > 0) {
                            System.out.println("\nThe changes have been saved.");
                            setGenreName(editGenreName);
                        } else {
                            System.out.println("\nSomething went wrong...");
                        }
                    } else {
                        System.out.println("\nThe changes have not been saved.");
                    }
                } else {
                    System.out.println(errorMessage);
                    error = true;
                }
            }
        } while (error);

        String continueModify;
        do {
            System.out.println("\nDo you want to continue to modify another genre? (Y / N)");
            System.out.print("Answer: ");
            String answer2 = sc.next();
            sc.nextLine();

            continueModify = MovieUtils.askForContinue(answer2);
        } while (continueModify.equals("Invalid"));

        if (continueModify.equals("Y")) {
            return true;
        }
        else {
            return false;
        }
    }*/

    /*private boolean deleteGenre(Scanner sc) throws Exception{
        String delete;
        do {
            System.out.println("\nAre you sure you want to delete this genre? (Y / N)");
            System.out.print("Answer: ");
            String answer = sc.next();
            sc.nextLine();

            delete = MovieUtils.askForContinue(answer);
        } while (delete.equals("Invalid"));

        if (delete.equals("Y")) {
            Object[] params = {getGenreID()};
            int rowAffected = DatabaseUtils.deleteQueryById("genre", "genre_status", "genre_id", params);
            if (rowAffected > 0) {
                System.out.println("\nThis genre has been deleted.");
            }
            else {
                System.out.println("\nSomething went wrong...");
            }
        }
        else {
            System.out.println("\nThe genre is safe :)");
        }

        String continueDelete;
        do {
            System.out.println("\nDo you want to continue to delete another genre? (Y / N)");
            System.out.print("Answer: ");
            String answer2 = sc.next();
            sc.nextLine();

            continueDelete = MovieUtils.askForContinue(answer2);
        } while (continueDelete.equals("Invalid"));

        if (continueDelete.equals("Y")) {
            return true;
        }
        else {
            return false;
        }
    }*/
}