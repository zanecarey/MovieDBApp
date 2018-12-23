package com.example.zane.moviedbapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActorInfo {

    @SerializedName("birthday")
    @Expose
    private String birthday;

    @SerializedName("deathday")
    @Expose
    private String deathday;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("biography")
    @Expose
    private String biography;

    @SerializedName("place_of_birth")
    @Expose
    private String place_of_birth;

    @SerializedName("profile_path")
    @Expose
    private String profile_path;

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getDeathday() {
        return deathday;
    }

    public void setDeathday(String deathday) {
        this.deathday = deathday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getPlace_of_birth() {
        return place_of_birth;
    }

    public void setPlace_of_birth(String place_of_birth) {
        this.place_of_birth = place_of_birth;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
    }
}
