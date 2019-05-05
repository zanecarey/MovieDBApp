package my.app.zane.moviedbapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CastResults {

    @SerializedName("cast")
    @Expose
    private ArrayList<Cast> cast;

    public ArrayList<Cast> getCast() {
        return cast;
    }

    public void setCast(ArrayList<Cast> cast) {
        this.cast = cast;
    }
}
