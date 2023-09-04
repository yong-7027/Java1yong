package genre_management;

import Connect.DatabaseUtils;
import Driver.Name;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Genre {
    private int genreID;
    private Name genreName;
    private int post;

    public Genre(){
        post = 0;
    }

    // Constructor for getting data from the database
    public Genre(int genreID, Name genreName, int post){
        this.genreID = genreID;
        this.genreName = genreName;
        this.post = post;
    }

    // Method
    public static void viewGenreDetails(ArrayList<Genre> genres) throws SQLException {
        System.out.printf("\n%-5s %-15s %s\n", "No", "Genre Name", "Post");

        for (Genre genre : genres) {
            System.out.printf("%-5d %-15s %s\n", genre.getGenreID(), genre.getGenreName().getName(), genre.getPost());
        }
    }

    public void addGenre() throws SQLException {
        int rowAffected = 0;

        try {
            String insertSql = "INSERT INTO `genre`(`genre_name`, `post`) VALUES (?, ?)";
            Object[] params = {genreName.getName(), getPost()};
            rowAffected = DatabaseUtils.insertQuery(insertSql, params);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        if (rowAffected > 0) {
            System.out.println("\nGenre successfully added...");
        }
        else {
            System.out.println("\nSomething went wrong!");
        }
    }

    public void modifyGenre() throws SQLException {
        int rowAffected = 0;

        try {
            String updateSql = "UPDATE `genre` SET `genre_name`= ? WHERE genre_id = ?";
            Object[] params = {getGenreName().getName(), getGenreID()};
            rowAffected = DatabaseUtils.updateQuery(updateSql, params);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        if (rowAffected > 0) {
            System.out.println("\nThe changes have been saved.");
            setGenreName(genreName);
        } else {
            System.out.println("\nSomething went wrong...");
        }
    }

    public void deleteGenre() throws SQLException {
        int rowAffected = 0;

        try {
            Object[] params = {getGenreID()};
            rowAffected = DatabaseUtils.deleteQueryById("genre", "genre_status", "genre_id", params);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        if (rowAffected > 0) {
            System.out.println("\nThis genre has been deleted.");
        } else {
            System.out.println("\nSomething went wrong...");
        }
    }

    // Setter
    // No setter methods for genreID and post
    public void setGenreName(Name genreName){
        this.genreName = genreName;
    }

    // Getter
    public int getGenreID(){
        return genreID;
    }

    public Name getGenreName(){
        return genreName;
    }

    public int getPost() throws SQLException {
        Object[] params = {1, getGenreID()};
        ResultSet result = DatabaseUtils.selectQueryById("count(*) AS POST", "movie m, genre g", "m.genre_id = g.genre_id AND m.movie_status = ? AND g.genre_id = ?", params);
        while (result.next()) {
            post = result.getInt("post");
        }
        return post;
    }
}