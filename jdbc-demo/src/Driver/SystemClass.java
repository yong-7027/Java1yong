package Driver;

import Connect.DatabaseUtils;
import cinema_management.Cinema;
import hall_management.Hall;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class SystemClass {
    private Cinema cinema;

    public SystemClass(){
    }

    public SystemClass(Cinema cinema) {
        this.cinema = cinema;
    }

    // Cinema cinema = new Cinema();
    // cinema.manageHall();
    public void manageHall(Scanner sc) throws Exception {
        int choice = displayMenu("Hall", sc);
        boolean error = true;

        switch (choice){
            case 1:
                break;
            case 2:
                Name name = null;
                do {
                    System.out.print("\nEnter the hall name: ");
                    String hallName = sc.nextLine();

                    name = new Name(hallName);
                    name.capitalizeWords();

                    ResultSet result = null;
                    try {
                        Object[] params = {cinema.getCinemaID()};
                        result = DatabaseUtils.selectQueryById("hall_name", "hall", "cinema_id = ?", params);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    String errorMsg = name.checkName("hall", result, "hall_name");

                    if (errorMsg == null) {
                        error = false;
                    } else {
                        System.out.println(errorMsg);
                        error = true;
                    }
                } while (error);

                String hallType = null;
                do {
                    System.out.println("\nSelect the hall type: ");
                    System.out.println("1. Standard Hall");
                    System.out.println("2. 3D Hall");
                    System.out.print("\nEnter your selection: ");
                    int hallTypeSelection = sc.nextInt();

                    if (hallTypeSelection == 1) {
                        hallType = "STANDARD";
                        error = false;
                    } else if (hallTypeSelection == 2) {
                        hallType = "3D";
                        error = false;
                    }
                    else {
                        error = true;
                    }
                } while (error);

                Hall hall = new Hall(name, hallType);

                cinema.addHall(hall);
                break;
            case 3:
                break;
            case 4:
                break;
        }
    }

    private static int displayMenu(String propertyName, Scanner sc){
        boolean error = true;
        int choice = 0;

        do {
            try {
                System.out.println("\nSelect the operation:");
                System.out.println("1. View " + propertyName);
                System.out.println("2. Add " + propertyName);
                System.out.println("3. Modify " + propertyName);
                System.out.println("4. Delete " + propertyName);
                System.out.print("\nEnter your selection: ");
                choice = sc.nextInt();
                sc.nextLine();

                if (choice > 0 || choice <= 4) {
                    error = false;
                }
                else {
                    System.out.println("Your choice is not among the available options! PLease try again.");
                }
            }
            catch (InputMismatchException e) {
                System.out.println("Please enter a valid choice!");
                sc.nextLine();
            }
        } while (error);

        return choice;
    }

    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
    }

    public Cinema getCinema() {
        return cinema;
    }
}
