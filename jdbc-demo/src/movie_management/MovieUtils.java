package movie_management;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MovieUtils {
    private MovieUtils(){
    }

    public static Movie queryMovie(ArrayList<CustomerMovie> movies, String mvName){
        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);
            String name = movie.getMvName().toUpperCase();

            if (name.equals(mvName)) {
                return movie;
            }
        }
        return null;
    }

    public static String capitalizeWords(String input){
        StringBuilder result = new StringBuilder();
        String[] words = input.split(" ");

        for (String word : words) {
            if (!word.isEmpty()) {
                char firstChar = Character.toUpperCase(word.charAt(0));  // Capitalize the first character of the word
                String restOfWord = word.substring(1).toLowerCase();  // substring(1) returns a substring from index 1 (the second character) to the end of the string
                result.append(firstChar).append(restOfWord).append(" ");  // Combine them and put them into result
            }
        }

        return result.toString().trim();  // Replace it with a normal string and remove the leading and trailing spaces
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