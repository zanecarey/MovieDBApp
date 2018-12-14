package com.example.zane.moviedbapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NameResults {

    @SerializedName("results")
    @Expose
    private ArrayList<SearchName> results;

    public ArrayList<SearchName> getResults() {
        return results;
    }

    public void setResults(ArrayList<SearchName> results) {
        this.results = results;
    }
}
