package movie_management;

import java.util.ArrayList;

public class MovieUtils {
    private MovieUtils(){
    }

    public static Movie queryMovie(ArrayList<CustomerMovie> movies, String mvName){
        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);
            String name = movie.getMvName().getName().toUpperCase();

            if (name.equals(mvName)) {
                return movie;
            }
        }
        return null;
    }

    public static String askForContinue(String answer){
        answer = answer.toUpperCase();

        if (answer.equals("Y") || answer.equals("YES")) {
            return "Y";
        }
        else if (answer.equals("N") || answer.equals("NO")) {
            return "N";
        }
        else {
            System.out.println("Please enter Y / N.");
            return "Invalid";
        }
    }

    // Format double data type value to two decimal point
    public static double formatDouble(double value){
        return Math.round(value * 100.0) / 100.0;
    }
}