package movie_management;

import Connect.DatabaseUtils;
import Driver.Name;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CustomerMovie extends Movie{
    public static void customer(Scanner sc) throws Exception {
        //Scanner sc = new Scanner(System.in);
        boolean status1 = false, status2 = false, status3 = false;

        try {
            ResultSet result = DatabaseUtils.selectQueryById("*", "movie", null, null);

            ArrayList<CustomerMovie> movies = new ArrayList<>();

            while (result.next()) {
                //Movie movie = new Movie(id, mvName, releaseDate, duration, lang, director, writter, starring, musicProvider, country, metaDescription, childTicketPrice, adultTicketPrice);
                CustomerMovie movie = new CustomerMovie();

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

            do {
                System.out.println("\nSelect the option:");
                System.out.println("1. View profile");
                System.out.println("2. View movie list");
                System.out.println("3. Search movie");

                System.out.print("\nEnter you selection: ");
                int choice1 = sc.nextInt();
                sc.nextLine(); // 消耗剩余的回车符

                switch (choice1) {
                    case 1:
                        // view profile
                        status1 = false;
                        break;
                    case 2:
                        //status1 = false;
                        //int choice2 = Movie.viewMovieList(movies, sc);
                        //if (choice2 != 0) {
                          //  movies.get(choice2 - 1).movieDetail();
                        //}
                        break;
                    case 3:
                        status1 = false;
                        System.out.println("\nSearch Movie");
                        do {
                            System.out.print("\nEnter the movie name you want to search for (X - Back): ");
                            String mvName = sc.nextLine();

                            mvName = mvName.toUpperCase();

                            if(mvName.equals("X")){
                                status1 = true;
                                break;
                            }
                            else{
                                Movie movie = MovieUtils.queryMovie(movies, mvName);

                                if (movie != null) {
                                    movie.movieDetail();
                                    String continues = "N";

                                    do {
                                        System.out.println("\nDo you want to continue for searching the movie? (Y / N)");
                                        System.out.print("Answer: ");
                                        String answer1 = sc.next();
                                        sc.nextLine(); // 消耗剩余的回车符

                                        continues = MovieUtils.askForContinue(answer1);
                                    } while (continues.equals("Invalid"));

                                    if (continues.equals("Y")) {
                                        // Continue for searching
                                        System.out.println("\nSearch Movie");
                                        status2 = true;
                                        break;
                                    }
                                    else {
                                        // Discontinue for searching, status2 = true
                                        status1 = true;
                                        status2 = false;
                                        break;
                                    }
                                }
                                else {
                                    status2 = true;
                                    System.out.println("Sorry, this movie cannot be found! Please retry.");
                                }
                            }
                        } while (status2 == true);
                        break;
                    default:
                        status1 = true;
                        System.out.println("Invalid input! Please retry.");
                }
            } while (status1 == true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}