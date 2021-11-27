package com.ijul.projekpembunuh;


import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;


public class makanbang implements Parcelable {

    private String rating;
    private String release;
    private int title;

    public makanbang(int title, String rating, String release) {
        this.rating = rating;
        this.release = release;
        this.title = title;
    }

    public Integer getTitle() {
        return title;
    }

    public void setTitle(Integer title) {
        this.title = title;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.rating);
        dest.writeString(this.release);
    }

    public void readFromParcel(Parcel source) {
        this.rating = source.readString();
        this.release = source.readString();
    }

    protected makanbang(Parcel in) {
        this.rating = in.readString();
        this.release = in.readString();
    }

    public static final Creator<makanbang> CREATOR = new Creator<makanbang>() {
        @Override
        public makanbang createFromParcel(Parcel source) {
            return new makanbang(source);
        }

        @Override
        public makanbang[] newArray(int size) {
            return new makanbang[size];
        }
    };
}


