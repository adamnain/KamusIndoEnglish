package io.github.adamnain.kamusindoenglish.model;

import android.os.Parcel;
import android.os.Parcelable;

public class KamusModel implements Parcelable {
    private int id;
    private String kata;
    private String arti;

    public KamusModel(){

    }

    public KamusModel(String kata, String arti) {
        this.kata = kata;
        this.arti = arti;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKata() {
        return kata;
    }

    public void setKata(String kata) {
        this.kata = kata;
    }

    public String getArti() {
        return arti;
    }

    public void setArti(String arti) {
        this.arti = arti;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.kata);
        dest.writeString(this.arti);
    }

    protected KamusModel(Parcel in) {
        this.id = in.readInt();
        this.kata = in.readString();
        this.arti = in.readString();
    }

    public static final Parcelable.Creator<KamusModel> CREATOR = new Parcelable.Creator<KamusModel>() {
        @Override
        public KamusModel createFromParcel(Parcel source) {
            return new KamusModel(source);
        }

        @Override
        public KamusModel[] newArray(int size) {
            return new KamusModel[size];
        }
    };
}
