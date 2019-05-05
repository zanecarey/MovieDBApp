package my.app.zane.moviedbapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchName {

    @SerializedName("id")
    @Expose
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
