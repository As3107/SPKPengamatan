package com.ggpc.spkpengamatan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ggpc.spkpengamatan.Adapter.RidgerSingleAdapter;
import com.ggpc.spkpengamatan.Model.Notes;
import com.ggpc.spkpengamatan.Model.RidgerSingle;
import com.ggpc.spkpengamatan.Model.SPK_SRV;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class InputRidgerSingleActivity extends AppCompatActivity {

    TextView tvNoSpk, tvDate, tvItem, tvLocation, tvLuas, tvStatus;
    TextView tvTotalSPL, tvTotalSTDR, tvTotalSTD, tvTotalMIN, tvTotalMAX, tvTotalRata, tvTotalPencapaian, tvTotalKualitas,
            tvTotalSPL2, tvTotalSTDR2, tvTotalSTD2, tvTotalMIN2, tvTotalMAX2, tvTotalRata2, tvTotalPencapaian2, tvTotalKualitas2;
    TextView tvNotes, tvDataNull;
    EditText etSampel, etPorosGulud, etKukuRidger;
    EditText etNotes;
    RecyclerView rvListRidgerS;
    RoundedImageView ivUpload2;
    MaterialButton btSave, btSend;
    MaterialCardView cvGetNotes;

    private Bitmap image2;
    private final ArrayList<RidgerSingle> list = new ArrayList<>();
    private final ArrayList<String> jarakPoros = new ArrayList<>();
    private final ArrayList<String> kedalamanKuku = new ArrayList<>();
    ProgressDialog progressDialog;
    Long tsLong;
    String ts, pengamat, dataNotes, kit, catatan;
    String sample;
    String poros_gulud;
    String kuku_ridger;
    String date;
    int totalSample = 0, no_sampel = 0;
    double porosGulud, kukuRidger;
    double totalSamplePoros = 0, totalSampleKuku = 0;
    int countSTDRPoros = 0, countSTDRKuku = 0;
    String id_ridger, jarak_poros, kedalaman_kuku, update_no_sampel;
    double update_jarak_poros = 0, update_kedalaman_kuku = 0;

    double minValuePoros = 0, maxValuePoros = 0;
    double minValueKuku = 0, maxValueKuku = 0;
    double meanPoros = 0, meanKuku = 0;
    double pencapaianPoros = 0, pencapaianKuku = 0;

    public static final String data_spk_real = "data_spk_real";
    public static final String DATA_NOTES = "data_notes";

    //private SPK_REAL spk_real;
    SPK_SRV spk_real;
    private Notes notes;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private RidgerSingle ridgerSingle;

    private static final String SHARED_PREF_NAME = "FCMSharedPref";
    public static final String sample_pref = "sample";

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_ridger_single);

        //Setting Toolbar
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.ridger_single);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        //TextView Header  SPK
        tvNoSpk = findViewById(R.id.tv_no_spk);
        tvDate = findViewById(R.id.tv_date);
        tvItem = findViewById(R.id.tv_item);
        tvStatus = findViewById(R.id.tv_status);
        tvLuas = findViewById(R.id.tv_area);
        tvLocation = findViewById(R.id.tv_location);

        //EditText Input
        etSampel = findViewById(R.id.et_sample);
        etPorosGulud = findViewById(R.id.et_poros_gulud);
        etKukuRidger = findViewById(R.id.et_kuku_ridger);
        etNotes = findViewById(R.id.et_notes);
        ivUpload2 = findViewById(R.id.iv_upload2);

        //ReportPoros
        rvListRidgerS = findViewById(R.id.rv_list_ridger_single);
        tvTotalSPL = findViewById(R.id.tv_total_spl);
        tvTotalSTDR = findViewById(R.id.tv_total_stdr);
        tvTotalSTD = findViewById(R.id.tv_total_std);
        tvTotalMIN = findViewById(R.id.tv_total_min);
        tvTotalMAX = findViewById(R.id.tv_total_max);
        tvTotalRata = findViewById(R.id.tv_total_rata);
        tvTotalPencapaian = findViewById(R.id.tv_total_pencapaian);
        tvTotalKualitas = findViewById(R.id.tv_total_pkualitas);
        //ReportKuku
        tvTotalSPL2 = findViewById(R.id.tv_total_spl2);
        tvTotalSTDR2 = findViewById(R.id.tv_total_stdr2);
        tvTotalSTD2 = findViewById(R.id.tv_total_std2);
        tvTotalMIN2 = findViewById(R.id.tv_total_min2);
        tvTotalMAX2 = findViewById(R.id.tv_total_max2);
        tvTotalRata2 = findViewById(R.id.tv_total_rata2);
        tvTotalPencapaian2 = findViewById(R.id.tv_total_pencapaian2);

        tvNotes = findViewById(R.id.tv_notes);
        cvGetNotes = findViewById(R.id.cv_get_note);
        tvDataNull = findViewById(R.id.tv_data_null);

        btSave = findViewById(R.id.bt_save);
        btSend = findViewById(R.id.bt_send);

        //Get Data
        tsLong = System.currentTimeMillis() / 1000;
        ts = tsLong.toString();
        spk_real = getIntent().getParcelableExtra(data_spk_real);
        pengamat = SharedPrefManager.getInstance(this).getDataTK().getNama();
        dataNotes = getIntent().getStringExtra(DATA_NOTES);
        kit = SharedPrefManager.getInstance(this).getUserLogin().getkit();
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
        String inputDateStr = spk_real.getTanggal_SPK();
        Date date = null;
        try {
            date = inputFormat.parse(inputDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outputDateStr = outputFormat.format(date);

        //Output SPK HEADER
        tvNoSpk.setText(spk_real.getSPK_ID());
        tvDate.setText(outputDateStr);
        tvItem.setText(spk_real.getItem());
        tvStatus.setText(spk_real.getSPK_Status());
        tvLocation.setText(spk_real.getLokasi());

        if (dataNotes != null && dataNotes.equals("true")) {
            cvGetNotes.setVisibility(View.VISIBLE);
        } else {
            cvGetNotes.setVisibility(View.GONE);
        }

        btSave.setOnClickListener(v -> validateInput());

        ivUpload2.setOnClickListener(v -> {
            selectImage(2);

        });

        btSend.setOnClickListener(v -> {
            catatan = etNotes.getText().toString();

            if ((list.size()) < sharedPreferences.getInt(sample_pref, 0)) {
                Toast.makeText(this, "Total Data Kurang dari Sampel", Toast.LENGTH_SHORT).show();
            } else {
                confirmSend();
            }

        });

        etSampel.setText(String.valueOf(sharedPreferences.getInt(sample_pref, 0)));
        getNotes();
        getDataRidgerS();

    }

    private void confirmSend() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Konfirmasi Data");
        builder.setMessage("Apakah Anda yakin semua data pengamatan sudah LENGKAP dan akan mengimkan ke MANDOR ?");
        builder.setPositiveButton("Ya", (dialog, which) -> {
            if (!(catatan.matches(""))) {
                sendNotes();
                updateStatus();
            } else {
                updateStatus();
            }

        });
        builder.setNegativeButton("Tidak", (dialog, which) -> {
            dialog.cancel();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void confirmDelete(String idRidger) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Apakah Anda Yakin Ingin Menghapus Data Ini ?");
        alertDialog.setPositiveButton("Ya", (dialog, which) -> deleteData(idRidger));
        alertDialog.setNegativeButton("Tidak", (dialog, which) -> dialog.cancel());

        AlertDialog ad = alertDialog.create();
        ad.show();

    }

    private void deleteData(String idRidger) {

        progressDialog = ProgressDialog.show(this, null, null, true);
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.item_progres_dialog);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.DELETE_DATA_RIDGERS_SRV,
                response -> {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("message").equalsIgnoreCase("true")) {
                            Toast.makeText(this, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                        } else {
                            Toast.makeText(this, "Data Gagal Dihapus", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }, error -> {
            progressDialog.dismiss();
            Toast.makeText(this, "Koneksi Error", Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("ID", idRidger);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void validateInput() {
        sample = etSampel.getText().toString();
        poros_gulud = etPorosGulud.getText().toString();
        kuku_ridger = etKukuRidger.getText().toString();
        totalSample = Integer.parseInt(sample);
        editor.putInt(sample_pref, totalSample);
        editor.apply();

        if ((list.size()==0)){
            no_sampel = 1;
        } else {
            no_sampel = list.size()+1;
        }

       if (!(sample.matches("") || poros_gulud.matches("") || kuku_ridger.matches(""))){
            porosGulud = Double.parseDouble(poros_gulud);
            kukuRidger = Double.parseDouble(kuku_ridger);
        }

        if (sample.matches("") || poros_gulud.matches("") || kuku_ridger.matches("")){
            Toast.makeText(this, "Input Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
        } else if ((porosGulud > 100) || (kukuRidger > 100)) {
            Toast.makeText(this, "Input Tidak Boleh Lebih Dari 100", Toast.LENGTH_SHORT).show();
        } else if ((porosGulud < 10) || (kukuRidger < 10)) {
            Toast.makeText(this, "Input Tidak Boleh Kurang Dari 10", Toast.LENGTH_SHORT).show();
        } else if ((list.size()) == sharedPreferences.getInt(sample_pref, 0)) {
            Toast.makeText(this, "Input Melebihi Total Sampel", Toast.LENGTH_SHORT).show();
        } else {
            saveDataChopper();
        }
    }

    private void saveDataChopper() {

        progressDialog = ProgressDialog.show(this, null, null, true);
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.item_progres_dialog);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.show();

        Calendar calendar = Calendar.getInstance();
        date = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.SEND_RIDGERS_URL_SRV,
                response -> {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("message").equalsIgnoreCase(String.valueOf(true))) {
                            Toast.makeText(this, "Berhasil Terkirim", Toast.LENGTH_SHORT).show();

                            if (image2 != null) {
                                sendImages();
                            }
                            finish();
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                        } else {
                            Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
            progressDialog.dismiss();
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("SPK_ID", spk_real.getSPK_ID());
                params.put("Item_No", spk_real.getItem());
                params.put("Tanggal_SPK", date);
                params.put("KIT", spk_real.getPengamat());
                params.put("Pengamat", SharedPrefManager.getInstance(InputRidgerSingleActivity.this).getDataTK().getNama());
                params.put("Lokasi", spk_real.getLokasi());
                params.put("No_Sampel", String.valueOf(no_sampel));
                DecimalFormat df = new DecimalFormat("##.##");
                params.put("Jarak_Poros", df.format(porosGulud));
                params.put("Kedalaman_Kuku", df.format(kukuRidger));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void sendImages() {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, EndPoints.SEND_IMAGES,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(new String(response.data));

                        if (obj.getString("data").equalsIgnoreCase("succes")) {

                            Toast.makeText(this, "Gambar Berhasil Diupload", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(this, "Gambar Gagal Diupload", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show()) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", ridgerSingle.getId() + 1);
                params.put("no_spk", spk_real.getSPK_ID());
                params.put("plot", String.valueOf(no_sampel));
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                params.put("image", new DataPart(ridgerSingle.getId() + 1 + ".jpg", getFileDataFromDrawable(image2)));
                return params;
            }
        };
        Volley.newRequestQueue(this).add(volleyMultipartRequest)
                .setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void updateStatus() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.UPDATE_STATUS_SPK_SRV,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getString("message").equalsIgnoreCase(String.valueOf(true))) {
                            Toast.makeText(this, "Data Berhasil Dikirim", Toast.LENGTH_SHORT).show();
                            sendNotifMandor();
                            editor.remove(sample_pref);
                            editor.apply();
                            finish();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },

                error -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("Item_No", spk_real.getItem());
                params.put("SPK_ID", spk_real.getSPK_ID());
                params.put("SPK_Status", "ON_MANDOR");
                params.put("Tanggal_Realisasi", "date");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void updateDatRidgerSR() {
        progressDialog = ProgressDialog.show(this, null, null, true);
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.item_progres_dialog);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.show();

        Calendar calendar = Calendar.getInstance();

        date = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.UPDATE_DATA_RIDGERS_SRV,
                response -> {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("message").equalsIgnoreCase(String.valueOf(true))) {
                            Toast.makeText(this, "Data Berhasil Diubah", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                        } else {
                            Toast.makeText(this, "Data Gagal Diubah", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
            progressDialog.dismiss();
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("ID", id_ridger);
                params.put("No_Sampel", update_no_sampel);
                params.put("Jarak_Poros", String.valueOf(update_jarak_poros));
                params.put("Kedalaman_Kuku", String.valueOf(update_kedalaman_kuku));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void sendNotes() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.SET_NOTES_URL,
                response -> {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("message").equalsIgnoreCase(String.valueOf(true))) {
                            Toast.makeText(this, "Catatan Berhasil Dikirim", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(this, "Catatan Gagal Dikirim", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", spk_real.getSPK_ID());
                params.put("indeks", spk_real.getMandor_Operator());
                params.put("no_spk", spk_real.getSPK_ID());
                params.put("catatan", catatan);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getNotes() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.GET_NOTES_URL,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);

                                notes = new Notes(
                                        data.getString("id"),
                                        data.getString("indeks"),
                                        data.getString("no_spk"),
                                        data.getString("catatan")
                                );
                            }
                            tvNotes.setText(notes.getCatatan());
                        } else {
                            tvNotes.setText(R.string.data_null);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", spk_real.getSPK_ID());
                params.put("indeks", kit);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void sendNotifMandor() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.SEND_NOTIF_MANDOR,
                response -> {
                    Toast.makeText(this, "Notifikasi Terkirim", Toast.LENGTH_SHORT).show();
                    finish();
                },
                error -> {

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("title", spk_real.getPengamat());
                params.put("message", "Laporan Pengamatan " + spk_real.getDeskripsi());
                params.put("index", spk_real.getMandor_Operator());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @SuppressLint("SetTextI18n")
    private void getDataRidgerS() {
        list.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.GET_RIDGERS_URL_SRV,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);

                                ridgerSingle = new RidgerSingle(
                                        data.getString("ID"),
                                        data.getString("No_Sampel"),
                                        data.getString("SPK_ID"),
                                        data.getString("Tanggal_SPK"),
                                        data.getString("KIT"),
                                        data.getString("Pengamat"),
                                        data.getString("Lokasi"),
                                        data.getString("Jarak_Poros"),
                                        data.getString("Kedalaman_Kuku")
                                );
                                list.add(ridgerSingle);

                                totalSamplePoros += Double.parseDouble(ridgerSingle.getPoros_gulud());
                                totalSampleKuku += Double.parseDouble(ridgerSingle.getKuku_ridger());

                                //Poros Gulud
                                jarakPoros.add(ridgerSingle.getPoros_gulud());
                                if (minValuePoros==0){
                                    minValuePoros = Double.parseDouble(jarakPoros.get(0));
                                }
                                if (Double.parseDouble(jarakPoros.get(i))>48 && Double.parseDouble(jarakPoros.get(i))<52) {
                                    countSTDRPoros++;
                                }
                                if(Double.parseDouble(jarakPoros.get(i)) < minValuePoros){
                                    minValuePoros = Double.parseDouble(jarakPoros.get(i));
                                }
                                if(Double.parseDouble(jarakPoros.get(i)) > maxValuePoros){
                                    maxValuePoros = Double.parseDouble(jarakPoros.get(i));
                                }

                                //Kedalaman Kuku
                                kedalamanKuku.add(ridgerSingle.getKuku_ridger());
                                if (minValueKuku==0){
                                    minValueKuku = Double.parseDouble(kedalamanKuku.get(0));
                                }
                                if (Double.parseDouble(kedalamanKuku.get(i))>15 && Double.parseDouble(kedalamanKuku.get(i))<20) {
                                    countSTDRKuku++;
                                }
                                if(Double.parseDouble(kedalamanKuku.get(i)) < minValueKuku){
                                    minValueKuku = Double.parseDouble(kedalamanKuku.get(i));
                                }
                                if(Double.parseDouble(kedalamanKuku.get(i)) > maxValueKuku){
                                    maxValueKuku = Double.parseDouble(kedalamanKuku.get(i));
                                }

                            }

                            pencapaianPoros = (Double.parseDouble(String.valueOf(countSTDRPoros))/jarakPoros.size())*100;
                            pencapaianKuku = (Double.parseDouble(String.valueOf(countSTDRKuku))/kedalamanKuku.size())*100;

                            meanPoros = totalSamplePoros/jarakPoros.size();
                            meanKuku = totalSampleKuku/kedalamanKuku.size();

                            double totalPKualitas = (pencapaianPoros+pencapaianKuku)/2;

                            DecimalFormat df = new DecimalFormat("##.##");
                            //SetText Report Jarak Poros
                            tvTotalSPL.setText(String.valueOf(jarakPoros.size()));
                            tvTotalSTDR.setText(String.valueOf(countSTDRPoros));
                            tvTotalSTD.setText(String.valueOf(jarakPoros.size()-countSTDRPoros));
                            tvTotalMIN.setText(String.valueOf(minValuePoros));
                            tvTotalMAX.setText(String.valueOf(maxValuePoros));
                            tvTotalRata.setText(String.valueOf(df.format(meanPoros)));
                            tvTotalPencapaian.setText(df.format(pencapaianPoros)+"%");

                            //SetText Report Kedalaman Kuku
                            tvTotalSPL2.setText(String.valueOf(kedalamanKuku.size()));
                            tvTotalSTDR2.setText(String.valueOf(countSTDRKuku));
                            tvTotalSTD2.setText(String.valueOf(kedalamanKuku.size()-countSTDRKuku));
                            tvTotalMIN2.setText(String.valueOf(minValueKuku));
                            tvTotalMAX2.setText(String.valueOf(maxValueKuku));
                            tvTotalRata2.setText(String.valueOf(df.format(meanKuku)));
                            tvTotalPencapaian2.setText(df.format(pencapaianKuku)+"%");

                            tvTotalKualitas.setText(df.format(totalPKualitas)+"%");

                            tvDataNull.setVisibility(View.GONE);
                            rvListRidgerS.setVisibility(View.VISIBLE);
                            rvListRidgerS.setLayoutManager(new LinearLayoutManager(InputRidgerSingleActivity.this));
                            rvListRidgerS.setNestedScrollingEnabled(false);
                            rvListRidgerS.setHasFixedSize(false);
                            RidgerSingleAdapter adapter = new RidgerSingleAdapter(InputRidgerSingleActivity.this, list);
                            rvListRidgerS.setAdapter(adapter);
                            adapter.onItemClickCallback(this::editDataRidgerS);
                        } else {
                            tvDataNull.setVisibility(View.VISIBLE);
                            rvListRidgerS.setVisibility(View.GONE);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {

                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("KIT", spk_real.getPengamat());
                //params.put("item", spk_real.getItem());
                params.put("SPK_ID", spk_real.getSPK_ID());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void editDataRidgerS(RidgerSingle ridgerSingle) {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.item_edit_ridger_sr, null);

        EditText etJarakPoros, etKedalamanKuku, etNoSampel;
        MaterialButton btSaves, btDelete;

        etNoSampel = dialogView.findViewById(R.id.et_no_sample);
        etJarakPoros = dialogView.findViewById(R.id.et_poros_gulud);
        etKedalamanKuku = dialogView.findViewById(R.id.et_kuku_ridger);
        btSaves = dialogView.findViewById(R.id.bt_save);
        btDelete = dialogView.findViewById(R.id.bt_delete);

        double porosGulud = Double.parseDouble(ridgerSingle.getPoros_gulud());
        double kedalamanKuku = Double.parseDouble(ridgerSingle.getKuku_ridger());

        Log.d("TAG", "poros : " + porosGulud + " kuku : " + kedalamanKuku);

        DecimalFormat df = new DecimalFormat("##.##");

        etNoSampel.setText(ridgerSingle.getNo_sampel(), TextView.BufferType.EDITABLE);
        etJarakPoros.setText(ridgerSingle.getPoros_gulud(), TextView.BufferType.EDITABLE);
        etKedalamanKuku.setText(ridgerSingle.getKuku_ridger(), TextView.BufferType.EDITABLE);

        btSaves.setOnClickListener(v -> {
            id_ridger = ridgerSingle.getId();
            jarak_poros = etJarakPoros.getText().toString();
            kedalaman_kuku = etKedalamanKuku.getText().toString();
            update_no_sampel = etNoSampel.getText().toString();

            update_jarak_poros = Double.parseDouble(jarak_poros);
            update_kedalaman_kuku = Double.parseDouble(kedalaman_kuku);

            Log.d("TAG", "Poros : " + update_jarak_poros + "Kuku : " + update_kedalaman_kuku);

            if (jarak_poros.matches("") || kedalaman_kuku.matches("")) {
                Toast.makeText(this, "Input Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
            } else if ((update_jarak_poros > 100) || (update_kedalaman_kuku > 100)) {
                Toast.makeText(this, "Input Tidak Boleh Lebih Dari 100", Toast.LENGTH_SHORT).show();
            } else if ((update_jarak_poros < 10) || (update_kedalaman_kuku < 10)) {
                Toast.makeText(this, "Input Tidak Boleh Kurang Dari 10", Toast.LENGTH_SHORT).show();
            } else {
                updateDatRidgerSR();
            }
        });

        btDelete.setOnClickListener(v -> {
            confirmDelete(String.valueOf(ridgerSingle.getId()));
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void selectImage(int intent) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, (dialog, item) -> {

            if (intent == 2) {
                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 20);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 200);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }

        });
        builder.show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {

                case 20:
                    if (resultCode == RESULT_OK && data != null) {
                        image2 = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                        ivUpload2.setImageBitmap(image2);
                    }
                    break;

                case 200:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri imageUri = data.getData();
                        try {
                            image2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                            ivUpload2.setImageBitmap(image2);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

            }
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}