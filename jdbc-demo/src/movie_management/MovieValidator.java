package movie_management;

import Connect.DatabaseUtils;
import java.sql.ResultSet;

public class MovieValidator {
    private MovieValidator(){
    }

    public static String checkGenreID(int range, int choice){
        if (choice <= 0 || choice > range) {
            return "Please enter a valid choice!";
        }
        return null;
    }

    /*public static String checkMovieName(String mvName) throws Exception{
        if (mvName.trim().isEmpty()) {
            return "Please enter the movie name.";
        }
        else {
            boolean duplicateName = checkDuplicateName(mvName);
            if(duplicateName == true) {
                return "Same movie name detected.";
            }
            else{
                return null;
            }
        }
    }

    public static boolean checkDuplicateName(String mvName) throws Exception{
        try {
            ResultSet result = DatabaseUtils.selectQueryById("mv_name", "movie", null, null);

            while (result.next()) {
                String name = result.getString("mv_name");

                if (mvName.equals(name)) {
                    result.close();
                    return true;
                }
            }
            result.close();

            return false;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }*/

    // Check whether the release date later than the current date
    public static String checkDuration(int minutes){
        if (minutes < 80 || minutes > 300) {
            return "The movie must be between 80 and 300 minutes long.";
        }
        return null;
    }

    public static String checkRange(int choice, String[] languages){
        if (choice < 0 || choice > languages.length) {
            return "The selection must be between 1 and " + languages.length + ".";
        }
        return null;
    }

    public static String checkValue(String value, String propertyName) throws Exception{
        if (value.trim().isEmpty()) {
            return "Please enter the movie " + propertyName + ".";
        }
        return null;
    }

    public static String checkMetaDescription(String metaDescription){
        if (metaDescription.trim().isEmpty()) {
            return "Please enter the movie meta description.";
        }
        else {
            if (metaDescription.contains("'")) {
                return "Apostrophe cannot be contains in the description.";
            }
            else {
                return null;
            }
        }
    }

    public static String checkTicketPrice(double ticketPrice){
        if (ticketPrice < 0 || ticketPrice > 100) {
            return "The ticket price must be between RM0 and RM100.";
        }
        return null;
    }

    // For Edit Movie
    public static String checkEditMovieName(String mvName, String orgMvName) throws Exception{
        if (mvName.trim().isEmpty()) {
            return "Please enter the movie name.";
        }
        else {
            boolean duplicateName = checkEditDuplicateName(mvName, orgMvName);
            if(duplicateName == true) {
                return "Same movie name detected.";
            }
            else{
                return null;
            }
        }
    }

    public static boolean checkEditDuplicateName(String mvName, String orgMvName) throws Exception{
        try {
            ResultSet result = DatabaseUtils.selectQueryById("mv_name", "movie", null, null);

            while (result.next()) {
                String name = result.getString("mv_name");

                if (mvName.equals(name)) {
                    if (mvName.equals(orgMvName)) {
                        result.close();
                        return false;
                    }
                    else {
                        result.close();
                        return true;
                    }
                }
            }
            result.close();

            return false;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}