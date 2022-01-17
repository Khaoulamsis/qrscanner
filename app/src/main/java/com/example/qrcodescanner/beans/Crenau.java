package com.example.qrcodescanner.beans;

import android.os.Parcel;
import android.os.Parcelable;

public class Crenau implements Parcelable {
    private String _id;
    private String hrdebut;
    private String hrfin;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getHrdebut() {
        return hrdebut;
    }

    public void setHrdebut(String hrdebut) {
        this.hrdebut = hrdebut;
    }

    public String getHrfin() {
        return hrfin;
    }

    public void setHrfin(String hrfin) {
        this.hrfin = hrfin;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    public Crenau(Parcel in) {
        _id = in.readString();
        hrdebut = in.readString();
        hrfin = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(_id);
        dest.writeString(hrdebut);
        dest.writeString(hrfin);
    }

    public Crenau(String _id, String hrdebut, String hrfin) {
        this._id = _id;
        this.hrdebut = hrdebut;
        this.hrfin = hrfin;
    }

    @Override
    public String toString() {
        return "Crenau{" +
                "_id='" + _id + '\'' +
                ", hrdebut='" + hrdebut + '\'' +
                ", hrfin='" + hrfin + '\'' +
                '}';
    }
}
