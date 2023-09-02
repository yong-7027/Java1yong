package Driver;

import cinema_management.Cinema;
import timetable_management.AdminTimeTable;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Driver {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        //CustomerMovie.customer(sc);
        //AdminMovie.adminMovie(sc);
        //AdminGenre.adminGenre(sc);
        //AdminTimeTable.adminTimeTable(sc);

        System.out.println("Select the operation:");
        System.out.println("1. Manage Cinema");
        System.out.println("2. Manage Hall");
        System.out.print("\nEnter your selection: ");

        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1:
                break;
            case 2:
                ArrayList<Cinema> cinemas = Cinema.getCinemas();
                int cinemaSelected = 0;
                boolean error = true;

                do {
                    try {
                        System.out.println("\nSelect the cinema you want to manage it's hall: ");
                        for (int i = 0; i < cinemas.size(); i++) {
                            System.out.println((i + 1) + ". " + cinemas.get(i).getCinemaName());
                        }
                        System.out.print("\nEnter your selection: ");
                        cinemaSelected = sc.nextInt();
                        sc.nextLine();

                        if (cinemaSelected > 0 && cinemaSelected <= cinemas.size()) {
                            error = false;
                        }
                        else {
                            System.out.println("Your choice is not among the available options! PLease try again.");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Please enter a valid cinema no!");
                        sc.nextLine();
                    }

                    Cinema cinema = cinemas.get(cinemaSelected - 1);
                    SystemClass system = new SystemClass(cinema);
                    system.manageHall(sc);
                } while (error);
                break;
        }
    }
}