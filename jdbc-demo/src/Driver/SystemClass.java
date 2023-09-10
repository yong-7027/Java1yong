package Driver;

import Connect.DatabaseUtils;
import cinema_management.Address;
import cinema_management.Cinema;
import genre_management.Genre;
import hall_management.Hall;
import movie_management.Movie;
import movie_management.MovieUtils;
import movie_management.MovieValidator;
import movie_management.ShowDate;
import timetable_management.TimeTable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class SystemClass {
    private Cinema cinema;
    private Movie movie;
    private Genre genre;

    public SystemClass(){
    }

    public SystemClass(Cinema cinema) {
        this.cinema = cinema;
    }

    public SystemClass(Movie movie) {
        this.movie = movie;
    }

    public void customer(Scanner sc) throws Exception {
        int choice = 0;
        boolean error = true;

        do {
            try {
                System.out.println("\nSelect the operation: ");
                System.out.println("1. View Profile");
                System.out.println("2. View Movie");
                System.out.println("3. Search Movie");
                System.out.println("4. Log out");

                choice = sc.nextInt();
                sc.nextLine();

                if (choice > 0 && choice <= 4) {
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

        switch (choice) {
            case 1:
                break;
            case 2:
                int periodSelected = 0;
                error = true;

                do {
                    try {
                        System.out.println("\nSelect the time period: ");
                        System.out.println("1. Opening This Week");
                        System.out.println("2. Coming Soon");
                        periodSelected = sc.nextInt();
                        sc.nextLine();

                        if (periodSelected > 0 && periodSelected <= 2) {
                            error = false;
                        }
                    }
                    catch (InputMismatchException e) {
                        System.out.println("Please enter a valid choice!");
                        sc.nextLine();
                    }
                } while (error);

                switch (periodSelected) {
                    case 1:
                        int movieSelected = 0;
                        LocalDate currentDate = LocalDate.now();
                        LocalDate oneWeekAgo = currentDate.minusWeeks(1);

                        System.out.println("Opening This Week");
                        ArrayList<Movie> moviesAfterFiltered = Movie.showMovieListAfterFiltered(oneWeekAgo, currentDate, 1);

                        do {
                            try {
                                System.out.print("\nEnter the movie no (0 - Back): ");
                                movieSelected = sc.nextInt();
                                sc.nextLine();

                                if (movieSelected == 0 || (movieSelected > 0 && movieSelected <= moviesAfterFiltered.size() && moviesAfterFiltered.get(movieSelected - 1).getStutus() == 1)) {
                                    error = false;
                                } else {
                                    System.out.println("Your choice is not among the available options! PLease try again.");
                                    error = true;
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Please enter a valid choice!");
                                sc.nextLine();
                                error = true;
                            }
                        } while (error);

                        if (movieSelected != 0) {
                            Movie viewMovie = moviesAfterFiltered.get(movieSelected - 1);
                            viewMovie.viewMovieDetails();
                        }
                        break;
                    case 2:
                        break;
                }
                break;
            case 3:
                break;
            case 4:
                break;
        }

    }

    public void manageCinema(Scanner sc) throws Exception {
        boolean back = false;

        do {
            int choice = displayMenu("Cinema", sc);
            boolean error = true;
            boolean continues = true;

            switch (choice) {
                case 0:
                    back = true;
                    break;
                case 1:
                    // View Cinema
                    do {
                        int cinemaNo = 0;
                        error = true;
                        ArrayList<Cinema> cinemas = new ArrayList<>();
                        do {
                            try {
                                System.out.println("\nSelect the cinema: ");
                                cinemas = Cinema.viewCinemaList(1);
                                System.out.print("\nEnter the cinema no (0 - Back): ");
                                cinemaNo = sc.nextInt();
                                sc.nextLine();

                                if (cinemaNo == 0 || (cinemaNo > 0 && cinemaNo <= cinemas.size() && cinemas.get(cinemaNo - 1).getStatus() == 1)) {
                                    error = false;
                                } else {
                                    System.out.println("Your choice is not among the available options! PLease try again.");
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Please enter a valid hall no!");
                                sc.nextLine();
                            }
                        } while (error);

                        if (cinemaNo != 0) {
                            cinemas.get(cinemaNo - 1).viewCinemaDetails();

                            String continueViewCinema;
                            do {
                                System.out.println("\nDo you want view another cinema? (Y / N)");
                                System.out.print("Answer: ");
                                String answer = sc.next();
                                sc.nextLine();

                                continueViewCinema = MovieUtils.askForContinue(answer);
                            } while (continueViewCinema.equals("Invalid"));

                            if (continueViewCinema.equals("Y")) {
                                continues = true;
                            } else {
                                continues = false;
                                back = false;
                            }
                        } else {
                            continues = false;
                            back = false;
                        }
                    } while (continues);
                    break;
                case 2:
                    // Add Cinema
                    do {
                        Cinema newCinema = new Cinema();
                        // Cinema Name
                        Name name = null;
                        do {
                            System.out.print("\nEnter cinema name (0 - Back): ");
                            String cinemaName = sc.nextLine();

                            name = new Name(cinemaName);
                            name.capitalizeWords();

                            ResultSet result = null;
                            try {
                                result = DatabaseUtils.selectQueryById("cinema_name", "cinema", null, null);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }

                            String errorMessage = name.checkName("cinema", result, "cinema_name");

                            if (errorMessage == null) {
                                newCinema.setCinemaName(name);
                                error = false;
                            } else {
                                System.out.println(errorMessage);
                                error = true;
                            }
                        } while (error);

                        boolean duplicatedAddress = false;
                        do {
                            // Cinema Address
                            int stateSelected = 0;
                            do {
                                try {
                                    System.out.println("\nSelect the state: ");
                                    Address.viewStateList();
                                    System.out.print("\nEnter your selection: ");
                                    stateSelected = sc.nextInt();
                                    sc.nextLine();

                                    if (stateSelected > 0 && stateSelected <= Address.getStateToCities().size()) {
                                        error = false;
                                    } else {
                                        System.out.println("Your choice is not among the available options! PLease try again.");
                                        error = true;
                                    }
                                } catch (InputMismatchException e) {
                                    System.out.println("Please enter a valid state no!");
                                    sc.nextLine();
                                }
                            } while (error);

                            String stateName = Address.getStateName(stateSelected - 1);

                            int citySelected = 0;
                            do {
                                try {
                                    System.out.println("\nSelect the city: ");
                                    int count = Address.viewCityList(stateSelected - 1);
                                    System.out.print("\nEnter your selection: ");
                                    citySelected = sc.nextInt();
                                    sc.nextLine();

                                    if (citySelected > 0 && citySelected <= count) {
                                        error = false;
                                    } else {
                                        System.out.println("Your choice is not among the available options! PLease try again.");
                                        error = true;
                                    }
                                } catch (InputMismatchException e) {
                                    System.out.println("Please enter a valid city no!");
                                    sc.nextLine();
                                }
                            } while (error);

                            String cityName = Address.getCityName(stateName, citySelected - 1);

                            int postcodeSelected = 0;
                            do {
                                try {
                                    System.out.println("\nSelect the city: ");
                                    int count = Address.viewPostcodeList(cityName);
                                    System.out.print("\nEnter your selection: ");
                                    postcodeSelected = sc.nextInt();
                                    sc.nextLine();

                                    if (postcodeSelected > 0 && postcodeSelected <= count) {
                                        error = false;
                                    } else {
                                        System.out.println("Your choice is not among the available options! PLease try again.");
                                        error = true;
                                    }
                                } catch (InputMismatchException e) {
                                    System.out.println("Please enter a valid postcode no!");
                                    sc.nextLine();
                                }
                            } while (error);

                            String postcode = Address.getPostcodeSelected(cityName, postcodeSelected - 1);

                            String streetName;
                            do {
                                System.out.print("\nEnter the street name: ");
                                streetName = sc.nextLine();

                                if (streetName.trim().isEmpty()) {
                                    System.out.println("Please enter the street name.");
                                    error = true;
                                } else {
                                    streetName = streetName.toUpperCase();
                                    error = false;
                                }
                            } while (error);

                            Address cinemaAddress = new Address(streetName.trim(), postcode, cityName, stateName);
                            newCinema.setCinemaAddress(cinemaAddress);

                            ResultSet result = null;
                            try {
                                result = DatabaseUtils.selectQueryById("cinema_address", "cinema", null, null);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }

                            duplicatedAddress = cinemaAddress.checkAddressDuplicate(result, "cinema_address");

                            if (duplicatedAddress == true) {
                                System.out.println("Same cinema address detected.");
                            }
                        } while (duplicatedAddress);

                        // Cinema Phone
                        do {
                            System.out.print("\nEnter the cinema phone number: ");
                            String phoneNumber = sc.nextLine();

                            if (phoneNumber.trim().isEmpty()) {
                                System.out.println("Please enter the phone number.");
                                error = true;
                            } else {
                                newCinema.setCinemaPhone(phoneNumber.trim());

                                if (newCinema.isValidOfficePhoneNumber()) {
                                    error = false;
                                } else {
                                    System.out.println("The phone number is invalid.");
                                    error = true;
                                }
                            }
                        } while (error);

                        // Add Cinema
                        newCinema.add();

                        String continueAddCinema;
                        do {
                            System.out.println("\nDo you want add another new cinema? (Y / N)");
                            System.out.print("Answer: ");
                            String answer = sc.next();
                            sc.nextLine();

                            continueAddCinema = MovieUtils.askForContinue(answer);
                        } while (continueAddCinema.equals("Invalid"));

                        if (continueAddCinema.equals("Y")) {
                            continues = true;
                        } else {
                            continues = false;
                            back = false;
                        }
                    } while (continues);
                    break;
                case 3:
                    // Modify Cinema
                    do {
                        error = true;
                        ArrayList<Cinema> cinemasModified = new ArrayList<>();
                        int cinemaModified = 0;

                        do {
                            try {
                                System.out.println("\nSelect the cinema you want to modify: ");
                                cinemasModified = Cinema.viewCinemaList(1);
                                System.out.print("\nEnter the cinema no (0 - Back): ");
                                cinemaModified = sc.nextInt();
                                sc.nextLine();

                                if (cinemaModified == 0 || (cinemaModified > 0 && cinemaModified <= cinemasModified.size() && cinemasModified.get(cinemaModified - 1).getStatus() == 1)) {
                                    error = false;
                                } else {
                                    error = true;
                                    System.out.println("Your choice is not among the available options! PLease try again.");
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Please enter a valid cinema no!");
                                sc.nextLine();
                            }
                        } while (error);

                        if (cinemaModified != 0) {
                            Cinema orgCinema = cinemasModified.get(cinemaModified - 1);
                            cinema = new Cinema(orgCinema.getCinemaID(), orgCinema.getCinemaName(), orgCinema.getCinemaAddress(), orgCinema.getCinemaPhone());
                            boolean stop = false;

                            do {
                                int serialNum = cinema.modifyCinemaDetails(sc);
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

                                        stop = true;

                                        if (save.equals("Y")) {
                                            cinema.modify();
                                        } else {
                                            setCinema(orgCinema);
                                            System.out.println("\nThe changes have not been saved.");
                                        }

                                        String continueModifyCinema;
                                        do {
                                            System.out.println("\nDo you want modify another cinema? (Y / N)");
                                            System.out.print("Answer: ");
                                            String answer = sc.next();
                                            sc.nextLine();

                                            continueModifyCinema = MovieUtils.askForContinue(answer);
                                        } while (continueModifyCinema.equals("Invalid"));

                                        if (continueModifyCinema.equals("Y")) {
                                            continues = true;
                                        } else {
                                            continues = false;
                                            back = false;
                                        }
                                        break;
                                    case 1:
                                        // Cinema Name
                                        Name name = null;
                                        do {
                                            System.out.print("\nEnter the new cinema name: ");
                                            String newCinemaName = sc.nextLine();

                                            name = new Name(newCinemaName);
                                            name.capitalizeWords();

                                            ResultSet result = null;
                                            try {
                                                result = DatabaseUtils.selectQueryById("cinema_name", "cinema", null, null);
                                            } catch (SQLException e) {
                                                throw new RuntimeException(e);
                                            }

                                            String errorMessage = name.checkEditName("cinema", result, "cinema_name", cinema.getCinemaName().getName());

                                            if (errorMessage == null) {
                                                cinema.setCinemaName(name);
                                                error = false;
                                            } else {
                                                System.out.println(errorMessage);
                                                error = true;
                                            }
                                        } while (error);
                                        break;
                                    case 2:
                                        // Cinema Address
                                        boolean duplicatedAddress;
                                        do {
                                            int stateSelected = 0;
                                            do {
                                                try {
                                                    System.out.println("\nSelect the state: ");
                                                    Address.viewStateList();
                                                    System.out.print("\nEnter your selection: ");
                                                    stateSelected = sc.nextInt();
                                                    sc.nextLine();

                                                    if (stateSelected > 0 && stateSelected <= Address.getStateToCities().size()) {
                                                        error = false;
                                                    } else {
                                                        System.out.println("Your choice is not among the available options! PLease try again.");
                                                        error = true;
                                                    }
                                                } catch (InputMismatchException e) {
                                                    System.out.println("Please enter a valid state no!");
                                                    sc.nextLine();
                                                }
                                            } while (error);

                                            String stateName = Address.getStateName(stateSelected - 1);

                                            int citySelected = 0;
                                            do {
                                                try {
                                                    System.out.println("\nSelect the city: ");
                                                    int count = Address.viewCityList(stateSelected - 1);
                                                    System.out.print("\nEnter your selection: ");
                                                    citySelected = sc.nextInt();
                                                    sc.nextLine();

                                                    if (citySelected > 0 && citySelected <= count) {
                                                        error = false;
                                                    } else {
                                                        System.out.println("Your choice is not among the available options! PLease try again.");
                                                        error = true;
                                                    }
                                                } catch (InputMismatchException e) {
                                                    System.out.println("Please enter a valid city no!");
                                                    sc.nextLine();
                                                }
                                            } while (error);

                                            String cityName = Address.getCityName(stateName, citySelected - 1);

                                            int postcodeSelected = 0;
                                            do {
                                                try {
                                                    System.out.println("\nSelect the city: ");
                                                    int count = Address.viewPostcodeList(cityName);
                                                    System.out.print("\nEnter your selection: ");
                                                    postcodeSelected = sc.nextInt();
                                                    sc.nextLine();

                                                    if (postcodeSelected > 0 && postcodeSelected <= count) {
                                                        error = false;
                                                    } else {
                                                        System.out.println("Your choice is not among the available options! PLease try again.");
                                                        error = true;
                                                    }
                                                } catch (InputMismatchException e) {
                                                    System.out.println("Please enter a valid postcode no!");
                                                    sc.nextLine();
                                                }
                                            } while (error);

                                            String postcode = Address.getPostcodeSelected(cityName, postcodeSelected - 1);

                                            String streetName;
                                            do {
                                                System.out.print("\nEnter the street name: ");
                                                streetName = sc.nextLine();

                                                if (streetName.trim().isEmpty()) {
                                                    System.out.println("Please enter the street name.");
                                                    error = true;
                                                } else {
                                                    streetName = streetName.toUpperCase();
                                                    error = false;
                                                }
                                            } while (error);

                                            Address cinemaAddress = new Address(streetName.trim(), postcode, cityName, stateName);
                                            cinema.setCinemaAddress(cinemaAddress);

                                            ResultSet result = null;
                                            try {
                                                result = DatabaseUtils.selectQueryById("cinema_address", "cinema", null, null);
                                            } catch (SQLException e) {
                                                throw new RuntimeException(e);
                                            }

                                            duplicatedAddress = cinemaAddress.checkEditAddressDuplicate(result, "cinema_address", orgCinema.getCinemaAddress().getAddress());

                                            if (duplicatedAddress == true) {
                                                System.out.println("Same cinema address detected.");
                                            }
                                        } while (duplicatedAddress);
                                        break;
                                    case 3:
                                        // Cinema phone
                                        do {
                                            System.out.print("\nEnter the new cinema phone number: ");
                                            String phoneNumber = sc.nextLine();

                                            if (phoneNumber.trim().isEmpty()) {
                                                System.out.println("Please enter the phone number.");
                                                error = true;
                                            } else {
                                                cinema.setCinemaPhone(phoneNumber.trim());

                                                if (cinema.isValidOfficePhoneNumber()) {
                                                    error = false;
                                                } else {
                                                    System.out.println("The phone number is invalid.");
                                                    error = true;
                                                }
                                            }
                                        } while (error);
                                        break;
                                }
                            } while (stop == false);
                        } else {
                            continues = false;
                            back = false;
                        }
                    } while (continues);
                    break;
                case 4:
                    // Delete Cinema
                    do {
                        error = true;
                        int cinemaDeleted = 0;
                        ArrayList<Cinema> cinemasDeleted = new ArrayList<>();
                        do {
                            try {
                                System.out.println("\nSelect the genre you want to delete: ");
                                cinemasDeleted = Cinema.viewCinemaList(1);
                                System.out.print("\nEnter the genre no (0 - Back): ");
                                cinemaDeleted = sc.nextInt();
                                sc.nextLine();

                                if (cinemaDeleted == 0 || (cinemaDeleted > 0 && cinemaDeleted <= cinemasDeleted.size() && cinemasDeleted.get(cinemaDeleted - 1).getStatus() == 1)) {
                                    error = false;
                                } else {
                                    System.out.println("Your choice is not among the available options! PLease try again.");
                                    error = true;
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Please enter a valid choice!");
                                sc.nextLine();
                                error = true;
                            }
                        } while (error);

                        if (cinemaDeleted != 0) {
                            cinema = cinemasDeleted.get(cinemaDeleted - 1);
                            String delete;
                            do {
                                System.out.println("\nAre you sure you want to delete this cinema? (Y / N)");
                                System.out.print("Answer: ");
                                String answer = sc.next();
                                sc.nextLine();

                                delete = MovieUtils.askForContinue(answer);
                            } while (delete.equals("Invalid"));

                            if (delete.equals("Y")) {
                                cinema.delete();
                            } else {
                                System.out.println("\nThe cinema is safe :)");
                            }

                            String continueDelete;
                            do {
                                System.out.println("\nDo you want to continue to delete another cinema? (Y / N)");
                                System.out.print("Answer: ");
                                String answer2 = sc.next();
                                sc.nextLine();

                                continueDelete = MovieUtils.askForContinue(answer2);
                            } while (continueDelete.equals("Invalid"));

                            if (continueDelete.equals("Y")) {
                                continues = true;
                            } else {
                                continues = false;
                                back = false;
                            }
                        } else {
                            continues = false;
                            back = false;
                        }
                    } while (continues);
                    break;
            }
        } while (back == false);
    }

    public void manageHall(Scanner sc) throws Exception {
        boolean back = false;
        do {
            int choice = displayMenu("Hall", sc);
            boolean error = true;

            switch (choice) {
                case 0:
                    back = false;
                    break;
                case 1:
                    // Hall
                    int hallNo = 0;
                    error = true;
                    ArrayList<Hall> halls = new ArrayList<>();
                    do {
                        try {
                            System.out.println("\nSelect the hall: ");
                            halls = cinema.viewHallList(1);
                            System.out.print("\nEnter the hall no (0 - Back): ");
                            hallNo = sc.nextInt();
                            sc.nextLine();

                            if(hallNo == 0) {
                                back = true;
                                error = false;
                            }
                            else if (hallNo > 0 && hallNo <= halls.size() && halls.get(hallNo - 1).getStatus() == 1) {
                                back = false;
                                error = false;
                            } else {
                                System.out.println("Your choice is not among the available options! PLease try again.");
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Please enter a valid hall no!");
                            sc.nextLine();
                        }
                    } while (error);

                    if (back == false) {
                        halls.get(hallNo - 1).viewHallDetails();
                        back = true;
                    }
                    break;
                case 2:
                    boolean insertError = true;
                    do {
                        Name name = null;
                        do {
                            System.out.print("\nEnter the hall name (0 - Back): ");
                            String hallName = sc.nextLine();

                            if (!hallName.equals("0")) {
                                back = false;
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
                            }
                            else {
                                error = false;
                                back = true;
                                insertError = false;
                            }
                        } while (error);

                        if (back == false) {
                            back = true;
                            String hallType = null;
                            do {
                                try {
                                    System.out.println("\nSelect the hall type: ");
                                    System.out.println("1. Standard Hall");
                                    System.out.println("2. 3D Hall");
                                    System.out.print("\nEnter your selection: ");
                                    int hallTypeSelection = sc.nextInt();
                                    sc.nextLine();

                                    if (hallTypeSelection == 1) {
                                        hallType = "STANDARD";
                                        error = false;
                                    } else if (hallTypeSelection == 2) {
                                        hallType = "3D";
                                        error = false;
                                    } else {
                                        System.out.println("Your choice is not among the available options! PLease try again.");
                                        error = true;
                                    }
                                } catch (InputMismatchException e) {
                                    System.out.println("Please enter a valid hall type no!");
                                    sc.nextLine();
                                    error = true;
                                }
                            } while (error);

                            Hall hall = new Hall(name, hallType);

                            insertError = cinema.addHall(hall);
                        }
                    } while (insertError);
                    break;
                case 3:
                    // Modify Hall
                    error = true;
                    boolean stop = true;
                    ArrayList<Hall> hallsModified = new ArrayList<>();
                    int hallModified = 0;

                    do {
                        try {
                            System.out.println("\nSelect the hall you want to modify: ");
                            hallsModified = cinema.viewHallList(1);
                            System.out.print("\nEnter the hall no (0 - Back): ");
                            hallModified = sc.nextInt();
                            sc.nextLine();

                            if (hallModified == 0) {
                                back = true;
                                error = false;
                            }
                            else if (hallModified > 0 && hallModified <= hallsModified.size() && hallsModified.get(hallModified - 1).getStatus() == 1) {
                                back = false;
                                error = false;
                            } else {
                                System.out.println("Your choice is not among the available options! PLease try again.");
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Please enter a valid hall no!");
                            sc.nextLine();
                        }
                    } while (error);

                    if (back == false) {
                        do {
                            cinema.setHall(hallsModified.get(hallModified - 1));
                            int serialNo = cinema.getHall().modifyHallDetails(sc);

                            switch (serialNo) {
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
                                        cinema.getHall().modifyHall();
                                    } else {
                                        System.out.println("\nThe changes have not been saved.");
                                    }
                                    back = true;
                                    break;
                                case 1:
                                    Name name = null;
                                    do {
                                        System.out.print("\nEnter the new hall name: ");
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

                                        String errorMsg = name.checkEditName("hall", result, "hall_name", cinema.getHall().getHallName().getName());

                                        if (errorMsg == null) {
                                            hallsModified.get(hallModified - 1).setHallName(name);
                                            error = false;
                                        } else {
                                            System.out.println(errorMsg);
                                            error = true;
                                        }
                                    } while (error);
                                    break;
                                case 2:
                                    System.out.println("Hall type cannot be modified! Please retry.");
                            /*do {
                                try {
                                    System.out.println("\nSelect the hall type: ");
                                    System.out.println("1. Standard Hall");
                                    System.out.println("2. 3D Hall");
                                    System.out.print("\nEnter your selection: ");
                                    int hallTypeSelection = sc.nextInt();

                                    if (hallTypeSelection == 1) {
                                        hallsModified.get(hallModified - 1).setHallType("STANDARD");
                                        //cinema.setHall(new Hall(hallsModified.get(hallModified - 1).getHallName(), "STANDARD"));
                                        error = false;
                                    } else if (hallTypeSelection == 2) {
                                        hallsModified.get(hallModified - 1).setHallType("3D");
                                        //cinema.setHall(new Hall(hallsModified.get(hallModified - 1).getHallName(), "3D"));
                                        error = false;
                                    } else {
                                        System.out.println("Your choice is not among the available options! PLease try again.");
                                        error = true;
                                    }
                                }
                                catch (InputMismatchException e) {
                                    System.out.println("Please enter a valid hall type no!");
                                    sc.nextLine();
                                    error = true;
                                }
                            } while (error);*/
                                    break;
                                case 3:
                                    System.out.println("Hall capacity cannot be modified! Please retry.");
                                    break;
                            }
                        } while (stop);
                    }
                    break;
                case 4:
                    // Delete Hall
                    error = true;
                    ArrayList<Hall> hallsDeleted = new ArrayList<>();
                    int hallDeleted = 0;
                    do {
                        try {
                            System.out.println("\nSelect the hall you want to delete: ");
                            hallsDeleted = cinema.viewHallList(1);
                            System.out.print("\nEnter the hall no (0 - Back): ");
                            hallDeleted = sc.nextInt();
                            sc.nextLine();

                            if (hallDeleted == 0) {
                                back = true;
                                error = false;
                            }
                            else if (hallDeleted > 0 && hallDeleted <= hallsDeleted.size() && hallsDeleted.get(hallDeleted - 1).getStatus() == 1) {
                                back = false;
                                error = false;
                            } else {
                                System.out.println("Your choice is not among the available options! PLease try again.");
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Please enter a valid hall no!");
                            sc.nextLine();
                        }
                    } while (error);

                    if (back == false) {
                        cinema.setHall(hallsDeleted.get(hallDeleted - 1));
                        String delete;
                        do {
                            System.out.println("\nDo you want to delete this hall? (Y / N)");
                            System.out.print("Answer: ");
                            String answer = sc.next();
                            sc.nextLine();

                            delete = MovieUtils.askForContinue(answer);
                        } while (delete.equals("Invalid"));

                        if (delete.equals("Y")) {
                            cinema.getHall().deleteHall();
                        } else {
                            System.out.println("\nThe movie is saved.");
                        }
                        back = true;
                    }
                    break;
            }
        } while (back);
    }

    public void manageMovie(Scanner sc) throws Exception {
        boolean back = false;
        ArrayList<Movie> movies = Movie.getAllMovies();

        do {
            int choice = displayMenu("Movie", sc);
            boolean error = true;
            boolean continues = true;

            switch (choice) {
                case 0:
                    back = true;
                    break;
                case 1:
                    // View Movie
                    do {
                        int choice1 = 0;
                        ArrayList<Movie> moviesAfterFiltered = Movie.showMovieListAfterFiltered(null, null, 1);

                        do {
                            try {
                                System.out.print("\nEnter the movie no (0 - Back): ");
                                choice1 = sc.nextInt();
                                sc.nextLine();

                                if (choice1 == 0 || (choice1 > 0 && choice1 <= moviesAfterFiltered.size() && moviesAfterFiltered.get(choice1 - 1).getStutus() == 1)) {
                                    error = false;
                                }
                                else {
                                    System.out.println("Your choice is not among the available options! PLease try again.");
                                    error = true;
                                }
                            }
                            catch (InputMismatchException e) {
                                System.out.println("Please enter a valid choice!");
                                sc.nextLine();
                                error = true;
                            }
                        } while (error);

                        if (choice1 != 0) {
                            Movie viewMovie = moviesAfterFiltered.get(choice1 - 1);
                            viewMovie.viewMovieDetails();

                            String continueViewMovie;
                            do {
                                System.out.println("\nDo you want view another movie? (Y / N)");
                                System.out.print("Answer: ");
                                String answer = sc.next();
                                sc.nextLine();

                                continueViewMovie = MovieUtils.askForContinue(answer);
                            } while (continueViewMovie.equals("Invalid"));

                            if (continueViewMovie.equals("Y")) {
                                continues = true;
                            } else {
                                continues = false;
                                back = false;
                            }
                        } else {
                            continues = false;
                            back = false;
                        }
                    } while (continues);
                    break;
                case 2:
                    do {
                        Movie newMovie = new Movie();

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
                                newMovie.setMvName(name);
                                error = false;
                            } else {
                                System.out.println(errorMessage + "\n");
                                error = true;
                            }
                        } while (error);

                        // Movie Genre ID
                        do {
                            try {
                                Object[] params = {1};
                                ResultSet result = DatabaseUtils.selectQueryById("genre_id, genre_name", "genre", "genre_status = ?", params);

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
                                    int genreSelected = sc.nextInt();
                                    sc.nextLine();

                                    String errorMessage = MovieValidator.checkGenreID(genreID.size(), genreSelected);

                                    if (errorMessage == null) {
                                        Genre genre = new Genre(genreID.get(genreSelected - 1));
                                        newMovie.setGenre(genre);  // ArrayList starts from index 0
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
                                            newMovie.setReleaseDate(date);
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
                                    newMovie.setDuration(duration);
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

                        String mvLanguage = Movie.getMultipleChosens(sc, languages, "languages");
                        newMovie.setLang(mvLanguage);

                        // Movie Director
                        String mvDirector = Movie.getMultipleValues(sc, "director", "directors");
                        newMovie.setDirector(mvDirector);

                        // Movie Writter
                        String mvWritter = Movie.getMultipleValues(sc, "writter", "writters");
                        newMovie.setWritter(mvWritter);

                        // Movie Starring
                        String mvStarring = Movie.getMultipleValues(sc, "starring", "starrings");
                        newMovie.setStarring(mvStarring);

                        // Movie Music Provider
                        String mvMusicProvider = Movie.getMultipleValues(sc, "music provider", "music prodivers");
                        newMovie.setMusicProvider(mvMusicProvider);

                        // Movie Country
                        String[] countries = {"United States", "United Kingdom", "Canada", "China", "Taiwan", "Malaysia", "Singapore", "Japan", "North Korea", "Italy", "Hong Kong", "France", "Russia", "India"};

                        String mvCountry = Movie.getMultipleChosens(sc, countries, "countries");
                        newMovie.setCountry(mvCountry);

                        // Movie Meta Description
                        do {
                            System.out.print("\nEnter movie meta description: ");
                            String mvDescription = sc.nextLine();

                            String errorMessage = MovieValidator.checkMetaDescription(mvDescription);

                            if (errorMessage == null) {
                                newMovie.setMetaDescription(mvDescription);
                                error = false;
                            }
                            else {
                                System.out.println(errorMessage);
                                error = true;
                            }
                        } while (error);

                        // Movie Child Ticket Price
                        double mvChildTicketPrice = Movie.getTicketPrice(sc, "child");
                        newMovie.setChildTicketPrice(mvChildTicketPrice);

                        // Movie Adult Ticket Price
                        double mvAdultTicketPrice = Movie.getTicketPrice(sc, "adult");
                        newMovie.setAdultTicketPrice(mvAdultTicketPrice);

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
                            back = true;
                        } else {
                            back = false;
                        }
                    } while (back);
                    break;
                case 3:
                    ArrayList<Movie> moviesAfterFiltered;
                    int movieID = 1;

                    do {
                        System.out.println("\nSelect the movie you want to modify: ");
                        moviesAfterFiltered = Movie.showMovieListAfterFiltered(null, null, 1);

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

                        if (movieID != 0) {
                            Movie orgMovie = moviesAfterFiltered.get(movieID - 1);
                            movie = new Movie(orgMovie.getGenre(), orgMovie.getMvName(), orgMovie.getReleaseDate(), orgMovie.getDuration(), orgMovie.getLang(), orgMovie.getDirector(), orgMovie.getWritter(), orgMovie.getStarring(), orgMovie.getMusicProvider(), orgMovie.getCountry(), orgMovie.getMetaDescription(), orgMovie.getChildTicketPrice(), orgMovie.getAdultTicketPrice());
                            boolean stop = true;

                            do {
                                int serialNum = movie.modifyMovieDetail(sc);
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
                                            movie.modify();
                                        }
                                        else {
                                            setMovie(orgMovie);
                                            System.out.println("\nThe changes have not been saved.");
                                        }
                                        back = false;
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

                                            String errorMessage = name.checkEditName("movie", result, "mv_name", orgMovie.getMvName().getName());

                                            if (errorMessage == null) {
                                                movie.setMvName(name);
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
                                                Object[] params = {1};
                                                ResultSet result = DatabaseUtils.selectQueryById("genre_id, genre_name", "genre", "genre_status = ?", params);

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
                                                        Genre genre = new Genre(genreID.get(newGenre - 1));
                                                        movie.setGenre(genre);  // ArrayList starts from index 0
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
                                                        String errorMessage = editDate.checkLocalDate();

                                                        if (errorMessage == null) {
                                                            movie.setReleaseDate(editDate);
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
                                                    movie.setDuration(editDuration);
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

                                        String editMvLanguage = movie.getMultipleChosens(sc, languages, "languages");
                                        movie.setLang(editMvLanguage);
                                        break;
                                    case 6:
                                        // Movie Director
                                        String editMvDirector = movie.getMultipleValues(sc, "director", "directors");
                                        movie.setDirector(editMvDirector);
                                        break;
                                    case 7:
                                        // Movie Writter
                                        String editMvWritter = movie.getMultipleValues(sc, "writter", "writters");
                                        movie.setWritter(editMvWritter);
                                        break;
                                    case 8:
                                        // Movie Starring
                                        String editMvStarring = movie.getMultipleValues(sc, "starring", "starrings");
                                        movie.setStarring(editMvStarring);
                                        break;
                                    case 9:
                                        // Movie Music Provider
                                        String editMvMusicProvider = movie.getMultipleValues(sc, "music provider", "music prodivers");
                                        movie.setMusicProvider(editMvMusicProvider);
                                        break;
                                    case 10:
                                        // Movie Country
                                        String[] countries = {"United States", "United Kingdom", "Canada", "China", "Taiwan", "Malaysia", "Singapore", "Japan", "North Korea", "Italy", "Hong Kong", "France", "Russia", "India"};

                                        String editMvCountry = movie.getMultipleChosens(sc, countries, "countries");
                                        movie.setCountry(editMvCountry);
                                        break;
                                    case 11:
                                        // Movie Meta Description
                                        do {
                                            System.out.print("\nEnter the new movie meta description: ");
                                            String editMvDescription = sc.nextLine();

                                            String errorMessage = MovieValidator.checkMetaDescription(editMvDescription);

                                            if (errorMessage == null) {
                                                movie.setMetaDescription(editMvDescription);
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
                                        double editMvChildTicketPrice = movie.getTicketPrice(sc, "child");
                                        movie.setChildTicketPrice(editMvChildTicketPrice);
                                        break;
                                    case 13:
                                        // Movie Adult Ticket Price
                                        double editMvAdultTicketPrice = movie.getTicketPrice(sc, "adult");
                                        movie.setAdultTicketPrice(editMvAdultTicketPrice);
                                        break;
                                }
                            } while (stop);
                            back = true;
                        } else {
                            back = false;
                        }
                    } while (back);
                    break;
                case 4:
                    movieID = 0;
                    do {
                        System.out.println("\nSelect the movie you want to delete: ");
                        moviesAfterFiltered = Movie.showMovieListAfterFiltered(null, null, 1);

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

                        if (movieID != 0) {
                            movie = moviesAfterFiltered.get(movieID - 1);
                            movie.viewMovieDetails();
                            String delete;
                            do {
                                System.out.println("\nDo you want to delete this movie? (Y / N)");
                                System.out.print("Answer: ");
                                String answer = sc.next();
                                sc.nextLine();

                                delete = MovieUtils.askForContinue(answer);
                            } while (delete.equals("Invalid"));

                            if (delete.equals("Y")) {
                                movie.delete();
                            }
                            else {
                                System.out.println("\nThe movie is saved.");
                            }
                            back = true;
                        } else {
                            back = false;
                        }
                    } while (back);
                    break;
            }
        } while (back == false);
    }

    public void manageGenre(Scanner sc) throws Exception {
        boolean back = false;

        do {
            ArrayList<Genre> genres = new ArrayList<>();
            try {
                ResultSet result = DatabaseUtils.selectQueryById("*", "genre", null, null);

                while (result.next()) {
                    Genre genre = new Genre(result.getInt("genre_id"), new Name(result.getString("genre_name")), result.getInt("post"), result.getInt("genre_status"));
                    genres.add(genre);
                }

                result.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }

            int choice = displayMenu("Genre", sc);
            boolean error = true;
            boolean continues = true;

            switch (choice) {
                case 0:
                    back = true;
                    break;
                case 1:
                    Genre.viewGenreDetails(1);
                    System.out.print("\nPress any key to back...");
                    sc.nextLine();
                    back = false;
                    break;
                case 2:
                    do {
                        Genre newGenre = new Genre();
                        // Genre Name
                        Name name = null;
                        do {
                            System.out.print("\nEnter genre name: ");
                            String genreName = sc.nextLine();

                            name = new Name(genreName);
                            name.capitalizeWords();

                            ResultSet result = null;
                            try {
                                result = DatabaseUtils.selectQueryById("genre_name", "genre", null, null);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }

                            String errorMessage = name.checkName("genre", result, "genre_name");

                            if (errorMessage == null) {
                                newGenre.setGenreName(name);
                                error = false;
                            } else {
                                System.out.println(errorMessage);
                                error = true;
                            }
                        } while (error);

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
                            back = false;
                        }
                    } while (continues);
                    break;
                case 3:
                    do {
                        int genreModified = 0;
                        do {
                            try {
                                System.out.println("\nSelect the genre you want to modify: ");
                                Genre.viewGenreDetails(1);
                                System.out.print("\nEnter the genre no (0 - Back): ");
                                genreModified = sc.nextInt();
                                sc.nextLine();

                                if (genreModified == 0 || (genreModified > 0 && genreModified <= genres.size() && genres.get(genreModified - 1).getStatus() == 1)) {
                                    error = false;
                                } else {
                                    System.out.println("Your choice is not among the available options! PLease try again.");
                                    error = true;
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Please enter a valid choice!");
                                sc.nextLine();
                                error = true;
                            }
                        } while (error);

                        if (genreModified != 0) {
                            Genre orgGenre = genres.get(genreModified - 1);
                            genre = new Genre(orgGenre.getGenreID(), orgGenre.getGenreName(), orgGenre.getPost(), orgGenre.getStatus());
                            Name name = null;
                            do {
                                System.out.print("\nEnter the new genre name (0 - Back): ");
                                String editGenreName = sc.nextLine();

                                if (editGenreName.equals("0")) {
                                    error = false;
                                } else {
                                    name = new Name(editGenreName);
                                    name.capitalizeWords();

                                    ResultSet result = null;
                                    try {
                                        result = DatabaseUtils.selectQueryById("genre_name", "genre", null, null);
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }

                                    String errorMessage = name.checkEditName("genre", result, "genre_name", genre.getGenreName().getName());

                                    if (errorMessage == null) {
                                        error = false;
                                        genre.setGenreName(name);

                                        String save;
                                        do {
                                            System.out.println("\nDo you want to save the changes? (Y / N)");
                                            System.out.print("Answer: ");
                                            String answer = sc.next();
                                            sc.nextLine();

                                            save = MovieUtils.askForContinue(answer);
                                        } while (save.equals("Invalid"));

                                        if (save.equals("Y")) {
                                            genre.modify();
                                        } else {
                                            genre.setGenreName(orgGenre.getGenreName());
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
                                continues = true;
                            } else {
                                back = false;
                                continues = false;
                            }
                        } else {
                            back = false;
                            continues = false;
                        }
                    } while (continues);
                    break;
                case 4:
                    // Delere genre
                    do {
                        int genreDeleted = 0;
                        do {
                            try {
                                System.out.println("\nSelect the genre you want to delete: ");
                                Genre.viewGenreDetails(1);
                                System.out.print("\nEnter the genre no (0 - Back): ");
                                genreDeleted = sc.nextInt();
                                sc.nextLine();

                                if (genreDeleted == 0 || (genreDeleted > 0 && genreDeleted <= genres.size() && genres.get(genreDeleted - 1).getStatus() == 1)) {
                                    error = false;
                                } else {
                                    System.out.println("Your choice is not among the available options! PLease try again.");
                                    error = true;
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Please enter a valid choice!");
                                sc.nextLine();
                                error = true;
                            }
                        } while (error);

                        if (genreDeleted != 0) {
                            int post = genres.get(genreDeleted - 1).getPost();

                            if (post == 0) {
                                genre = genres.get(genreDeleted - 1);
                                String delete;
                                do {
                                    System.out.println("\nAre you sure you want to delete this genre? (Y / N)");
                                    System.out.print("Answer: ");
                                    String answer = sc.next();
                                    sc.nextLine();

                                    delete = MovieUtils.askForContinue(answer);
                                } while (delete.equals("Invalid"));

                                if (delete.equals("Y")) {
                                    genre.delete();
                                } else {
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
                                    continues = true;
                                } else {
                                    continues = false;
                                    back = false;
                                }
                            } else {
                                System.out.println("Sorry, you cannot delete this genre. Please make sure there are no movie posts in this genre!");
                                continues = true;
                            }
                        } else {
                            continues = false;
                            back = false;
                        }
                    } while (continues);
                    break;
            }
        } while (back == false);
    }

    public void manageSchedule(Scanner sc) throws Exception {
        boolean back = false;

        do {
            int choice = displayMenu("Schedule", sc);
            boolean error = true;

            switch (choice) {
                case 0:
                    back = true;
                    break;
                case 1:
                    ArrayList<TimeTable> viewTimeTables = TimeTable.viewSchedule(sc);
                    break;
                case 2:
                    TimeTable newSchedule = new TimeTable();

                    ArrayList<Movie> moviesAfterFiltered;
                    int movieID = 1, hallID = 1;
                    error = true;

                    do {
                        moviesAfterFiltered = Movie.viewMovieListByFilter(sc);

                        if (moviesAfterFiltered != null) {
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
                        }
                    } while (movieID == 0 && moviesAfterFiltered != null);

                    if (movieID != 0 && moviesAfterFiltered != null) {
                        newSchedule.setMovie(moviesAfterFiltered.get(movieID - 1));

                        // Cinema
                        int cinemaNo = 0;
                        error = true;
                        ArrayList<Cinema> cinemas = new ArrayList<>();
                        do {
                            try {
                                System.out.print("\nSelect the cinema you want to view the schedule: ");
                                cinemas = Cinema.viewCinemaList(1);
                                System.out.print("\nEnter the cinema no: ");
                                cinemaNo = sc.nextInt();
                                sc.nextLine();

                                if (cinemaNo > 0 && cinemaNo <= cinemas.size() && cinemas.get(cinemaNo - 1).getStatus() == 1) {
                                    error = false;
                                } else {
                                    System.out.println("Your choice is not among the available options! PLease try again.");
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Please enter a valid cinema no!");
                                sc.nextLine();
                            }
                        } while (error);

                        // Hall
                        int hallNo = 0;
                        error = true;
                        ArrayList<Hall> halls = new ArrayList<>();
                        do {
                            try {
                                System.out.println("\nSelect the hall: ");
                                halls = cinemas.get(cinemaNo - 1).viewHallList(1);
                                System.out.print("\nEnter the hall no: ");
                                hallNo = sc.nextInt();
                                sc.nextLine();

                                if (hallNo > 0 && hallNo <= halls.size() && halls.get(hallNo - 1).getStatus() == 1) {
                                    error = false;
                                } else {
                                    System.out.println("Your choice is not among the available options! PLease try again.");
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Please enter a valid hall no!");
                                sc.nextLine();
                            }
                        } while (error);

                        newSchedule.setHall(halls.get(hallNo - 1));

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
                                        String errorMessage = addDate.checkLocalDate();
                                        if (errorMessage == null) {
                                            newSchedule.setShowDate(addDate);
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

                        LocalTime[] selectedTimeSlots = newSchedule.availableTimeSlots(sc);
                        newSchedule.setStartTime(selectedTimeSlots[0]);
                        newSchedule.setEndTime(selectedTimeSlots[1]);

                        // Add schedule
                        newSchedule.add();
                    }
                    back = false;
                    break;
                case 3:
                    // Modify Schedule
                    error = true;
                    int scheduleNo = 0;
                    ArrayList<TimeTable> timeTables = TimeTable.viewSchedule(sc);
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

                    TimeTable modifySchedule = new TimeTable(timeTables.get(scheduleNo - 1).getTimetableID(), timeTables.get(scheduleNo - 1).getMovie(), timeTables.get(scheduleNo - 1).getHall(), timeTables.get(scheduleNo - 1).getShowDate(), timeTables.get(scheduleNo - 1).getStartTime());

                    error = true;
                    int choice2 = 0;
                    do {
                        try {
                            System.out.println("\nSelect the operation:");
                            System.out.println("1. Modify the movie show time");
                            System.out.println("2. Modify the movie show date");
                            System.out.println("3. Modify the movie to be played");
                            System.out.println("4. Modify the location of the movie to be played");
                            System.out.print("\nEnter your selection (0 - Back): ");
                            choice2 = sc.nextInt();
                            sc.nextLine();

                            if (choice2 < 0 || choice2 > 4) {
                                System.out.println("Your choice is not among the available options! PLease try again.");
                            } else {
                                error = false;
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Please enter a valid operation no.");
                            sc.nextLine();
                        }
                    } while (error);

                    switch (choice2) {
                        case 1:
                            // 时间调整
                            LocalTime[] selectedTimeSlots = modifySchedule.availableTimeSlots(sc);
                            modifySchedule.setStartTime(selectedTimeSlots[0]);
                            modifySchedule.setEndTime(selectedTimeSlots[1]);

                            modifySchedule.modify();
                            break;
                        case 2:
                            // 日期调整
                            error = true;
                            String date;
                            ShowDate modifyDate = null;  // Store the old show date
                            boolean validDate;

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
                                            String errorMessage = modifyDate.checkLocalDate();

                                            if (errorMessage == null) {
                                                modifySchedule.setShowDate(modifyDate);
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
                            selectedTimeSlots = modifySchedule.availableTimeSlots(sc);
                            modifySchedule.setStartTime(selectedTimeSlots[0]);
                            modifySchedule.setEndTime(selectedTimeSlots[1]);

                            modifySchedule.modify();
                            break;
                        case 3:
                            // Modify the movie to be played
                            movieID = 1;
                            do {
                                moviesAfterFiltered = Movie.viewMovieListByFilter(sc);

                                if (moviesAfterFiltered != null) {
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
                                }
                            } while (movieID == 0 && moviesAfterFiltered != null);

                            if (movieID != 0 && moviesAfterFiltered != null) {

                                modifySchedule.setMovie(moviesAfterFiltered.get(movieID - 1));

                                // 日期调整
                                error = true;
                                modifyDate = null;  // Store the old show date

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
                                                String errorMessage = modifyDate.checkLocalDate();

                                                if (errorMessage == null) {
                                                    modifySchedule.setShowDate(modifyDate);
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
                                selectedTimeSlots = modifySchedule.availableTimeSlots(sc);
                                modifySchedule.setStartTime(selectedTimeSlots[0]);
                                modifySchedule.setEndTime(selectedTimeSlots[1]);

                                modifySchedule.modify();
                            }
                            break;
                        case 4:
                            break;
                    }
                    back = false;
                    break;
                case 4:
                    timeTables = TimeTable.viewSchedule(sc);
                    scheduleNo = 1;
                    String delete;

                    do {
                        try {
                            System.out.print("\nEnter the schedule no. you want to delete (0 - Back): ");
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

                    TimeTable deleteSchedule = new TimeTable(timeTables.get(scheduleNo - 1).getTimetableID(), timeTables.get(scheduleNo - 1).getMovie(), timeTables.get(scheduleNo - 1).getHall(), timeTables.get(scheduleNo - 1).getShowDate(), timeTables.get(scheduleNo - 1).getStartTime());

                    do {
                        System.out.println("\nDo you want to delete this schedule? (Y / N)");
                        System.out.print("Answer: ");
                        String answer = sc.next();
                        sc.nextLine();

                        delete = MovieUtils.askForContinue(answer);
                    } while (delete.equals("Invalid"));

                    if (delete.equals("Y")) {
                        deleteSchedule.delete();
                    }
                    else {
                        System.out.println("\nThe schedule is saved.");
                    }
                    back = false;
                    break;
            }
        } while (back == false);
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
                System.out.print("\nEnter your selection (0 - Back): ");
                choice = sc.nextInt();
                sc.nextLine();

                if (choice >= 0 && choice <= 4) {
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

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Cinema getCinema() {
        return cinema;
    }

    public Movie getMovie() {
        return movie;
    }

    public Genre getGenre() {
        return genre;
    }
}