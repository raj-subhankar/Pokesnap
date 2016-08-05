package com.yellowsoft.subhankar.pokesnap;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by subhankar on 7/17/2016.
 */

public class FeedResult {

    @SerializedName("results")
    private ArrayList<Post> results;

    public ArrayList<Post> getResults() {
        return results;
    }

    public void setResults(ArrayList<Post> results) {
        this.results = results;
    }
}
