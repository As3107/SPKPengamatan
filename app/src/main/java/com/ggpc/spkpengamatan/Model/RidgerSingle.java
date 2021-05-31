package com.ggpc.spkpengamatan.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class RidgerSingle implements Parcelable {

    private final String id;
    private final String no_sampel;
    private final String no_spk;
    private final String tanggal;
    private final String kit;
    private final String pengamat;
    private final String lokasi;
    private final String poros_gulud;
    private final String kuku_ridger;

    public RidgerSingle(String id, String no_sampel, String no_spk, String tanggal, String kit, String pengamat, String lokasi, String poros_gulud, String kuku_ridger) {
        this.id = id;
        this.no_sampel = no_sampel;
        this.no_spk = no_spk;
        this.tanggal = tanggal;
        this.kit = kit;
        this.pengamat = pengamat;
        this.lokasi = lokasi;
        this.poros_gulud = poros_gulud;
        this.kuku_ridger = kuku_ridger;
    }

    protected RidgerSingle(Parcel in) {
        id = in.readString();
        no_sampel = in.readString();
        no_spk = in.readString();
        tanggal = in.readString();
        kit = in.readString();
        pengamat = in.readString();
        lokasi = in.readString();
        poros_gulud = in.readString();
        kuku_ridger = in.readString();
    }

    public static final Creator<RidgerSingle> CREATOR = new Creator<RidgerSingle>() {
        @Override
        public RidgerSingle createFromParcel(Parcel in) {
            return new RidgerSingle(in);
        }

        @Override
        public RidgerSingle[] newArray(int size) {
            return new RidgerSingle[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getNo_sampel() {
        return no_sampel;
    }

    public String getNo_spk() {
        return no_spk;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getKit() {
        return kit;
    }

    public String getPengamat() {
        return pengamat;
    }

    public String getLokasi() {
        return lokasi;
    }

    public String getPoros_gulud() {
        return poros_gulud;
    }

    public String getKuku_ridger() {
        return kuku_ridger;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(no_sampel);
        dest.writeString(no_spk);
        dest.writeString(tanggal);
        dest.writeString(kit);
        dest.writeString(pengamat);
        dest.writeString(lokasi);
        dest.writeString(poros_gulud);
        dest.writeString(kuku_ridger);
    }
}
