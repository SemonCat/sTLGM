package com.thu.persona.WizardPager.util;

import android.net.Uri;
import android.util.Log;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.thu.persona.WizardPager.bean.ImageResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by SemonCat on 2014/5/27.
 */
public class GoogleImageSearchHelper {
    public interface OnSearchFinishListener {
        void OnSearchSuccess(List<ImageResult> mData);

        void OnFail(Throwable error);
    }

    public static void SearchImage(final String query, final OnSearchFinishListener mListener) {
        AsyncHttpClient ahClient = new AsyncHttpClient();

        String Url = buildQueryString(query);

        ahClient.get(
                Url,
                new JsonHttpResponseHandler() {
                    public void onSuccess(JSONObject response) {


                        try {


                            JSONObject jsonResponse = response.getJSONObject("responseData");
                            JSONArray jsonImageResults = jsonResponse.getJSONArray("results");

                            List<ImageResult> mData = ImageResult.fromJSONArray(jsonImageResults);

                            if (mListener != null) {
                                mListener.OnSearchSuccess(mData);
                            }

                        } catch (JSONException e) {
                            if (mListener != null) {
                                mListener.OnFail(e);
                            }
                        }

                    }

                    @Override
                    public void onFailure(Throwable e, JSONObject errorResponse) {
                        if (mListener != null) {
                            mListener.OnFail(e);
                        }
                    }
                }
        );
    }

    private static String buildQueryString(String query) {

        StringBuilder queryString = new StringBuilder();
        queryString.append("https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8");

        queryString.append("&q=" + Uri.encode(query));
        return queryString.toString().toLowerCase(Locale.US);
    }


}
