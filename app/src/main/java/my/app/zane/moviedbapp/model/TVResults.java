package my.app.zane.moviedbapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TVResults {


    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("poster_path")
    @Expose
    private String poster_path;

    //OUR GETTERS AND SETTERS!

    public String getTitle() {
        return name;
    }

    public void setTitle(String title) {
        this.name = title;
    }


    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //OUR toSTRING METHOD
    @Override
    public String toString() {
        return "Results{" +
                "title='" + name + '\'' +
                ", id=" + id +
                ", poster_path='" + poster_path + '\'' +
                '}';
    }
}
