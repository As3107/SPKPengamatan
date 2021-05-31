package com.ggpc.spkpengamatan;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ggpc.spkpengamatan.Adapter.ChopperAdapter;
import com.ggpc.spkpengamatan.Model.Chopper;
import com.ggpc.spkpengamatan.Model.Notes;
import com.ggpc.spkpengamatan.Model.SPK;
import com.ggpc.spkpengamatan.Model.SPK_REAL;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import android.provider.Settings.Secure;

public class InputChopperActivity extends AppCompatActivity {

    TextView tvNoSpk, tvDate, tvItem, tvLocation, tvLuas, tvStatus;
    TextView tvTotalBt, tvTotalTh, tvTotalAr, tvTotalSample, tvTotalAchievement, tvDataNull, tvNotes;
    EditText etSampel, etPlot, etTh, etBt, etAr;
    EditText etNotes;
    RoundedImageView ivUpload2;
    MaterialButton btSave, btSend;
    MaterialCardView cvGetNotes;
    RecyclerView rvDataChopper;
    private Bitmap image2;

    private final List<Chopper> list = new ArrayList<>();
    private final ArrayList<String> plotArray = new ArrayList<>();
    ProgressDialog progressDialog;
    Long tsLong;
    String kit, ts, plot, bt, th, ar, pengamat, date, no_spk, id, mandor, dataNotes;
    String updatePlot, updateBt, updateTh, updateAr, updateTotal, catatan, sample;
    int id_chopper;
    float totalBT, totalTH, totalAR;
    double totalAchievement;
    int totalSample = 0, totalPlot = 0, countPlot = 0, countBefore = 0;

    public static final String DATA_SPK = "data_spk";
    public static final String data_spk_real = "data_spk_real";
    public static final String DATA_NOTES = "data_notes";

    //private SPK_REAL spk_real;
    private SPK_SRV spk_srv;
    private Notes notes;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Chopper chopper;

