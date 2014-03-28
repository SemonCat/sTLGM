package com.thu.stlgm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.facebook.model.GraphObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by SemonCat on 2014/3/25.
 */
public class Photo implements Parcelable {

    private long id;

    private String caption;

    private String src_big;

    private int like_count;

    private int comment_count;

    public Photo(long id, int like_count, int comment_count, String caption, String src_big) {
        this.id = id;
        this.caption = caption;
        this.src_big = src_big;
        this.like_count = like_count;
        this.comment_count = comment_count;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getSrc_big() {
        return src_big;
    }

    public void setSrc_big(String src_big) {
        this.src_big = src_big;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public static List<Photo> toPhoto(GraphObject graphObject ){
        if (graphObject != null) {
            HashSet<Photo> mPhotos = new HashSet<Photo>();
            JSONObject jsonObject = graphObject.getInnerJSONObject();

            try {
                JSONArray array = jsonObject.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = (JSONObject) array.get(i);

                    String id = object.get("object_id").toString();

                    String like_count = object.
                            getJSONObject("like_info").
                            get("like_count").toString();

                    String comment_count = object.
                            getJSONObject("comment_info").
                            get("comment_count").toString();

                    String caption = object.get("caption").toString();

                    String src_big = object.get("src_big").toString();

                    mPhotos.add(new Photo(Long.valueOf(id),Integer.valueOf(like_count),Integer.valueOf(comment_count),caption,src_big));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

            return new ArrayList<Photo>(mPhotos);
        }

        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Photo)) return false;

        Photo photo = (Photo) o;

        if (id != photo.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", caption='" + caption + '\'' +
                ", src_big='" + src_big + '\'' +
                ", like_count=" + like_count +
                ", comment_count=" + comment_count +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.caption);
        dest.writeString(this.src_big);
        dest.writeInt(this.like_count);
        dest.writeInt(this.comment_count);
    }

    private Photo(Parcel in) {
        this.id = in.readLong();
        this.caption = in.readString();
        this.src_big = in.readString();
        this.like_count = in.readInt();
        this.comment_count = in.readInt();
    }

    public static Creator<Photo> CREATOR = new Creator<Photo>() {
        public Photo createFromParcel(Parcel source) {
            return new Photo(source);
        }

        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };
}
