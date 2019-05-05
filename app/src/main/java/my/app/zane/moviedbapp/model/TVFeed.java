package my.app.zane.moviedbapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TVFeed {
    @SerializedName("results")
    @Expose
    private ArrayList<TVResults> results;

    public ArrayList<TVResults> getResults() {
        return results;
    }

    public void setResults(ArrayList<TVResults> results) {
        this.results = results;
    }
}