    private static final String SHARED_PREF_NAME = "FCMSharedPref";
    public static final String sample_pref = "sample";
    public static final String plot_pref = "plot";


    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_chopper_new);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.chopper);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        ivUpload2 = findViewById(R.id.iv_upload2);
        btSave = findViewById(R.id.bt_save);
        btSend = findViewById(R.id.bt_send);

        //TextView Header  SPK
        tvNoSpk = findViewById(R.id.tv_no_spk);
        tvDate = findViewById(R.id.tv_date);
        tvItem = findViewById(R.id.tv_item);
        tvStatus = findViewById(R.id.tv_status);
        tvLuas = findViewById(R.id.tv_area);
        tvLocation = findViewById(R.id.tv_location);

        //EditText Input Data
        etSampel = findViewById(R.id.et_sample);
        etPlot = findViewById(R.id.et_plot);
        etBt = findViewById(R.id.et_bt);
        etTh = findViewById(R.id.et_th);
        etAr = findViewById(R.id.et_ar);

        //TextView Total
        tvTotalBt = findViewById(R.id.tv_total_bt);
        tvTotalTh = findViewById(R.id.tv_total_th);
        tvTotalAr = findViewById(R.id.tv_total_ar);
        tvTotalSample = findViewById(R.id.tv_total_sample);
        tvTotalAchievement = findViewById(R.id.tv_total_achievement);

        tvNotes = findViewById(R.id.tv_notes);
        cvGetNotes = findViewById(R.id.cv_get_note);
        etNotes = findViewById(R.id.et_notes);
        rvDataChopper = findViewById(R.id.rv_list_data);
        tvDataNull = findViewById(R.id.tv_data_null);

        //Get Data
        tsLong = System.currentTimeMillis() / 1000;
        ts = tsLong.toString();
        //spk_real = getIntent().getParcelableExtra(data_spk_real);
        spk_srv = getIntent().getParcelableExtra(data_spk_real);
        pengamat = SharedPrefManager.getInstance(this).getDataTK().getNama();
        dataNotes = getIntent().getStringExtra(DATA_NOTES);
        kit = SharedPrefManager.getInstance(InputChopperActivity.this).getUserLogin().getkit();
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
        String inputDateStr = spk_srv.getTanggal_SPK();
        Date date = null;
        try {
            date = inputFormat.parse(inputDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outputDateStr = outputFormat.format(date);

        tvNoSpk.setText(spk_srv.getSPK_ID());
        tvDate.setText(outputDateStr);
        tvItem.setText(spk_srv.getItem());
        tvStatus.setText(spk_srv.getSPK_Status());
        tvLocation.setText(spk_srv.getLokasi());

        if (dataNotes != null && dataNotes.equals("true")) {
            cvGetNotes.setVisibility(View.VISIBLE);
        } else {
            cvGetNotes.setVisibility(View.GONE);
        }

        btSave.setOnClickListener(v -> validateInput());

        ivUpload2.setOnClickListener(v -> {
            selectImage(2);

        });

        getDataChopper();
        getNotes();

        btSend.setOnClickListener(v -> {
            catatan = etNotes.getText().toString();

            if ((list.size() * 10) < sharedPreferences.getInt(sample_pref, 0)) {
                Toast.makeText(this, "Total Data Kurang dari Sampel", Toast.LENGTH_SHORT).show();
            } else {
                confirmSend();
            }

            /*if (totalBT != 0) {
                confirmSend();
            } else {
                Toast.makeText(this, "Data Tidak Ada", Toast.LENGTH_SHORT).show();
            }*/
        });

        etSampel.setText(String.valueOf(sharedPreferences.getInt(sample_pref, 0)));
        etPlot.setText(String.valueOf(sharedPreferences.getInt(plot_pref, 0)));

    }

    private void confirmSend() {
        AlertDialog.Builder builder = new AlertDialog.Builder(InputChopperActivity.this);
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

    private void confirmDelete(String idChopper) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Apakah Anda Yakin Ingin Menghapus Data Ini ?");
        alertDialog.setPositiveButton("Ya", (dialog, which) -> deleteData(idChopper));
        alertDialog.setNegativeButton("Tidak", (dialog, which) -> dialog.cancel());

        AlertDialog ad = alertDialog.create();
        ad.show();

    }

    private void deleteData(String idChopper) {

        progressDialog = ProgressDialog.show(this, null, null, true);
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.item_progres_dialog);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.DELETE_DATA_CHOPPER_SRV,
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
                params.put("ID", idChopper);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void validateInput() {
        sample = etSampel.getText().toString();
        plot = etPlot.getText().toString();
        bt = etBt.getText().toString();
        th = etTh.getText().toString();
        ar = etAr.getText().toString();
        totalSample = Integer.parseInt(sample);
        totalPlot = Integer.parseInt(plot);
        editor.putInt(sample_pref, totalSample);
        editor.putInt(plot_pref, totalPlot);
        editor.apply();

        /*if (list.size()!=0){
            for(int i = 0; i < list.size(); i++){
                if(list.get(i).getPlot().equals(plot)){
                    countPlot++;
                } else {
                    countPlot = 0;
                }
            }
        } else {
            countPlot = 0;
        }*/

        for(int i = 0; i < plotArray.size(); i++){
            if(plotArray.get(i).equals(plot)){
                countPlot++;
            }
            if(plotArray.get(i).equals(chopper.getPlot())){
                countBefore++;
            }

            /*else {
                countPlot=0;
            }*/
            //Toast.makeText(this, "array : "+ plotArray.get(i), Toast.LENGTH_SHORT).show();
        }

        /*else if ((list.size() * 10) == sharedPreferences.getInt(sample_pref, 0)) {
            Toast.makeText(this, "Input Melebihi Total Sampel", Toast.LENGTH_SHORT).show();
        }*/
        if (sample.matches("") || plot.matches("") || bt.matches("") || th.matches("") || ar.matches("")) {
            Toast.makeText(this, "Input Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
        } else if ((Integer.parseInt(bt) > 100) || (Integer.parseInt(th) > 100) || (Integer.parseInt(ar) > 100)) {
            Toast.makeText(this, "Input Tidak Boleh Lebih Dari 100", Toast.LENGTH_SHORT).show();
        } else if ((Integer.parseInt(bt) < 10) || (Integer.parseInt(th) < 10) || (Integer.parseInt(ar) < 10)) {
            Toast.makeText(this, "Input Tidak Boleh Kurang Dari 10", Toast.LENGTH_SHORT).show();
        } else if (countPlot == sharedPreferences.getInt(sample_pref, 0)) {
            Toast.makeText(this, "Input Melebihi Sampel PerPlot", Toast.LENGTH_SHORT).show();
            countPlot = 0;
        } else if (countBefore < sharedPreferences.getInt(sample_pref, 0) && !chopper.getPlot().equals(plot)) {
            Toast.makeText(this, "Plot Sebelumnya Kurang Data Sampel", Toast.LENGTH_SHORT).show();
            countBefore = 0;
        } else {
            Toast.makeText(this, "Total Plot "+chopper.getPlot()+" : "+countBefore, Toast.LENGTH_SHORT).show();
            countPlot = 0;
            countBefore = 0;
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

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.SEND_CHOPPER_URL_SRV,
                response -> {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("message").equalsIgnoreCase(String.valueOf(true))) {
                            Toast.makeText(this, "Berhasil Terkirim", Toast.LENGTH_SHORT).show();
                            //countPlot = 0;
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
                error -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("SPK_ID", spk_srv.getSPK_ID());
                params.put("Item_No", spk_srv.getItem());
                params.put("Tanggal_SPK", date);
                params.put("KIT", spk_srv.getPengamat());
                params.put("Pengamat", SharedPrefManager.getInstance(InputChopperActivity.this).getDataTK().getNama());
                params.put("Lokasi", spk_srv.getLokasi());
                params.put("Plot", plot);
                params.put("BT", bt);
                params.put("TH", th);
                params.put("AR", ar);
                /*int total = Integer.parseInt(bt) + Integer.parseInt(th) + Integer.parseInt(ar);
                params.put("total", String.valueOf(total));*/
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(InputChopperActivity.this);
        requestQueue.add(stringRequest);
    }

    private void sendImages() {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, EndPoints.SEND_IMAGES,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(new String(response.data));

                        if (obj.getString("data").equalsIgnoreCase("succes")) {

                            Toast.makeText(InputChopperActivity.this, "Gambar Berhasil Diupload", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(InputChopperActivity.this, "Gambar Gagal Diupload", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show()) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(chopper.getId() + 1));
                params.put("no_spk", spk_srv.getSPK_ID());
                params.put("plot", plot);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                params.put("image", new DataPart(chopper.getId() + 1 + ".jpg", getFileDataFromDrawable(image2)));
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
                            editor.remove(plot_pref);
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
                params.put("Item_No", spk_srv.getItem());
                params.put("SPK_ID", spk_srv.getSPK_ID());
                params.put("Tanggal_Realisasi", date);
                params.put("SPK_Status", "ON_MANDOR");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void updateDatAChopper() {
        progressDialog = ProgressDialog.show(this, null, null, true);
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.item_progres_dialog);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.show();

        Calendar calendar = Calendar.getInstance();

        date = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.UPDATE_DATA_CHOPPER_SRV,
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
                error -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("ID", String.valueOf(id_chopper));
                params.put("Plot", updatePlot);
                params.put("BT", updateBt);
                params.put("TH", updateTh);
                params.put("AR", updateAr);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(InputChopperActivity.this);
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
                params.put("id", spk_srv.getSPK_ID());
                params.put("indeks", spk_srv.getMandor_Operator());
                params.put("no_spk", spk_srv.getSPK_ID());
                params.put("catatan", catatan);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(InputChopperActivity.this);
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
                params.put("id", spk_srv.getSPK_ID());
                params.put("indeks", kit);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(InputChopperActivity.this);
        requestQueue.add(stringRequest);
    }

    private void sendNotifMandor() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.SEND_NOTIF_MANDOR,
                response -> {
                    Toast.makeText(InputChopperActivity.this, "Notifikasi Terkirim", Toast.LENGTH_SHORT).show();
                    finish();
                },
                error -> {

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("title", spk_srv.getPengamat());
                params.put("message", "Laporan Pengamatan " + spk_srv.getDeskripsi());
                params.put("index", spk_srv.getMandor_Operator());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(this));
        requestQueue.add(stringRequest);
    }

    @SuppressLint("SetTextI18n")
    private void getDataChopper() {
        list.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.GET_CHOPPER_URL_SRV,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);

                                chopper = new Chopper(
                                        data.getInt("ID"),
                                        data.getString("SPK_ID"),
                                        data.getString("Item_No"),
                                        data.getString("Tanggal_SPK"),
                                        data.getString("KIT"),
                                        data.getString("Pengamat"),
                                        data.getString("Lokasi"),
                                        data.getString("Plot"),
                                        data.getString("BT"),
                                        data.getString("TH"),
                                        data.getString("AR")
                                );
                                list.add(chopper);
                                plotArray.add(chopper.getPlot());
                                totalBT += Float.parseFloat(chopper.getBt()) / 10;
                                totalTH += Float.parseFloat(chopper.getTh()) / 10;
                                totalAR += Float.parseFloat(chopper.getAr()) / 10;
                                totalSample = list.size() * 10;

                            }
                            totalAchievement = (totalTH / totalSample * 40) + (totalBT / totalSample * 40) + (totalAR / totalSample * 20);
                            DecimalFormat df = new DecimalFormat("##.##");
                            tvTotalBt.setText(String.valueOf(df.format(totalBT)));
                            tvTotalTh.setText(String.valueOf(df.format(totalTH)));
                            tvTotalAr.setText(String.valueOf(df.format(totalAR)));
                            tvTotalSample.setText(String.valueOf(totalSample));
                            if (totalAchievement < 85) {
                                tvTotalAchievement.setTextColor(ContextCompat.getColor(this, R.color.red));
                            }
                            tvTotalAchievement.setText(df.format(totalAchievement) + " %");

                            tvDataNull.setVisibility(View.GONE);
                            rvDataChopper.setVisibility(View.VISIBLE);
                            rvDataChopper.setLayoutManager(new LinearLayoutManager(InputChopperActivity.this));
                            rvDataChopper.setNestedScrollingEnabled(false);
                            rvDataChopper.setHasFixedSize(false);
                            ChopperAdapter adapter = new ChopperAdapter(InputChopperActivity.this, list);
                            rvDataChopper.setAdapter(adapter);
                            adapter.onItemClickCallback(this::editDataChopper);
                        } else {
                            tvDataNull.setVisibility(View.VISIBLE);
                            rvDataChopper.setVisibility(View.GONE);
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
                params.put("KIT", spk_srv.getPengamat());
                //params.put("item", spk_srv.getItem());
                params.put("SPK_ID", spk_srv.getSPK_ID());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(InputChopperActivity.this);
        requestQueue.add(stringRequest);
    }

    private void editDataChopper(Chopper choppers) {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.item_edit_chopper, null);

        EditText etPlots, etBts, etThs, etArs;
        MaterialButton btSaves, btDelete;

        etPlots = dialogView.findViewById(R.id.et_plot);
        etBts = dialogView.findViewById(R.id.et_bt);
        etThs = dialogView.findViewById(R.id.et_th);
        etArs = dialogView.findViewById(R.id.et_ar);
        btSaves = dialogView.findViewById(R.id.bt_save);
        btDelete = dialogView.findViewById(R.id.bt_delete);

        etPlots.setText(choppers.getPlot(), TextView.BufferType.EDITABLE);
        etBts.setText(choppers.getBt(), TextView.BufferType.EDITABLE);
        etThs.setText(choppers.getTh(), TextView.BufferType.EDITABLE);
        etArs.setText(choppers.getAr(), TextView.BufferType.EDITABLE);

        btSaves.setOnClickListener(v -> {
            id_chopper = choppers.getId();
            updatePlot = etPlots.getText().toString();
            updateBt = etBts.getText().toString();
            updateTh = etThs.getText().toString();
            updateAr = etArs.getText().toString();
            int total = Integer.parseInt(updateBt) + Integer.parseInt(updateTh) + Integer.parseInt(updateAr);
            updateTotal = String.valueOf(total);

            if (updatePlot.matches("") || updateBt.matches("") || updateTh.matches("") || updateAr.matches("")) {
                Toast.makeText(this, "Input Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
            } else if ((Integer.parseInt(updateBt) > 100) || (Integer.parseInt(updateTh) > 100) || (Integer.parseInt(updateAr) > 100)) {
                Toast.makeText(this, "Input Tidak Boleh Lebih Dari 100", Toast.LENGTH_SHORT).show();
            } else if ((Integer.parseInt(updateBt) < 10) || (Integer.parseInt(updateTh) < 10) || (Integer.parseInt(updateAr) < 10)) {
                Toast.makeText(this, "Input Tidak Boleh Kurang Dari 10", Toast.LENGTH_SHORT).show();
            } else {
                updateDatAChopper();
            }
        });

        btDelete.setOnClickListener(v -> {
            confirmDelete(String.valueOf(choppers.getId()));
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