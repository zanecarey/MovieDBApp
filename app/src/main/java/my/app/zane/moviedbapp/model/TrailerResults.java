package my.app.zane.moviedbapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TrailerResults {

    @SerializedName("results")
    @Expose
    private ArrayList<Trailer> results;

    public ArrayList<Trailer> getResults() {
        return results;
    }

    public void setResults(ArrayList<Trailer> results) {
        this.results = results;
    }
}
