package my.app.zane.moviedbapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Details {

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("tagline")
    @Expose
    private String tagline;

    @SerializedName("overview")
    @Expose
    private String overview;

    @SerializedName("runtime")
    @Expose
    private int runtime;

    @SerializedName("budget")
    @Expose
    private int budget;

    @SerializedName("revenue")
    @Expose
    private int revenue;

    @SerializedName("release_date")
    @Expose
    private String release_date;

    @SerializedName("genres")
    @Expose
    private ArrayList<Genres> genre;

    @SerializedName("poster_path")
    @Expose
    private String poster_path;

    //GETTERS AND SETTERS


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public int getRevenue() {
        return revenue;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public ArrayList<Genres> getGenre() {
        return genre;
    }

    public void setGenre(ArrayList<Genres> genre) {
        this.genre = genre;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    //ToString

    @Override
    public String toString() {
        return "Details{" +
                "title='" + title + '\'' +
                ", tagline='" + tagline + '\'' +
                ", overview='" + overview + '\'' +
                ", runtime=" + runtime +
                ", budget=" + budget +
                ", revenue=" + revenue +
                ", release_date='" + release_date + '\'' +
                ", genre=" + genre +
                '}';
    }
}
