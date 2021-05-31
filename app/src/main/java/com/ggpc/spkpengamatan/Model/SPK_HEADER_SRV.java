package com.ggpc.spkpengamatan.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class SPK_HEADER_SRV implements Parcelable {

    private final String SPK_ID;
    private final String Tanggal_SPK;

    public SPK_HEADER_SRV(String SPK_ID, String tanggal_SPK) {
        this.SPK_ID = SPK_ID;
        Tanggal_SPK = tanggal_SPK;
    }

    public String getSPK_ID() {
        return SPK_ID;
    }

    public String getTanggal_SPK() {
        return Tanggal_SPK;
    }

    public static Creator<SPK_HEADER_SRV> getCREATOR() {
        return CREATOR;
    }

    protected SPK_HEADER_SRV(Parcel in) {
        SPK_ID = in.readString();
        Tanggal_SPK = in.readString();
    }

    public static final Creator<SPK_HEADER_SRV> CREATOR = new Creator<SPK_HEADER_SRV>() {
        @Override
        public SPK_HEADER_SRV createFromParcel(Parcel in) {
            return new SPK_HEADER_SRV(in);
        }

        @Override
        public SPK_HEADER_SRV[] newArray(int size) {
            return new SPK_HEADER_SRV[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(SPK_ID);
        dest.writeString(Tanggal_SPK);
    }
}
