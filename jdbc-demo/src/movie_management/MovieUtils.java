package movie_management;

import java.util.ArrayList;

public class MovieUtils {
    private MovieUtils(){
    }

    public static ArrayList<Movie> queryMovieByName(ArrayList<Movie> movies, String mvName){
        ArrayList<Movie> searchResults = new ArrayList<>();

        String userInput = mvName.toLowerCase();

        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);
            String name = movie.getMvName().getName().toLowerCase();

            if (name.contains(userInput)) {
                searchResults.add(movie);
            }
        }
        return searchResults;
    }

    // Format double data type value to two decimal point
    public static double formatDouble(double value){
        return Math.round(value * 100.0) / 100.0;
    }
}