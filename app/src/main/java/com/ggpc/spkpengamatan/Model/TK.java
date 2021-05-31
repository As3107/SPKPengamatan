package com.ggpc.spkpengamatan.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class TK implements Parcelable {
    private String kit;
    private String nama;
    private String mandor;
    private String value =null;
    private String name = null;

    public TK(String kit, String nama, String mandor) {
        this.kit = kit;
        this.nama = nama;
        this.mandor = mandor;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected TK(Parcel in) {
        kit = in.readString();
        nama = in.readString();
        mandor = in.readString();
    }


    public static final Creator<TK> CREATOR = new Creator<TK>() {
        @Override
        public TK createFromParcel(Parcel in) {
            return new TK(in);
        }

        @Override
        public TK[] newArray(int size) {
            return new TK[size];
        }
    };

    public String getKit() {
        return kit;
    }

    public String getNama() {
        return nama;
    }

    public String getMandor() {
        return mandor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(kit);
        dest.writeString(nama);
        dest.writeString(mandor);
    }
}
