package my.app.zane.moviedbapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TVDetails {

    @SerializedName("created_by")
    @Expose
    private ArrayList<CreatedBy> created_by;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("first_air_date")
    @Expose
    private String first_air_date;

    @SerializedName("last_air_date")
    @Expose
    private String last_air_date;

    @SerializedName("genres")
    @Expose
    private ArrayList<Genres> genre;

    @SerializedName("number_of_episodes")
    @Expose
    private int number_of_episodes;

    @SerializedName("number_of_seasons")
    @Expose
    private int number_of_seasons;

    @SerializedName("overview")
    @Expose
    private String overview;

    @SerializedName("poster_path")
    @Expose
    private String poster_path;

        public ArrayList<CreatedBy> getCreated_by() {
        return created_by;
    }
    public void setCreated_by(ArrayList<CreatedBy> created_by) {
        this.created_by = created_by;
    }

    public String getFirst_air_date() {
        return first_air_date;
    }

    public void setFirst_air_date(String first_air_date) {
        this.first_air_date = first_air_date;
    }

    public String getLast_air_date() {
        return last_air_date;
    }

    public void setLast_air_date(String last_air_date) {
        this.last_air_date = last_air_date;
    }

    public ArrayList<Genres> getGenre() {
        return genre;
    }

    public void setGenre(ArrayList<Genres> genre) {
        this.genre = genre;
    }

    public int getNumber_of_episodes() {
        return number_of_episodes;
    }

    public void setNumber_of_episodes(int number_of_episodes) {
        this.number_of_episodes = number_of_episodes;
    }

    public int getNumber_of_seasons() {
        return number_of_seasons;
    }

    public void setNumber_of_seasons(int number_of_seasons) {
        this.number_of_seasons = number_of_seasons;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }
}
