package my.app.zane.moviedbapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Credits {

    @SerializedName("cast")
    @Expose
    private ArrayList<CreditResults> creditResults;

    public ArrayList<CreditResults> getCreditResults() {
        return creditResults;
    }

    public void setCredits(ArrayList<CreditResults> credits) {
        this.creditResults = credits;
    }
}
