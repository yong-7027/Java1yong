package genre_management;

import Connect.DatabaseUtils;
import movie_management.Movie;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Genre {
    private int genreID;
    private String genreName;
    private int post;

    public Genre(){
        post = 0;
    }

    // Constructor for getting data from the database
    public Genre(int genreID, String genreName, int post){
        this.genreID = genreID;
        this.genreName = genreName;
        this.post = post;
    }

    // Method
    public static void viewGenreDetails(ArrayList<? extends Genre> genres) throws SQLException {
        System.out.printf("\n%-5s %-15s %s\n", "No", "Genre Name", "Post");

        for (Genre genre : genres) {
            System.out.printf("%-5d %-15s %s\n", genre.getGenreID(), genre.getGenreName(), genre.getPost());
        }
    }

    // Setter
    // No setter methods for genreID and post
    public void setGenreName(String genreName){
        this.genreName = genreName;
    }

    // Getter
    public int getGenreID(){
        return genreID;
    }

    public String getGenreName(){
        return genreName;
    }

    public int getPost() throws SQLException {
        Object[] params = {getGenreID()};
        ResultSet result = DatabaseUtils.selectQueryById("count(*) AS POST", "movie m, genre g", "m.genre_id = g.genre_id AND g.genre_id = ?", params);
        while (result.next()) {
            post = result.getInt("post");
        }
        return post;
    }
}