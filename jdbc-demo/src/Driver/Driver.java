package Driver;

import cinema_management.Cinema;
import seat_management.Seat;
import hall_management.Hall;
import timetable_management.AdminTimeTable;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Driver {
    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);
        AdminTimeTable.adminTimeTable(sc);
        //CustomerMovie.customer(sc);
        //AdminMovie.adminMovie(sc);
        //AdminGenre.adminGenre(sc);
        //AdminTimeTable.adminTimeTable(sc);

        System.out.println("Select the operation:");
        System.out.println("1. Manage Cinema");
        System.out.println("2. Manage Hall");
        System.out.println("3. Manage Movie");
        System.out.println("4. Manage Genre");
        System.out.println("5. Manage Schedule");
        System.out.print("\nEnter your selection: ");

        int choice = sc.nextInt();
        sc.nextLine();

        SystemClass system = null;
        switch (choice) {
            case 1:
                system = new SystemClass();
                system.manageCinema(sc);
                break;
            case 2:
                ArrayList<Cinema> cinemas = Cinema.getCinemas();
                int cinemaSelected = 0;
                boolean error = true;

                do {
                    try {
                        System.out.println("\nSelect the cinema you want to manage it's hall: ");
                        for (int i = 0; i < cinemas.size(); i++) {
                            System.out.println((i + 1) + ". " + cinemas.get(i).getCinemaName().getName());
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
                } while (error);

                Cinema cinema = cinemas.get(cinemaSelected - 1);
                system = new SystemClass(cinema);
                system.manageHall(sc);
                break;
            case 3:
                system = new SystemClass();
                system.manageMovie(sc);
                break;
            case 4:
                system = new SystemClass();
                system.manageGenre(sc);
                break;
            case 5:
                system = new SystemClass();
                system.manageSchedule(sc);
        }
    }
}