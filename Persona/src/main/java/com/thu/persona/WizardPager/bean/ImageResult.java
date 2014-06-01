package com.thu.persona.WizardPager.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by SemonCat on 2014/5/27.
 */
public class ImageResult implements Serializable {

    private static final long serialVersionUID = 2955852200975284155L;
    private String fullUrl;
    private String thumbnailUrl;

    public ImageResult(JSONObject json) {
        try {
            this.fullUrl = json.getString("url");
            this.thumbnailUrl = json.getString("tbUrl");
        } catch (JSONException jsonEx) {
            this.fullUrl = null;
            this.thumbnailUrl = null;
        }
    }

    public String getFullUrl() {
        return this.fullUrl;
    }

    public String getThumbnailUrl() {
        return this.thumbnailUrl;
    }

    public String toString() {
        return this.thumbnailUrl;
    }

    public static ArrayList<ImageResult> fromJSONArray(JSONArray jsonArray) {
        ArrayList<ImageResult> results = new ArrayList<ImageResult>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                results.add(new ImageResult(jsonArray.getJSONObject(i)));
            } catch (JSONException jsonEx) {
                jsonEx.printStackTrace();
            }
        }

        return results;
    }
}
