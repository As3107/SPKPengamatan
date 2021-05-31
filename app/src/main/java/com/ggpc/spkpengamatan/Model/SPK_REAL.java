package com.ggpc.spkpengamatan.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class SPK_REAL implements Parcelable {

    private String ID;
    private String SPK_ID;
    private String Tanggal_SPK;
    private String Tanggal_Realisasi;
    private String Item;
    private String Kode_Aktivitas;
    private String Deskripsi;
    private String Lokasi;
    private String Kebun;
    private String Wilayah;
    private String Pengamat;
    private String Nama_Pengamat;
    private String Mandor_Operator;
    private String Kasie;
    private String Bagian;
    private String SPK_Status;

    public SPK_REAL(String ID, String SPK_ID, String tanggal_SPK, String tanggal_Realisasi, String item, String kode_Aktivitas,
                    String deskripsi, String lokasi, String kebun, String wilayah, String pengamat, String nama_Pengamat, String mandor_Operator, String kasie, String bagian, String SPK_Status) {
        this.ID = ID;
        this.SPK_ID = SPK_ID;
        Tanggal_SPK = tanggal_SPK;
        Tanggal_Realisasi = tanggal_Realisasi;
        Item = item;
        Kode_Aktivitas = kode_Aktivitas;
        Deskripsi = deskripsi;
        Lokasi = lokasi;
        Kebun = kebun;
        Wilayah = wilayah;
        Pengamat = pengamat;
        Nama_Pengamat = nama_Pengamat;
        Mandor_Operator = mandor_Operator;
        Kasie = kasie;
        Bagian = bagian;
        this.SPK_Status = SPK_Status;
    }

    protected SPK_REAL(Parcel in) {
        ID = in.readString();
        SPK_ID = in.readString();
        Tanggal_SPK = in.readString();
        Tanggal_Realisasi = in.readString();
        Item = in.readString();
        Kode_Aktivitas = in.readString();
        Deskripsi = in.readString();
        Lokasi = in.readString();
        Kebun = in.readString();
        Wilayah = in.readString();
        Pengamat = in.readString();
        Nama_Pengamat = in.readString();
        Mandor_Operator = in.readString();
        Kasie = in.readString();
        Bagian = in.readString();
        SPK_Status = in.readString();
    }

    public static final Creator<SPK_REAL> CREATOR = new Creator<SPK_REAL>() {
        @Override
        public SPK_REAL createFromParcel(Parcel in) {
            return new SPK_REAL(in);
        }

        @Override
        public SPK_REAL[] newArray(int size) {
            return new SPK_REAL[size];
        }
    };

    public String getID() {
        return ID;
    }

    public String getSPK_ID() {
        return SPK_ID;
    }

    public String getTanggal_SPK() {
        return Tanggal_SPK;
    }

    public String getTanggal_Realisasi() {
        return Tanggal_Realisasi;
    }

    public String getItem() {
        return Item;
    }

    public String getKode_Aktivitas() {
        return Kode_Aktivitas;
    }

    public String getDeskripsi() {
        return Deskripsi;
    }

    public String getLokasi() {
        return Lokasi;
    }

    public String getKebun() {
        return Kebun;
    }

    public String getWilayah() {
        return Wilayah;
    }

    public String getPengamat() {
        return Pengamat;
    }

    public String getNama_Pengamat() {
        return Nama_Pengamat;
    }

    public String getMandor_Operator() {
        return Mandor_Operator;
    }

    public String getKasie() {
        return Kasie;
    }

    public String getBagian() {
        return Bagian;
    }

    public String getSPK_Status() {
        return SPK_Status;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(SPK_ID);
        dest.writeString(Tanggal_SPK);
        dest.writeString(Tanggal_Realisasi);
        dest.writeString(Item);
        dest.writeString(Kode_Aktivitas);
        dest.writeString(Deskripsi);
        dest.writeString(Lokasi);
        dest.writeString(Kebun);
        dest.writeString(Wilayah);
        dest.writeString(Pengamat);
        dest.writeString(Nama_Pengamat);
        dest.writeString(Mandor_Operator);
        dest.writeString(Kasie);
        dest.writeString(Bagian);
        dest.writeString(SPK_Status);
    }
}
